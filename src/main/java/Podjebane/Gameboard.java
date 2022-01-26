package Podjebane;

import client.field.LoseResultField;
import client.field.WinResultField;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.*;


public class Gameboard implements Runnable {
    static final int TILESIZE = 80;
    final public Fields kafelki = new Fields(3, 3, TILESIZE);
    FieldType type;
    Socket socket;
    JFrame frame;
    OutputStream output;
    InputStream input;
    static boolean yourTurn = false;

    public Gameboard(JFrame frame, FieldType type, Socket socket) throws IOException {
        this.type = type;
        this.socket = socket;
        output = socket.getOutputStream();
        input = socket.getInputStream();
        System.setProperty("sun.java2d.opengl", "true");
        System.out.println(type);
        // konstruowanie okna
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame = frame;
        this.frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.getContentPane().add(kafelki, gbc);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // reakcja na kliknięcie uruchomienie wątku z iteracją
        kafelki.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (yourTurn) {
                    int x = e.getX() / TILESIZE;
                    int y = e.getY() / TILESIZE;
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) {
                        if (kafelki.isBlank(y, x))
                            try {
                                output.write(("UPDATE;" + x + ';' + y + ";" + type + "&").getBytes());
                                output.flush();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                    }
                }
            }
        });
        // reakcja na ruch - podświetlenie wskazanego kafelka
        kafelki.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / TILESIZE;
                int y = e.getY() / TILESIZE;
                kafelki.highlight(x, y);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (input.available() > 0) {
                    int d;
                    String msg = "";
                    while ((d = input.read()) != 38) {
                        msg = msg + (char) d;
                    }
                    String[] split = msg.split(";");
                    if (split[0].equals("UPDATE")) {
                        int x = Integer.parseInt(split[1]);
                        int y = Integer.parseInt(split[2]);
                        FieldType incomingType = split[3].equals("CIRCLE") ? FieldType.CIRCLE : FieldType.CROSS;
                        kafelki.setTypeAt(y, x, incomingType);
                    }
                    if (split[0].equals("move"))
                        yourTurn = true;
                    if (split[0].equals("wait"))
                        yourTurn = false;
                    if (msg.equals("DRAW"))
                        showExitDialog("Draw");
                    if (split[0].equals("WIN")) {
                        for(int i=1;i<split.length;i+=2){
                            int x = Integer.parseInt(split[i]);
                            int y = Integer.parseInt(split[i+1]);
                            kafelki.setAt(y,x,new WinResultField(kafelki.getAt(y,x)));
                        }
                    }
                    if (split[0].equals("LOSE")) {
                        for(int i=1;i<split.length;i+=2){
                            int x = Integer.parseInt(split[i]);
                            int y = Integer.parseInt(split[i+1]);
                            kafelki.setAt(y,x,new LoseResultField(kafelki.getAt(y,x)));
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showExitDialog(String message) {
        int result = JOptionPane.showConfirmDialog(frame,
                message, "Game over",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION)
            frame.dispose();
    }
}
