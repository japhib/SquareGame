package squaregame.squares.spiderweb;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;

public abstract class SpiderBase extends SquareLogic {
    private static final int ANGRY = 100;

    protected int generationsOld;

    private int anger = 0;
    private int randomMoves = 0;
    private Direction lastEnemyDirection;

    public SpiderBase(int generationsOld) {
        this.generationsOld = generationsOld;
    }

    @Override
    public SquareAction run(SquareView view) {
        var enemyDirs = view.getEnemyDirections();
        if (enemyDirs.size() > 0) {

            // !!!
            anger = ANGRY;

            // attack a random direction enemy
            var randomEnemyDirection = enemyDirs.get(SpiderwebUtils.random.nextInt(enemyDirs.size()));
            lastEnemyDirection = randomEnemyDirection;
            return attack(randomEnemyDirection);
        }

        if (anger > 0) {
            anger--;
            var empty = view.getEmptyDirections();
            if (empty.contains(lastEnemyDirection)) {
                return move(lastEnemyDirection);
            }

            for (var adj : SpiderwebUtils.adjacentDirs(lastEnemyDirection)) {
                if (empty.contains(adj)) {
                    return move(adj);
                }
            }
        }

        if (generationsOld >= 8) {
            return randomMove(view);
        }

        return doRun(view);
    }

    protected SquareAction randomMove(SquareView view) {
        randomMoves++;
        if (randomMoves >= 100) {
            generationsOld = 0;
            return run(view);
        }

        var empty = view.getEmptyDirections();

        if (empty.isEmpty())
            return Wait();

        return move(empty.get(SpiderwebUtils.random.nextInt(empty.size())));
    }

    @Override
    public String getSquareName() {
        return "Spiderweb";
    }

    @Override
    public Color getColor() {
        return new Color(200, 30, 255);
    }

    public abstract SquareAction doRun(SquareView view);
}
