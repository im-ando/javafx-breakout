package jp.imagemagic.breakout;

public record Vector(double x, double y) {
    public Vector move(double dx, double dy) {
        return new Vector(x + dx, y + dy);
    }

    public Vector move(Vector v) {
        return move(v.x, v.y);
    }

    public Vector scalar(double n) {
        return scalar(n, n);
    }

    public Vector scalar(double nx, double ny) {
        return new Vector(x * nx, y * ny);
    }
}
