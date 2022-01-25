package Podjebane;

import java.awt.*;

public enum FieldType {
    BLANK(Color.WHITE),
    CROSS(Color.BLACK),
    CIRCLE(Color.RED);


    private final Color color;

    FieldType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }


}
