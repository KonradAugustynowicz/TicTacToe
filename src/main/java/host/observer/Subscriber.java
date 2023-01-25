package host.observer;

import java.io.IOException;
import java.net.Socket;

public record Subscriber(Socket socket) implements ISubscriber {

    @Override
    public void Update(String updateMessage) {
        write(updateMessage);
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void write(String message) {
        try {
            socket.getOutputStream().write((message+"&").getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
