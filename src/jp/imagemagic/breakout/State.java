package jp.imagemagic.breakout;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final record State(List<Block> blocks, Ball ball, Bar bar, Status status, int hp) {
    static State init(List<Block> blocks, Vector base, Status status, int hp) {
        return new State(blocks, Ball.init(base, Ball.initialSize), Bar.init(base, Bar.initialSize), status, hp);
    }

    State transBlock(List<Block> blocks) {
        return new State(Collections.unmodifiableList(blocks), ball, bar, status, hp);
    }

    State transBar(UnaryOperator<Bar> trans) {
        return new State(blocks, ball, trans.apply(bar), status, hp);
    }

    State transBall(UnaryOperator<Ball> trans) {
        return new State(blocks, trans.apply(ball), bar, status, hp);
    }

    State transStatus(Status status) {
        return new State(blocks, ball, bar, status, hp);
    }

    View view() {
        return new View(Stream.concat(Stream.of(ball, bar), blocks.stream())
                .collect(Collectors.toList()));
    }

    boolean valid() {
        return hp > 0;
    }

    boolean clear() {
        return blocks.isEmpty();
    }
}
