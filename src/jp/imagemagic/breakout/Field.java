package jp.imagemagic.breakout;

import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final record Field(Vector size) {
    UnaryOperator<State> moveBar(double x) {
        final UnaryOperator<State> trans = s -> s.transBar(bar -> bar.move(x));
        return state -> switch (state.status()) {
                case Active -> trans.apply(state);
                case Inactive -> trans.apply(state).transBall(b -> b.follow(x));
                default -> state;
            };
    }

    UnaryOperator<State> moveBall() {
        return state -> switch (state.status()) {
            case Active -> transMove().andThen(validate()).apply(state);
            default -> state;
        };
    }

    private UnaryOperator<State> transMove() {
        return state -> checkReflect(state.transBall(Ball::move), state.ball())
                .orElse(State.init(state.blocks(), startPos(), Status.Inactive, state.hp() - 1));
    }

    private UnaryOperator<State> validate() {
        return state -> {
            if (!state.valid()) {
                return state.transStatus(Status.GameOver);
            }
            if (state.clear()) {
                return state.transStatus(Status.Clear);
            }

            return state;
        };
    }

    private Optional<State> checkReflect(State state, Ball prev) {
        final var current = state.ball();
        if (current.pos().y() > size.y()) {
            return Optional.empty();
        }
        final var outerReflect = Optional.of(state).flatMap(s -> {
            if (current.pos().x() < 0 || current.anotherPos().x() > size.x()) {
                return Optional.of(ReflectDirection.Horizontal);
            }
            if (current.pos().y() < 0) {
                return Optional.of(ReflectDirection.Vertical);
            }
            if (current.speed().y() > 0 &&
                    current.anotherPos().x() >= state.bar().pos().x() &&
                    current.pos().x() <= state.bar().anotherPos().x() &&
                    current.anotherPos().y() >= state.bar().pos().y() &&
                    current.pos().y() <= state.bar().anotherPos().y()) {
                return Optional.of(ReflectDirection.Vertical);
            }

            return Optional.empty();
        });

        if (outerReflect.isPresent()) {
            return outerReflect.map(rd -> state.transBall(b -> b.reflect(rd)));
        }

        final var spitted = state.blocks().stream()
                .collect(Collectors.partitioningBy(b -> current.anotherPos().x() >= b.pos().x() &&
                        current.pos().x() < b.anotherPos().x() &&
                        current.anotherPos().y() > b.pos().y() &&
                        current.pos().y() < b.anotherPos().y()
                ));
        final var hit = spitted.get(true);
        final var blockReflect = hit.stream().map(b -> {
            if (prev.anotherPos().x() >= b.pos().x() && prev.pos().x() <= b.anotherPos().x()) {
                return ReflectDirection.Vertical;
            }
            if (prev.anotherPos().y() >= b.pos().y() && prev.pos().y() <= b.anotherPos().y()) {
                return ReflectDirection.Horizontal;
            }

            return ReflectDirection.Both;
        }).reduce(ReflectDirection::add);
        final var blocks = Stream.concat(spitted.get(false).stream(), hit.stream()
                .map(Block::decrementHp).filter(Block::valid))
                .collect(Collectors.toList());
        return Optional.of(state.transBlock(blocks))
                .map(s -> blockReflect.map(rd -> s.transBall(b -> b.reflect(rd))).orElse(s));
    }

    UnaryOperator<State> transStatus() {
        return s -> switch (s.status()) {
            case Ready -> s.transStatus(Status.Inactive);
            case Inactive -> s.transStatus(Status.Active)
                    .transBall(b -> b.changeSpeed(1, -1));
            case GameOver, Clear -> initState();
            default -> s;
        };
    }

    State initState() {
        return State.init(Block.initBlocks(), startPos(), Status.Ready, 3);
    }

    private Vector startPos() {
        return size.scalar(0.5, 1).move(0, - 60);
    }
}
