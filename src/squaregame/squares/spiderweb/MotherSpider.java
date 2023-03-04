package squaregame.squares.spiderweb;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;

public class MotherSpider extends SpiderBase {
    private static final int STARTING_GENERATIONS = 2;

    private Direction currDirection = Direction.values()[SpiderwebUtils.random.nextInt(Direction.values().length)];
    private int generationsLeft;
    private int spawns;

    public MotherSpider() {
        this(0, STARTING_GENERATIONS);
    }

    public MotherSpider(int generationsOld, int generationsLeft) {
        super(generationsOld);
        this.generationsLeft = generationsLeft;
    }

    @Override
    public SquareAction doRun(SquareView view) {
        if (spawns >= 8) {
            return new SpiderWeb(generationsOld).run(view);
        }

        var emptyDirections = view.getEmptyDirections();

        int tried = 0;
        while (tried < 8 && !emptyDirections.contains(currDirection)) {
            tried++;
            currDirection = currDirection.rotateClockwise(-1);
        }

        if (emptyDirections.contains(currDirection)) {
            var direction = currDirection;
            currDirection = currDirection.rotateClockwise(-1);
            spawns++;
            return SquareAction.replicate(direction, this, new EggSac(generationsOld + 1, direction, generationsLeft));
        }

        return new SpiderWeb(generationsOld).run(view);
    }
}
