package jp.imagemagic.breakout;

final record Ball(Vector pos, Vector size, Vector speed) implements Drawable {
    static final Vector initialSize = new Vector(20, 20);

    static Ball init(Vector base, Vector size) {
        return new Ball(base.move(size.scalar(0.5, 1).scalar(-1)), size, new Vector(0, 0));
    }

    Ball follow(double x) {
        return new Ball(new Vector(size().scalar(-0.5, 0).x() + x, pos.y()), size, speed);
    }

    Ball move() {
        return new Ball(new Vector(pos.x() + speed.x(), pos.y() + speed.y()), size, speed);
    }

    Ball changeSpeed(double x, double y) {
        return new Ball(pos, size, new Vector(x, y));
    }

    Ball reflect(ReflectDirection reflectDirection) {
        final var speed = switch (reflectDirection) {
            case Horizontal -> speed().scalar(-1, 1);
            case Vertical -> speed().scalar(1, -1);
            case Both -> speed().scalar(-1);
        };
        return new Ball(pos, size, speed);
    }

    Vector anotherPos() {
        return pos.move(size);
    }

    @Override
    public DrawType drawType() {
        return DrawType.Oval;
    }
}
