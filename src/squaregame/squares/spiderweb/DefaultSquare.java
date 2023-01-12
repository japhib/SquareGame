package squaregame.squares.spiderweb;

import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.util.Random;

public class DefaultSquare extends SquareLogic {
    public static Random random = new Random();

    @Override
    public SquareAction run(SquareView view) {
        var empty = view.getEmptyDirections();
        if (empty.isEmpty()) {
            return Wait();
        }

        var dir = empty.get(random.nextInt(empty.size()));
        return SquareAction.replicate(
            dir,
            new SpiderWeb(SpiderWeb.START_RADIUS, dir.getOppositeDirection()),
            new SpiderWeb(SpiderWeb.START_RADIUS, dir)
        );
    }

    @Override
    public String getSquareName() {
        return "Spiderweb";
    }
}