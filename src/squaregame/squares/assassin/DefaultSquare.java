package squaregame.squares.assassin;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DefaultSquare extends SquareLogic {

    private int aliveTurn = 0;

    private static final Set<Direction> VALID_DIRECTIONS = new HashSet<>(Arrays.asList(Direction.W, Direction.S, Direction.SW));

    @Override
    public SquareAction run(SquareView squareView) {

        final Optional<Direction> direction =  squareView.getEmptyDirections().stream().filter(VALID_DIRECTIONS::contains).findAny();

        return direction.map(d -> SquareAction.replicate(d, this, new AssassinSquare(Direction.values()[aliveTurn++ % 8])))
                .orElseGet(() -> SquareAction.wait(this));
    }

    @Override
    public String getSquareName() {
        return "Assassin Factory";
    }

    @Override
    public Color getColor() {
        // grey
        return new Color(150, 150, 150);
    }
}
