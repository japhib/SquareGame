package squaregame.squares.thegreatwall;

import squaregame.model.Direction;
import squaregame.model.MagicSquare;
import squaregame.model.Player;
import squaregame.model.SquareAction;
import squaregame.squares.SquareLogic;
import squaregame.utils.SquareLogicUtilities;

import java.util.List;
import java.util.Optional;

public class WallBuilder extends SquareLogic {

    private final boolean directionChange;
    private int generation;
    private double movesLeft;
    private Direction moveDirection;
    private int endSize = 128;

    public WallBuilder(int generation, Direction moveDirection, boolean directionChange) {
        this.generation = generation;
        this.movesLeft = endSize / (Math.pow(2, generation + 1)) - 1;
        this.moveDirection = moveDirection;
        this.directionChange = directionChange;
    }

    @Override
    public SquareAction run(MagicSquare magicSquare, int row, int col, List<Player> view) {
        final Optional<Direction> direction = SquareLogicUtilities.getEnemyDirections(view, magicSquare.getPlayer()).stream().findAny();
        if (direction.isPresent()) {
            return SquareAction.attack(direction.get(), this);
        }
        if (endSize / (Math.pow(2, generation + 1)) - 1 < 0) {
            if (this.directionChange) {
                return SquareAction.wait(new SearchAndDestroy());
            }
            return SquareAction.replicate(getMovingDirection(),
                    new WallBuilder(1, SquareLogicUtilities.getOppositeDirection(getMovingDirection()), true),
                    new WallBuilder(1, getMovingDirection(), true));
        }
        if (view.get(this.moveDirection.ordinal()) == null) {
            if (movesLeft > 0) {
                movesLeft--;
                return SquareAction.move(this.moveDirection, this);
            } else {
                return SquareAction.replicate(this.moveDirection,
                        new WallBuilder(generation + 1, SquareLogicUtilities.getOppositeDirection(this.moveDirection), this.directionChange),
                        new WallBuilder(generation + 1, this.moveDirection, this.directionChange));
            }
        } else {
            return SquareAction.wait(this);
        }
    }

    private Direction getMovingDirection() {
        return SquareLogicUtilities.getRotatedDirection(DefaultSquare.startingDirection, 2);
    }

    @Override
    public String getSquareName() {
        return null;
    }
}