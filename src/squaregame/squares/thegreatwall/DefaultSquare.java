package squaregame.squares.thegreatwall;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public class DefaultSquare extends SquareLogic {

    public static Direction startingDirection;

    @Override
    public SquareAction run(SquareView squareView) {
        final Random random = new Random();
        startingDirection = Direction.values()[random.nextInt(8)];
        final Optional<Direction> direction = squareView.getEnemyDirections().stream().findAny();
        return direction.map(this::attack)
                .orElseGet(() -> SquareAction.replicate(startingDirection,
                        new WallBuilder(1, startingDirection.getOppositeDirection(), false, squareView.getPlayerAllowedMetadata().getBoardSize()),
                        new WallBuilder(1, startingDirection, false, squareView.getPlayerAllowedMetadata().getBoardSize())));
    }

    @Override
    public String getSquareName() {
        return "The Great Wall";
    }

    @Override
    public Color getColor() {
        return new Color(255, 0, 0);
    }
}
