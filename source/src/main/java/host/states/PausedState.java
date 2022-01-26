package host.states;

import host.Client;
import host.Game;

public class PausedState implements GameState{
    private static final GameState instance = new PausedState();

    private PausedState() {
    }

    public static GameState getState(){
        return instance;
    }

    @Override
    public String play(Client client1, Client client2, Game game) {
        String msg;
        if (game.firstPlayerTurn) {
            msg = client1.read();
        }
        else {
            msg = client2.read();
        }
        if(msg.equals("PAUSE")){
            game.setState(RunningState.getState());
        }
        return "";
    }
}
