package host;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    Socket socket;
    FieldType type;
    OutputStream output;
    InputStream input;

    public Client(Socket socket, FieldType type) {
        this.socket = socket;
        try {
            input=socket.getInputStream();
            output=socket.getOutputStream();

            write("INIT;" + type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.type = type;
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
                    String[] split = msg.split(";");
                    if(split[split.length-1].equals(type.toString()))
                        exit = true;
                    if(msg.equals("PAUSE"))
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
