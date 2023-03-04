package squaregame.squares.spiderweb;

import squaregame.model.Direction;

import java.util.Random;

public class SpiderwebUtils {
    public static Random random = new Random();

    // TODO maybe cache this so we don't allocate each time?
    // But what if someone mutates the cached array???
    public static Direction[] adjacentDirs(Direction dir) {
        switch (dir) {
            case NW:
                return new Direction[] { Direction.N, Direction.W };
            case N:
                return new Direction[] { Direction.NW, Direction.NE };
            case NE:
                return new Direction[] { Direction.N, Direction.E };
            case E:
                return new Direction[] { Direction.NE, Direction.SE };
            case SE:
                return new Direction[] { Direction.S, Direction.E };
            case S:
                return new Direction[] { Direction.SE, Direction.SW };
            case SW:
                return new Direction[] { Direction.S, Direction.W };
            case W:
                return new Direction[] { Direction.SW, Direction.NW };
            default:
                return new Direction[0];
        }
    }
}
