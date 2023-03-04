package squaregame.squares.spiderweb;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;

import java.util.Random;

public class SpiderWeb extends SpiderBase {
    public static final int START_RADIUS = 5;
    public static Random random = new Random();

    private int radius;
    // The direction this square came from
    private Direction originDirection;

    public SpiderWeb(int generationsOld) {
        this(
            generationsOld,
            START_RADIUS + random.nextInt(5),
            Direction.values()[random.nextInt(Direction.values().length)]
        );
    }

    public SpiderWeb(int generationsOld, int radius, Direction originDirection) {
        super(generationsOld);
        this.radius = radius;
        this.originDirection = originDirection;
    }

    @Override
    public SquareAction doRun(SquareView view) {
        var empty = view.getEmptyDirections();

        if (empty.isEmpty())
            return Wait();

        if (radius == 0) {
            // choose a new random direction to replicate in
            var randomDirection = empty.get(random.nextInt(empty.size()));
            var newRadius = START_RADIUS + random.nextInt(5);
            return SquareAction.replicate(originDirection, this, new SpiderWeb(generationsOld + 1, newRadius, randomDirection));
        }

        // keep going in same direction
//        if (radius > 0 && empty.contains(originDirection)) {
            return SquareAction.replicate(originDirection, this, new SpiderWeb(generationsOld + 1, radius - 1, originDirection));
//        }
//
//        return Wait();
    }
}
