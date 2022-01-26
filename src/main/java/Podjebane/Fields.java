package Podjebane;

import client.field.IField;
import client.field.Field;

import javax.swing.*;
import java.awt.*;

public class Fields extends JPanel {
    private IField[][] matrix;
    private int tilesize;
    // kafelek podświetlony (myszką)
    private int hx = - 1, hy = - 1;

    // inicjalizacja macierzy
    public Fields(int cols, int rows, int tilesize) {
        this.setPreferredSize(new Dimension(cols * tilesize, rows * tilesize));
        this.tilesize = tilesize;
        matrix = new IField[rows][cols];
        for (int i = 0; i < matrix.length; ++ i) {
            for (int j = 0; j < matrix[i].length; ++ j) {
                matrix[i][j] = Field.getField(FieldType.BLANK);
            }
        }
        this.setBackground(Color.BLACK);
    }

    // rysowanie macierzy (oraz jednego podświetlonego)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < matrix.length; ++ y) {
            for (int x = 0; x < matrix[y].length; ++ x) {
                g.setColor(Color.WHITE);
                if (y == hy && x == hx) {
                    if (Gameboard.yourTurn && isBlank(y,x))
                        g.setColor(Color.WHITE.darker());
                }
                matrix[y][x].draw(g, x, y, tilesize);
                // System.out.println(matrix[y][x]);
            }
        }
        //System.out.println();
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

    public IField getAt(int row, int col) {
        return matrix[row][col];
    }

    public void setTypeAt(int row, int col, FieldType type) {
        matrix[row][col] = Field.getField(type);
    }
    public void setAt(int row,int col,IField field){
        matrix[row][col] = field;
    }

    public boolean isBlank(int row, int col) {
        return matrix[row][col].getType() == FieldType.BLANK;
    }
}
