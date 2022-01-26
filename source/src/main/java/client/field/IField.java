package client.field;

import java.awt.*;

public interface IField {
    void draw(Graphics g, int x, int y, int tilesize);

    FieldType getType();
}
