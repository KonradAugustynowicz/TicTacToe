package Podjebane;

import javax.swing.*;
import java.awt.*;

public class Fields extends JPanel {
    private Field[][] matrix;
    private int tilesize;
    // kafelek podświetlony (myszką)
    private int hx = -1, hy = -1;

    // inicjalizacja macierzy
    public Fields(int cols, int rows, int tilesize) {
        this.setPreferredSize(new Dimension(cols * tilesize, rows * tilesize));
        this.tilesize = tilesize;
        matrix = new Field[rows][cols];
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                matrix[i][j] = new Field();
            }
        }
        this.setBackground(Color.BLACK);
    }

    // rysowanie macierzy (oraz jednego podświetlonego)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (i == hy && j == hx) {
                    g.setColor(matrix[i][j].getColor().brighter());
                } else {
                    g.setColor(matrix[i][j].getColor());
                }
                g.fillRect(j * tilesize, i * tilesize + 1, tilesize - 1, tilesize - 1);
            }
        }
    }

    // podświetl
    public void highlight(int x, int y) {
        hx = x;
        hy = y;
        repaint();
    }

    // trzy poniższe metody znikną w finalnej wersji
    public int getRows() {
        return matrix.length;
    }

    public int getCols() {
        return matrix[0].length;
    }

    public Field getAt(int row, int col) {
        return matrix[row][col];
    }
}
