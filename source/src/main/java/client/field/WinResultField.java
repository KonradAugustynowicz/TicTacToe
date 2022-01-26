package client.field;

import java.awt.*;

public class WinResultField implements IField {

    private final IField field;

    public WinResultField(IField field) {
        this.field = field;
    }

    @Override
    public void draw(Graphics g, int x, int y, int tilesize) {
        g.setColor(Color.GREEN);
        field.draw(g,x,y,tilesize);
    }

    @Override
    public FieldType getType() {
        return field.getType();
    }
}
