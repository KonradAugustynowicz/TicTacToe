package states;

import host.Client;
import host.Game;

public interface GameState {
    String play(Client client1, Client client2, Game game);
}
