package dev.lpa.games;

import dev.lpa.games.poker.PokerGame;

public class GameController {

    private static final int DEFAULT_PLAYERS = 8;

    public static void main(String[] args) {
        int players = DEFAULT_PLAYERS;
        if (args.length > 0) {
            try {
                players = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage: java -jar poker-game.jar [players 2-10]");
                System.exit(2);
            }
        }

        try {
            new PokerGame(players).startPlay();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }
}
