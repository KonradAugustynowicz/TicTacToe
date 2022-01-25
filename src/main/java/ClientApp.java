import Podjebane.FieldType;
import Podjebane.Gameboard;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        Socket socket = new Socket("localhost", 2000);

        OutputStream output = socket.getOutputStream();
        InputStream input = socket.getInputStream();



        while (true) {
            if (input.available() > 0) {
                int d;
                String msg = "";
                while ((d = input.read()) != 38) {
                    msg = msg + (char) d;
                }
                String[] split = msg.split(";");
                if(split[0].equals("INIT")) {
                    if (split[1].equals("CIRCLE")) {
                        new Gameboard(new JFrame(), FieldType.CIRCLE, socket).run();
                    }
                    else {
                        new Gameboard(new JFrame(), FieldType.CROSS, socket).run();
                    }
                    break;
                }
            }
        }


    }
}