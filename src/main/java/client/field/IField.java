package client.field;

import client.FieldType;

import java.awt.*;

public interface IField {
    void draw(Graphics g, int x, int y, int tilesize);

    FieldType getType();
}
