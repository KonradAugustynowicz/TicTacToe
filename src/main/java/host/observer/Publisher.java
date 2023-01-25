package host.observer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Publisher {

    private final List<Subscriber> subscriberList;

    private Socket socket = null;


    public Publisher(ServerSocket serverSocket) {
        subscriberList = new LinkedList<>();
        CompletableFuture.runAsync(() -> {
           while (true) {
               System.out.println("Launching Async Task!");
               try {
                   socket = serverSocket.accept();
                   System.out.println("Found connection!");
               } catch (IOException e) {
                   e.printStackTrace();
               }

               if (socket != null) {
                   Subscriber s = new Subscriber(socket);
                   subscriberList.add(s);
                   s.write("INIT;SPECTATOR");
               }
           }
        });
    }

    public void Subscribe(Socket socket) {
        subscriberList.add(new Subscriber(socket));
    }

    public void Unsubscribe(Socket socket) {
        subscriberList.removeIf(s -> s.getSocket() == socket);
    }

    public void NotifySubscribers(String message) {
        for (Subscriber s :
                subscriberList) {
            s.Update(message);
        }
    }
}
