package jp.imagemagic.breakout;

enum ReflectDirection {
    Horizontal,
    Vertical,
    Both;

    ReflectDirection add(ReflectDirection reflectDirection) {
        if (this.equals(reflectDirection)) {
            return this;
        }

        return ReflectDirection.Both;
    }
}
