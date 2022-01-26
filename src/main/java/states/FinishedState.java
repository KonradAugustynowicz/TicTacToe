package states;

import host.Client;
import host.Game;

public class FinishedState implements GameState{
    private static final GameState instance = new FinishedState();

    public static GameState getState(){
        return instance;
    }

    private FinishedState() {
    }

    @Override
    public String play(Client client1, Client client2, Game game) {
        return "";
    }
}
