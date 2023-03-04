package squaregame.squares.spiderweb;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;

public class EggSac extends SpiderBase {
    public static int TRAVEL_DISTANCE_BASE = 20, TRAVEL_DISTANCE_VARIANCE = 20;

    private Direction direction;
    private int distanceToTravel;
    private int generationsLeft;

    public EggSac(int generationsOld, Direction direction, int generationsLeft) {
        super(generationsOld);
        this.direction = direction;
        this.generationsLeft = generationsLeft;
        this.distanceToTravel = TRAVEL_DISTANCE_BASE + SpiderwebUtils.random.nextInt(TRAVEL_DISTANCE_VARIANCE);
    }

    @Override
    public SquareAction doRun(SquareView view) {
        if (distanceToTravel > 0) {
            distanceToTravel--;

            var emptyDirs = view.getEmptyDirections();
            if (emptyDirs.contains(direction)) {
                return SquareAction.move(direction, this);
            }

            // couldn't go directly in that direction, can we go one of the adjacent directions?
            var adjacent = SpiderwebUtils.adjacentDirs(direction);
            for (var dir : adjacent) {
                if (emptyDirs.contains(dir)) {
                    return SquareAction.move(direction, this);
                }
            }
        }

        // if we made it this far, settle down
        if (generationsLeft > 0) {
            return new MotherSpider(generationsOld, generationsLeft - 1).run(view);
        } else {
            return new SpiderWeb(generationsOld).run(view);
        }
    }
}
