package client.field;

import Podjebane.FieldType;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

public class Field implements IField {

    private FieldType type;

    private final static HashMap<FieldType, IField> fields = new HashMap<>();

    public static IField getField(FieldType type) {
        if (! fields.containsKey(type))
            fields.put(type, new Field(type));
        return fields.get(type);
    }

    private Field(FieldType type) {
        this.type = type;
    }

    @Override
    public void draw(Graphics g, int x, int y, int tilesize) {
        g.fillRect(x * tilesize, y * tilesize + 1, tilesize - 1, tilesize - 1);

        if (type == FieldType.CIRCLE) {
            Graphics2D g2d = (Graphics2D) g;
            Stroke defaultStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(5));
            Ellipse2D outer = new Ellipse2D.Double((x * tilesize) + 5, (y * tilesize) + 5,tilesize-10,tilesize-10);
            g2d.setColor(type.getColor());
            g2d.draw(outer);
            g2d.setStroke(defaultStroke);
        }
        if (type == FieldType.CROSS) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(type.getColor());
            Stroke defaultStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(5));
            g2d.drawLine((x * tilesize) + 5, (y * tilesize) + 5, (x * tilesize) + tilesize - 5, (y * tilesize) + tilesize - 5);
            g2d.drawLine((x * tilesize) + 5, (y * tilesize) + tilesize - 5, (x * tilesize) + tilesize - 5, (y * tilesize) + 5);
            g2d.setStroke(defaultStroke);
        }
    }

    @Override
    public FieldType getType() {
        return this.type;
    }
}


