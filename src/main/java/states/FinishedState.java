package states;

public class FinishedState implements GameState{
    private static final GameState instance = new FinishedState();

    public static GameState getState(){
        return instance;
    }
}
