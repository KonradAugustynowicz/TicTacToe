package host;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Socket socket = null;
        while (true) {
            //listening for client 1
            System.out.println("waiting client 1...........");
            socket = serverSocket.accept();
            Client c1 = new Client(socket, FieldType.CIRCLE);
            System.out.println("client 1 joined..........");

            //listening for client2
            System.out.println("waiting client 2...........");
            socket = serverSocket.accept();
            Client c2 = new Client(socket,FieldType.CROSS);
            System.out.println("client 2 joined..........");
            Game game = new Game(c1,c2, serverSocket);
            game.run();
        }
    }
}
