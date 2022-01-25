package Podjebane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel {
    JLabel label=new JLabel("Circle and Cross");
    JButton startButton=new JButton("Start");
    JButton historyButton=new JButton("Historia");
    JButton exitButton = new JButton("Exit");
    GridBagConstraints gbc=new GridBagConstraints();

    public StartMenu() {
        this.setLayout(new GridBagLayout());
        startButton.setPreferredSize(new Dimension(80, 30));
        startButton.setFont(new Font("Arial", Font.PLAIN, 15));

        label.setFont(new Font("Arial", Font.PLAIN, 35));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,50,0);
        this.add(label,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,0,0);
        this.add(startButton,gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5,0,0,0);
        this.add(historyButton,gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10,0,0,0);
        this.add(exitButton,gbc);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }
}
