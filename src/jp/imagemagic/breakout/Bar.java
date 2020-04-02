package jp.imagemagic.breakout;

final record Bar(Vector pos, Vector size) implements Drawable {
    static final Vector initialSize = new Vector(50, 10);

    static Bar init(Vector base, Vector size) {
        return new Bar(base.move(size.scalar(-0.5, 0)), size);
    }

    Vector anotherPos() {
        return pos.move(size);
    }

    Bar move(double x) {
        return new Bar(new Vector(x - size().x() / 2, pos.y()), size);
    }
}
