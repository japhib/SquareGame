package squaregame.squares.bunny;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;
import java.util.List;

/**
 * Created by Russell on 5/5/18.
 */
public class DefaultSquare extends SquareLogic {

    @Override
    public SquareAction run(SquareView squareView) {
        final List<Direction> directions = squareView.getEmptyDirections();
        if (directions.isEmpty()) {
            return SquareAction.wait(this);
        }
        return SquareAction.replicate(directions.get(0), this, new DefaultSquare());
    }

    @Override
    public String getSquareName() {
        return "Make like bunnies";
    }

    @Override
    public Color getColor() {
        // light blue
        return new Color(0, 229, 255);
    }
}
