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

        new Gameboard(new JFrame());

        Thread read = new Thread(() -> {
            while (true) {
                try {
                    if (input.available() > 0) {
                        int d = 0;
                        String msg = "";
                        //bomba
                        int i=0;
                        boolean numberCheck =true;
                        //koniec bomby
                        while ((d = input.read()) != 38) {
                            //bomba
                            if (((char) d == 'O' || (char) d == 'X')){
                                //Gameboard.kafelki.getAt(i/3,i%3).setValue(true);
                            }else if (numberCheck){
                                numberCheck=false;
                            }else{
                                numberCheck=true;
                                i++;
                            }
                            //koniec bomby
                            msg = msg + (char) d;
                        }
                        //bomba
                        if (msg.equals("Please enter from 1-9: "))
                            Gameboard.yourTourn=true;
                        //koniec bomby
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
                    System.out.println(Gameboard.clicked);
                    //bomba
                    if (Gameboard.clicked != 0){
                        output.write((Gameboard.clicked + "&").getBytes());
                    }else{
                        output.write((msg + "&").getBytes()); //to powinno nie być zakomentowane
                    }
                    //koniec bomby
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        write.start();
    }
}