package dev.lpa.games.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PokerGameTest {

    @Test
    void aRoundDealsFiveCardsToEachPlayerAndKeepsTheRestInTheDeck() {
        PokerGame game = new PokerGame(6, new Random(42));

        game.play();

        assertEquals(6, game.getHands().size());
        assertEquals(52 - 6 * 5, game.getRemainingCards().size());
    }

    @Test
    void theWinnerHoldsTheStrongestHandAtTheTable() {
        PokerGame game = new PokerGame(8, new Random(7));

        List<PokerHand> winners = game.play();

        assertFalse(winners.isEmpty());
        for (PokerHand hand : game.getHands()) {
            assertTrue(winners.get(0).compareTo(hand) >= 0);
        }
    }

    @Test
    void aTableNeedsBetweenTwoAndTenPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new PokerGame(1));
        assertThrows(IllegalArgumentException.class, () -> new PokerGame(11));
    }
}
