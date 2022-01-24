package Podjebane;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;


public class Gameboard {
    static final int TILESIZE = 40;
    public static int clicked = 0;
    public static boolean yourTourn=false;
    final public static Fields kafelki = new Fields(3, 3, TILESIZE);

    JFrame frame = new JFrame("Iterator");

    public Gameboard(JFrame frame) {

        this.frame = frame;
        System.setProperty("sun.java2d.opengl", "true");

        // konstruowanie okna
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(kafelki);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        // reakcja na kliknięcie uruchomienie wątku z iteracją
        kafelki.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (yourTourn){
                    int x = e.getX() / TILESIZE;
                    int y = e.getY() / TILESIZE;
                    clicked = 1+x + (y*3);
                    kafelki.getAt(y,x).flip();
                    yourTourn=false;
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
}
