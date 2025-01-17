package squaregame.model;

import org.reflections.Reflections;
import squaregame.squares.SquareLogic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created by Russell on 5/5/18.
 */
public class GameState {
    private int roundNumber;
    private int totalRounds = 3000;

    private static int MAX_PLAYERS = 8;

    private List<Player> playerList;
    private Map<Player, Set<Player>> whoPlayersBeat;
    private List<AIOption> aiOptions;
    private boolean freeForAll;
    private Leaderboard freeForAllLeaderboard;
    private Leaderboard leaderboard;

    private Map<Player, Score> scoreBoard;

    public GameState() {
        roundNumber = 0;
        final Reflections reflections = new Reflections("squaregame.squares");
        final Set<Class<? extends SquareLogic>> classes = reflections.getSubTypesOf(SquareLogic.class);
        aiOptions = new ArrayList<>();
        aiOptions.add(null);
        final AtomicInteger aiId = new AtomicInteger(0);
        classes.forEach(c -> {
            if (c.getSimpleName().equals("DefaultSquare")) {
                aiOptions.add(new AIOption((Class<SquareLogic>) c, aiId.getAndIncrement()));
            }
        });
        this.whoPlayersBeat = new HashMap<>();
        this.leaderboard = new Leaderboard(aiOptions, "LEADERBOARD");
        this.freeForAllLeaderboard = new Leaderboard(aiOptions, "FREEFORALL");
        this.scoreBoard = new HashMap<>();
        playerList = new ArrayList<>();
        IntStream.rangeClosed(1, MAX_PLAYERS).forEach((i) -> playerList.add(new Player(Color.WHITE, aiOptions.get(0))));
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public boolean someoneWon() {
        return playerList.stream().filter(p -> scoreBoard.get(p).getScore() > 0).count() < 2;
    }

    public void finalRank() {
        rankNewDeadPlayers();
        final Comparator<Player> comp = Comparator.comparingInt(p1 -> this.scoreBoard.get(p1).getScore());
        playerList.stream()
                .filter(Player::isPlaying)
                .filter(p -> this.scoreBoard.get(p).getScore() != 0)
                .sorted(comp)
                .forEach(loser -> this.whoPlayersBeat.putIfAbsent(loser, new HashSet<>(this.whoPlayersBeat.keySet())));
        if (this.isFreeForAll()) {
            this.whoPlayersBeat.forEach((winner, value) -> value.forEach(loser -> {
                final int K = 100;
                final int winnerMmr = this.freeForAllLeaderboard.getScore(winner.getAiOption().getId());
                final int loserMmr = this.freeForAllLeaderboard.getScore(loser.getAiOption().getId());
                final double gameMmrValue = (1 - ((double)winnerMmr / (winnerMmr + loserMmr))) * K;
                this.freeForAllLeaderboard.addScore(winner.getAiOption().getId(), (int)gameMmrValue);
                this.freeForAllLeaderboard.addScore(loser.getAiOption().getId(), -(int)gameMmrValue);
            }));
        } else {
            this.whoPlayersBeat.forEach((winner, value) -> value.forEach(loser -> {
                final int K = 50;
                final int winnerMmr = this.leaderboard.getScore(winner.getAiOption().getId());
                final int loserMmr = this.leaderboard.getScore(loser.getAiOption().getId());
                final double gameMmrValue = (1 - ((double)winnerMmr / (winnerMmr + loserMmr))) * K;
                System.out.println("GameMMR Value" + gameMmrValue);
                this.leaderboard.addScore(winner.getAiOption().getId(), (int)gameMmrValue);
                this.leaderboard.addScore(loser.getAiOption().getId(), -(int)gameMmrValue);
            }));
        }
    }

    public void rankNewDeadPlayers() {
        playerList.stream()
                .filter(Player::isPlaying)
                .filter(p -> this.scoreBoard.get(p).getScore() == 0)
                .forEach(loser -> this.whoPlayersBeat.putIfAbsent(loser, new HashSet<>(this.whoPlayersBeat.keySet())));
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void reset() {
        roundNumber = 0;
        this.whoPlayersBeat = new HashMap<>();
        getPlayerList().forEach(p -> scoreBoard.put(p, new Score()));
    }

    public void nextRound() {
        this.roundNumber++;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public List<AIOption> getAiOptions() {
        return aiOptions;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public boolean gameOver() {
        return totalRounds < this.roundNumber || this.someoneWon();
    }

    public boolean isFreeForAll() {
        return this.freeForAll;
    }

    public void setIsFreeForAll(boolean freeForAll) {
        this.freeForAll = freeForAll;
    }

    public Leaderboard getFreeForAllLeaderboard() {
        return freeForAllLeaderboard;
    }

    public Map<Player, Score> getScoreBoard() {
        return scoreBoard;
    }
}
