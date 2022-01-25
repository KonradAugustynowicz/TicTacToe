package game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    Socket socket;
    InputStream input;
    OutputStream output;
    String name;

    Client(Socket socket) throws IOException {
        this.socket = socket;
        input = socket.getInputStream();
        output = socket.getOutputStream();
        initialSetup();
    }

    private void initialSetup() {
        //client name.........
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        write("Type your name: ");
        name = read();
        System.out.println(name);
    }

    public String read(){
        String msg = "";
        boolean exit = false;
        while (!exit){
            try {
                if (input.available() > 0) {
                    int d;
                    while ((d = input.read()) != 38) {
                        msg = msg + (char) d;
                    }
                    exit = true;
                }
            } catch (IOException e) {
                System.out.println("Error reading msg..........");
            }
        }
        return msg;
    }

    public void write(String msg){
        try {
            output.write((msg+"&").getBytes());
            output.flush();
        } catch (IOException e) {
            System.out.println("Error sending msg...........");
        }
    }

    public void close() {
        try {
            socket.close();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
