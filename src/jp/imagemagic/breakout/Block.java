package jp.imagemagic.breakout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final record Block(Vector pos, Vector size, int hp) implements Drawable {
    private static final Vector initialSize = new Vector(30, 30);
    private static final int[][] initialGrid = {
            {0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            {0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3},
            {0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4},
    };

    static List<Block> initBlocks() {
        return IntStream.range(0, initialGrid.length).boxed()
                .flatMap(y -> IntStream.range(0, initialGrid[y].length).mapToObj(x ->
                        new Block(initialSize.scalar(x, y), initialSize, initialGrid[y][x])))
                .filter(Block::valid)
                .collect(Collectors.toList());
    }

    Vector anotherPos() {
        return pos.move(size);
    }

    boolean valid() {
        return hp > 0;
    }

    Block decrementHp() {
        return new Block(pos, size, hp - 1);
    }
}
