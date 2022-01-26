package states;

import host.Client;
import host.Game;

public class RunningState implements GameState {

    private static final GameState instance = new RunningState();

    public static GameState getState() {
        return instance;
    }

    @Override
    public String play(Client client1, Client client2, Game game) {
        String msg;
        if (game.firstPlayerTurn) {
            client1.write("move");
            client2.write("wait");
            msg = client1.read();
        }
        else {
            client1.write("wait");
            client2.write("move");
            msg = client2.read();
        }
        if (! msg.equals("PAUSE"))
            game.firstPlayerTurn = ! game.firstPlayerTurn;
        return msg;
    }
}
