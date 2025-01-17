package squaregame.squares.fungi.v1;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import squaregame.squares.fungi.DirectionUtils;

/**
 * Mycelium.
 */
public class Mycelium extends SquareLogic {

    private int attackCountdown = 0;
    private int attackCountdownMax = 100;

    private Direction from;
    private int COLONIZATION_RATE = 3;
    private int sporeCountdownMax = 50;

    private static int FORK_RATE = 4;

    /**
     * Constructor.
     * @param from the direction this came from.
     */
    public Mycelium(Direction from){
        this.from = from;
    }

    /**
     * Constructor.
     * @param from the direction this came from.
     * @param sporeCountdownMax optional override
     */
    public Mycelium(Direction from, int sporeCountdownMax){
        this.from = from;
        this.sporeCountdownMax = sporeCountdownMax;
    }

    @Override
    public SquareAction run(SquareView squareView) {

        //if there's a bad guy, attack one most away from "from"
        List<Direction> enemies = squareView.getEnemyDirections();
        if(!enemies.isEmpty()){
            //TODO attack the farthest from "from"
            return SquareAction.attack(enemies.get(0), this);
        }

        List<Direction> emptySquares = squareView.getEmptyDirections();
        //if there's only good guys, wait
        if(emptySquares.isEmpty()){
            attackCountdown = attackCountdownMax;
            return SquareAction.wait(this);
        }

        //if there's only one square and it's the from... fork.
        final List<Direction> friends = squareView.getFriendlyDirections();
        if(friends.size() == 1 && friends.contains(from)){

            //to fork or not to fork
            if(ThreadLocalRandom.current().nextInt(0, FORK_RATE) == 0){
                Direction to = DirectionUtils.fork(from, null);
                return SquareAction.replicate(to, this, new Mycelium(DirectionUtils.opposite(to)));
            }
            //myceleum running
            else {
                return SquareAction.replicate(DirectionUtils.opposite(from), this, new Mycelium(from));
            }
        }//if there's 2 and one is the from, and the other is in a fork position, fork.
        else if(friends.size() == 2 && friends.contains(from)){
                if(friends.contains(DirectionUtils.fork(from, null))) {
                    Direction to = DirectionUtils.fork(from, DirectionUtils.fork(from, null));
                    return SquareAction.replicate(to, this, new Mycelium(DirectionUtils.opposite(to)));
                }//if we've bumpped into someone
                else if(friends.contains(DirectionUtils.opposite(from))){
                    Direction to = preferredOrRandom(squareView.getEmptyDirections(), DirectionUtils.opposite(from));
                    return SquareAction.replicate(to, this, new Mycelium(DirectionUtils.opposite(to)));
                }//just keep moving along
                else {
                   return SquareAction.replicate(DirectionUtils.opposite(from), this, new Mycelium(from));
                }
        }

        //a square just opened nearby, attack it.
        if(emptySquares.size() > 0 && attackCountdown > 0){
            attackCountdown--;
            Direction attackDirection = preferredOrRandom(emptySquares, DirectionUtils.opposite(from));
            from = DirectionUtils.opposite(attackDirection);//start looking at the attack direction.
            return SquareAction.attack(attackDirection, this);
        }

        //Starting to get saturated
        if(emptySquares.size() == 3) {
            //to colonize or not to colonize
            if (ThreadLocalRandom.current().nextInt(0, COLONIZATION_RATE) == 0) {
                Direction to = emptySquares.get(ThreadLocalRandom.current().nextInt(0, emptySquares.size()));
                return SquareAction.move(to, new Spore(to, sporeCountdownMax));
            }
        }

        //In saturated state, back off
        if(emptySquares.size() <= 5){
            if(ThreadLocalRandom.current().nextInt(0, 8) == 0){
                return SquareAction.replicate(emptySquares.get(0), this, new Mycelium(DirectionUtils.opposite(emptySquares.get(0))));
            }
            return SquareAction.wait(this);
        }

        Direction to = preferredOrRandom(squareView.getEmptyDirections(), DirectionUtils.opposite(from));
        return SquareAction.replicate(to, this, new Mycelium(DirectionUtils.opposite(to)));

    }

    @Override
    public String getSquareName() {
        return "Mycelium";
    }

    private Direction preferredOrRandom(List<Direction> emptySquares, Direction preferred){
        if(emptySquares.contains(preferred)){
            return preferred;
        }
        return emptySquares.get(ThreadLocalRandom.current().nextInt(0, emptySquares.size()));
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
