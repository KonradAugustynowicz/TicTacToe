import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2000);

        OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();

        Thread read = new Thread(() -> {
            while (true) {
                try {
                    if (input.available() > 0) {
                        int d = 0;
                        String msg = "";
                        while ((d = input.read()) != 38) {
                            msg = msg + (char) d;
                        }
                        System.out.println(msg);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        read.start();

        Thread write = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                try {
                    output.write((msg + "&").getBytes());
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        write.start();
    }
}