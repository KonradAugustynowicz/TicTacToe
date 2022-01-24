package Podjebane;

import java.awt.*;

public class Field {
    // schowek na wartość logiczną

    private boolean value = false;
    // kolory

    private static final Color on = new Color(0xffd700), off = new Color(0xFFFFFF);

    // odczyt koloru
    public Color getColor() {
        return value ? on : off;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    // zmiana koloru
    public void flip() {
        value = !value;
    }
}
