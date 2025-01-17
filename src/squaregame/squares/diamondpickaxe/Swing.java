package squaregame.squares.diamondpickaxe;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;
import java.util.Optional;

public class Swing extends SquareLogic {

    private int turn = 0;

    @Override
    public SquareAction run(SquareView squareView) {
        final Optional<Direction> direction = squareView.getEnemyDirections().stream().findAny();
        if (direction.isPresent()) {
            return SquareAction.attack(direction.get(), this);
        }
        final Direction moveDirection = DefaultSquare.startingDirection.rotateClockwise(Math.floorMod(turn-- / 250, 8) + 1);
        if (squareView.getLocation(moveDirection) == null) {
            return SquareAction.move(moveDirection, this); 
        } else {
            return SquareAction.wait(this);
        }
    }

    @Override
    public String getSquareName() {
        return null;
    }

    @Override
    public Color getColor() {
        // blue
        return new Color(0, 47, 255);
    }
}
