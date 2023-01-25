package client;

import client.field.FieldType;
import client.field.LoseResultField;
import client.field.WinResultField;
import client.memento.History;
import client.memento.Originator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.*;


public class Gameboard implements Runnable {
    static final int TILESIZE = 80;
    private Fields fields = new Fields(3, 3, TILESIZE);
    final public StartMenu startMenu = new StartMenu();
    FieldType type;
    Socket socket;
    JFrame frame;
    OutputStream output;
    InputStream input;
    static boolean yourTurn = false;
    static boolean paused = false;

    Boolean isHistory = false;
    Originator originator;
    History history;

    String msg = "";
    int mementoIndex = 0;

    //Buttons
    JButton leftButton = new JButton("<");
    JButton rightButton = new JButton(">");
    JLabel historyLabel = new JLabel("Historia");
    JLabel mainLabel = new JLabel();


    public Gameboard(JFrame frame, FieldType type, Socket socket) throws IOException {
        this.type = type;
        this.socket = socket;
        output = socket.getOutputStream();
        input = socket.getInputStream();
        System.setProperty("sun.java2d.opengl", "true");
        System.out.println(type);


        originator = new Originator();
        history = new History();

        // konstruowanie okna
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame = frame;
        this.frame.setLayout(new GridBagLayout());
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.getContentPane().add(startMenu);

        startMenu.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();

                mainLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.insets = new Insets(0, 0, 10, 0);
                frame.getContentPane().add(mainLabel, gbc);

                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.insets = new Insets(0, 0, 0, 0);
                frame.getContentPane().add(fields, gbc);

                JButton pauseButton = new JButton("Pause");
                pauseButton.addActionListener(e1 -> {
                    if (yourTurn) {
                        try {
                            output.write(("PAUSE&").getBytes());
                            output.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.insets = new Insets(10, 0, 0, 0);
                frame.getContentPane().add(pauseButton, gbc);
                historyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                historyLabel.setVisible(false);
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.insets = new Insets(10, 0, 0, 0);
                frame.getContentPane().add(historyLabel, gbc);
                leftButton.setVisible(false);
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.insets = new Insets(10, 0, 0, - 120);
                frame.getContentPane().add(leftButton, gbc);
                rightButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (mementoIndex < history.getListSize() - 1) {
                            msg = originator.getState();
                            mementoIndex++;
                            originator.getStateFromMemento(history.getMemento(mementoIndex));
                        }
                    }
                });
                rightButton.setVisible(false);
                gbc.gridx = 2;
                gbc.gridy = 3;
                gbc.insets = new Insets(10, - 120, 0, 0);
                frame.getContentPane().add(rightButton, gbc);

                frame.getContentPane().repaint();
                frame.revalidate();
            }
        });

        // reakcja na kliknięcie uruchomienie wątku z iteracją
        fields.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (yourTurn && ! paused) {
                    int x = e.getX() / TILESIZE;
                    int y = e.getY() / TILESIZE;
                    if (x >= 0 && x <= 2 && y >= 0 && y <= 2) {
                        if (fields.isBlank(y, x))
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
        fields.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() / TILESIZE;
                int y = e.getY() / TILESIZE;
                fields.highlight(x, y);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            if (! isHistory)
                msg = "";
            try {
                if (input.available() > 0 || ! msg.isEmpty()) {
                    int d;
                    if (! isHistory) {
                        while ((d = input.read()) != 38) {
                            msg = msg + (char) d;
                        }
                    }
                    String[] split = msg.split(";");
                    if (split[0].equals("UPDATE")) {
                        if (! isHistory) {
                            originator.setState(msg);
                            history.addMemento(originator.saveStateToMemento());
                        }
                        mainLabel.setText("");
                        int x = Integer.parseInt(split[1]);
                        int y = Integer.parseInt(split[2]);
                        FieldType incomingType = split[3].equals("CIRCLE") ? FieldType.CIRCLE : FieldType.CROSS;
                        fields.setTypeAt(y, x, incomingType);
                    }
                    if (msg.equals("PAUSE")) {
                        mainLabel.setText("Pause");
                        paused = ! paused;
                    }
                    if (split[0].equals("move")) {
                        mainLabel.setText("Your move.");
                        yourTurn = true;
                        paused=false;
                    }
                    if (split[0].equals("wait")) {
                        mainLabel.setText("Opponents move.");
                        yourTurn = false;
                        paused=false;
                    }
                    if (msg.equals("DRAW")) {
                        if (! isHistory) {
                            originator.setState(msg);
                            history.addMemento(originator.saveStateToMemento());
                        }
                        leftButton.setVisible(true);
                        rightButton.setVisible(true);
                        historyLabel.setVisible(true);
                        if (! isHistory) {
                            showExitDialog("Draw");
                        }
                    }
                    if (split[0].equals("WIN")) {
                        if (! isHistory) {
                            originator.setState(msg);
                            history.addMemento(originator.saveStateToMemento());
                        }
                        leftButton.setVisible(true);
                        rightButton.setVisible(true);
                        historyLabel.setVisible(true);
                        for (int i = 1; i < split.length; i += 2) {
                            int x = Integer.parseInt(split[i]);
                            int y = Integer.parseInt(split[i + 1]);
                            fields.setAt(y, x, new WinResultField(fields.getAt(y, x)));
                        }
                        if (type == null) {
                            showExitDialog("Game Finished!");
                        }
                        if (! isHistory) {
                            showExitDialog("You won!");
                        }
                    }
                    if (split[0].equals("LOSE")) {
                        if (! isHistory) {
                            originator.setState(msg);
                            history.addMemento(originator.saveStateToMemento());
                        }
                        leftButton.setVisible(true);
                        rightButton.setVisible(true);
                        historyLabel.setVisible(true);
                        for (int i = 1; i < split.length; i += 2) {
                            int x = Integer.parseInt(split[i]);
                            int y = Integer.parseInt(split[i + 1]);
                            fields.setAt(y, x, new LoseResultField(fields.getAt(y, x)));
                        }
                        if (! isHistory) {
                            showExitDialog("You lost :c");
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
                message + "\nDo you want to see replay?", "Game Over",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.NO_OPTION)
            frame.dispose();
        else {
            msg = "";
            isHistory = true;
            originator.getStateFromMemento(history.getMemento(0));
            fields.reset();
        }
    }
}
