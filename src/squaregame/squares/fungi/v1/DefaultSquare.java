package squaregame.squares.fungi.v1;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;

/**
 * growth:
 * Propogate "generation" or "turn" (they are the same if it's all one type of square)
 * if a new "type" is made, then consider "generation", like if you wanna do something different for a while, then stop.
 *
 * consider growing to mean " moving "away" from others of your own.   "and attacking to move "towards"?)
 * propogate direction knowledge. (relative to "me")
 *
 * EndGame... if all but one near you(or last move failed?), 1/8 chance of moving in.(or pick an "always" direction to move from?... might not work)
 *
 * Colonize:
 * every so often shoot off a spore and start a new colony.
 */
public class DefaultSquare extends SquareLogic {

    @Override
    public SquareAction run(SquareView squareView) {
        return SquareAction.replicate(Direction.N, new Mycelium(Direction.N), new Mycelium(Direction.S));
    }

    /**
     * @return The name of the AI.
     */
    @Override
    public String getSquareName() {
        return "Fungi";
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
