package jp.imagemagic.breakout;

import javafx.scene.paint.Color;

interface Drawable {
    Vector pos();

    Vector size();

    default DrawType drawType() {
        return DrawType.Rect;
    }

    default Color fill() {
        return Color.WHITE;
    }

    default Color stroke() {
        return Color.BLACK;
    }
}
