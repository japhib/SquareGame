package squaregame.squares.spiderweb;

import squaregame.model.SquareAction;
import squaregame.model.SquareView;

public class DefaultSquare extends SpiderBase {
    public DefaultSquare() {
        super(0);
    }

    @Override
    public SquareAction doRun(SquareView view) {
//        int boardSize = view.getPlayerAllowedMetadata().getBoardSize();
//        EggSac.TRAVEL_DISTANCE_BASE = boardSize / 5;
//        EggSac.TRAVEL_DISTANCE_VARIANCE = boardSize / 5;

        return new MotherSpider().run(view);
    }
}