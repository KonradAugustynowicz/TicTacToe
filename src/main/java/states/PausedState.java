package states;

public class PausedState implements GameState{
    private static final GameState instance = new PausedState();

    public static GameState getState(){
        return instance;
    }

    @Override
    public void play() {

    }
}
