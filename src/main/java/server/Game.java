package server;

import Podjebane.FieldType;
import states.GameState;
import states.RunningState;

import java.util.Arrays;

public class Game implements Runnable {

    private final Client client1;
    private final Client client2;
    private GameState state;
    private FieldType[] fields;

    public Game(Client client1, Client client2) {
        this.client1 = client1;
        this.client2 = client2;
        fields = new FieldType[9];
        Arrays.fill(fields, FieldType.BLANK);
        state = RunningState.getState();
    }

    @Override
    public void run() {
        boolean firstPlayerTurn = true;
        boolean gameOver = false;
        while (! gameOver) {
            String msg;
            if (firstPlayerTurn) {
                client1.write("move");
                client2.write("wait");
                msg = client1.read();
                firstPlayerTurn = false;
            }
            else {
                client1.write("wait");
                client2.write("move");
                msg = client2.read();
                firstPlayerTurn = true;
            }
            String[] split = msg.split(";");
            if (split[0].equals("UPDATE")) {
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                FieldType type = split[3].equals("CIRCLE") ? FieldType.CIRCLE : FieldType.CROSS;
                fields[x+3*y]=type;
            }
            client1.write(msg);
            client2.write(msg);
            gameOver = checkForEnd();

        }
    }

    private boolean checkForEnd() {
        boolean draw = !Arrays.asList(fields).contains(FieldType.BLANK);
        if(draw) {
            client1.write("DRAW");
            client2.write("DRAW");
            client1.close();
            client2.close();
            return true;
        }
        return false;
    }
}
