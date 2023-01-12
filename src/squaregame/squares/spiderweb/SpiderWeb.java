package squaregame.squares.spiderweb;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.util.Random;

public class SpiderWeb extends SquareLogic {
    public static final int START_RADIUS = 5;
    public static Random random = new Random();

    private int radius;
    // The direction this square came from
    private Direction originDirection;

    public SpiderWeb(int radius, Direction originDirection) {
        this.radius = radius;
        this.originDirection = originDirection;
    }

    @Override
    public SquareAction run(SquareView view) {
        var empty = view.getEmptyDirections();

        if (empty.isEmpty())
            return Wait();

        if (radius == 0) {
            // choose a new random direction to move in
            var randomDirection = empty.get(random.nextInt(empty.size()));
            var newRadius = START_RADIUS + random.nextInt(5);
            return SquareAction.replicate(originDirection, this, new SpiderWeb(START_RADIUS, randomDirection));
        }

        // keep going in same direction
        if (radius > 0 && empty.contains(originDirection)) {
            return SquareAction.replicate(originDirection, this, new SpiderWeb(radius - 1, originDirection));
        }

//        } else if (!empty.isEmpty()) {
//            var randomDirection = empty.get(random.nextInt(empty.size()));
//            return SquareAction.move(randomDirection, );
//        }

        return Wait();
    }

    @Override
    public String getSquareName() {
        return "Spiderweb";
    }
}
