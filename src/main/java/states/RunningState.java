package states;

public class RunningState implements GameState{

    private static final GameState instance = new RunningState();

    public static GameState getState(){
        return instance;
    }
}
