package server;

public class Game implements Runnable {

    private final Client client1;
    private final Client client2;

    public Game(Client client1, Client client2) {

        this.client1 = client1;
        this.client2 = client2;
    }

    @Override
    public void run() {
        boolean firstPlayerTurn = true;
        while (true) {
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
            client1.write(msg);
            client2.write(msg);
        }
    }
}
