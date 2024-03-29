package host;

import client.field.FieldType;
import host.observer.Publisher;
import host.states.FinishedState;
import host.states.GameState;
import host.states.PausedState;
import host.states.RunningState;

import java.net.ServerSocket;
import java.util.Arrays;

public class Game implements Runnable {

    private final Client client1;
    private final Client client2;

    private final Publisher publisher;

    public void setState(GameState state) {
        this.state = state;
    }

    private GameState state;
    private FieldType[] fields;
    public boolean firstPlayerTurn = true;

    public Game(Client client1, Client client2, ServerSocket serverSocket) {
        this.client1 = client1;
        this.client2 = client2;
        this.publisher = new Publisher(serverSocket);
        fields = new FieldType[9];
        Arrays.fill(fields, FieldType.BLANK);
        state = RunningState.getState();
    }

    @Override
    public void run() {
        boolean gameOver = false;
        while (! gameOver) {
            String msg;

            msg = state.play(client1, client2,this);

            String[] split = msg.split(";");
            if (msg.equals("PAUSE") && !(state instanceof PausedState))
                state = PausedState.getState();
            if (split[0].equals("UPDATE")) {
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                FieldType type = split[3].equals("CIRCLE") ? FieldType.CIRCLE : FieldType.CROSS;
                fields[x + 3 * y] = type;
            }
            client1.write(msg);
            client2.write(msg);
            publisher.NotifySubscribers(msg);
            gameOver = checkForEnd();
        }
        state = FinishedState.getState();
        client1.close();
        client2.close();
    }

    private boolean checkForEnd() {
        //check rows
        for (int row = 0; row <= 6; row += 3) {
            if (fields[row] == fields[row + 1] && fields[row] == fields[row + 2]) {
                if (fields[row] != FieldType.BLANK) {
                    int y = row / 3;
                    if (fields[row] == FieldType.CIRCLE) {
                        client1.write("WIN;" + 0 + ";" + y + ";" + 1 + ";" + y + ";" + 2 + ";" + y);
                        client2.write("LOSE;" + 0 + ";" + y + ";" + 1 + ";" + y + ";" + 2 + ";" + y);
                    }
                    else {
                        client1.write("LOSE;" + 0 + ";" + y + ";" + 1 + ";" + y + ";" + 2 + ";" + y);
                        client2.write("WIN;" + 0 + ";" + y + ";" + 1 + ";" + y + ";" + 2 + ";" + y);
                    }
                    publisher.NotifySubscribers("WIN;" + 0 + ";" + y + ";" + 1 + ";" + y + ";" + 2 + ";" + y);

                    return true;
                }
            }
        }
        //check columns
        for (int column = 0; column < 3; column++) {
            if (fields[column] == fields[column + 3] && fields[column] == fields[column + 6]) {
                if (fields[column] != FieldType.BLANK) {
                    int x = column;
                    if (fields[column] == FieldType.CIRCLE) {
                        client1.write("WIN;" + x + ";" + 0 + ";" + x + ";" + 1 + ";" + x + ";" + 2);
                        client2.write("LOSE;" + x + ";" + 0 + ";" + x + ";" + 1 + ";" + x + ";" + 2);
                    }
                    else {
                        client1.write("LOSE;" + x + ";" + 0 + ";" + x + ";" + 1 + ";" + x + ";" + 2);
                        client2.write("WIN;" + x + ";" + 0 + ";" + x + ";" + 1 + ";" + x + ";" + 2);
                    }
                    publisher.NotifySubscribers("WIN;" + x + ";" + 0 + ";" + x + ";" + 1 + ";" + x + ";" + 2);
                    return true;
                }
            }
        }

        //check diagonal
        if (fields[0] == fields[4] && fields[0] == fields[8]) {
            if (fields[0] != FieldType.BLANK) {
                if (fields[0] == FieldType.CIRCLE) {
                    client1.write("WIN;" + 0 + ";" + 0 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 2);
                    client2.write("LOSE;" + 0 + ";" + 0 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 2);
                }
                else {
                    client1.write("LOSE;" + 0 + ";" + 0 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 2);
                    client2.write("WIN;" + 0 + ";" + 0 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 2);
                }
                publisher.NotifySubscribers("WIN;" + 0 + ";" + 0 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 2);
                return true;
            }
        }
        if (fields[6] == fields[4] && fields[6] == fields[2]) {
            if (fields[6] != FieldType.BLANK) {
                if (fields[6] == FieldType.CIRCLE) {
                    client1.write("WIN;" + 0 + ";" + 2 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 0);
                    client2.write("LOSE;" + 0 + ";" + 2 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 0);
                }
                else {
                    client1.write("LOSE;" + 0 + ";" + 2 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 0);
                    client2.write("WIN;" + 0 + ";" + 2 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 0);
                }
                publisher.NotifySubscribers("WIN;" + 0 + ";" + 2 + ";" + 1 + ";" + 1 + ";" + 2 + ";" + 0);
                return true;
            }
        }
        if (! Arrays.asList(fields).contains(FieldType.BLANK)) {
            client1.write("DRAW");
            client2.write("DRAW");
            publisher.NotifySubscribers("DRAW");
            return true;
        }
        return false;
    }
}
