package jp.imagemagic.breakout;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class ViewUnit {
    private final Field field;
    private State state;

    ViewUnit(double w, double h) {
        field = new Field(new Vector(w, h));
        state = field.initState();
    }

    void trans(UnaryOperator<State> trans) {
        state = trans.apply(state);
    }

    UnaryOperator<State> moveBar(double x) {
        return field.moveBar(x);
    }

    UnaryOperator<State> moveBall() {
        return field.moveBall();
    }

    UnaryOperator<State> transStatus() {
        return field.transStatus();
    }

    void drawView(Consumer<Drawable> draw, Consumer<Status> statusDraw) {
        state.view().objects().forEach(draw);
        statusDraw.accept(state.status());
    }
}
