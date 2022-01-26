package client.field;

import Podjebane.FieldType;

import java.awt.*;

public class LoseResultField implements IField {

    private final IField field;

    public LoseResultField(IField field) {
        this.field = field;
    }

    @Override
    public void draw(Graphics g, int x, int y, int tilesize) {
        g.setColor(Color.RED);
        field.draw(g,x,y,tilesize);
    }

    @Override
    public FieldType getType() {
        return field.getType();
    }
}
