package dev.lpa.games.poker;

import dev.lpa.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/** Deals a round of five-card draw, ranks every hand and reports the winner. */
public class PokerGame {

    public static final int CARDS_IN_HAND = 5;
    public static final int MAX_PLAYERS = 52 / CARDS_IN_HAND;

    private final List<Card> deck = Card.getStandardDeck();
    private final int playerCount;
    private final Random random;
    private final List<PokerHand> pokerHands;
    private List<Card> remainingCards = List.of();

    public PokerGame(int playerCount) {
        this(playerCount, new Random());
    }

    /** Takes the random source so a round can be replayed exactly in tests. */
    PokerGame(int playerCount, Random random) {
        if (playerCount < 2 || playerCount > MAX_PLAYERS) {
            throw new IllegalArgumentException(
                    "Player count must be between 2 and " + MAX_PLAYERS + ", got " + playerCount);
        }
        this.playerCount = playerCount;
        this.random = random;
        pokerHands = new ArrayList<>(playerCount);
    }

    public List<PokerHand> getHands() {
        return Collections.unmodifiableList(pokerHands);
    }

    public List<Card> getRemainingCards() {
        return remainingCards;
    }

    /** Deals and evaluates one round. Returns the winning hands; more than one is a split pot. */
    public List<PokerHand> play() {
        Collections.shuffle(deck, random);
        // Cut the deck, as a dealer would.
        Collections.rotate(deck, random.nextInt(15, 35));

        deal();
        pokerHands.forEach(PokerHand::evalHand);

        int cardsDealt = playerCount * CARDS_IN_HAND;
        remainingCards = List.copyOf(deck.subList(cardsDealt, deck.size()));

        PokerHand best = Collections.max(pokerHands);
        return pokerHands.stream().filter(hand -> hand.compareTo(best) == 0).toList();
    }

    public void startPlay() {
        List<PokerHand> winners = play();

        System.out.println("---------------------------");
        pokerHands.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
        System.out.println("---------------------------");

        if (winners.size() == 1) {
            PokerHand winner = winners.get(0);
            System.out.printf("Player %d wins with %s%n", winner.getPlayerNo(), winner.getScore());
        } else {
            System.out.printf(
                    "Split pot between players %s with %s%n",
                    winners.stream().map(PokerHand::getPlayerNo).toList(),
                    winners.get(0).getScore());
        }

        Card.printDeck(remainingCards, "Remaining Cards", 2);
    }

    private void deal() {
        Card[][] hands = new Card[playerCount][CARDS_IN_HAND];
        // One card to each player in turn, as at a real table.
        for (int deckIndex = 0, i = 0; i < CARDS_IN_HAND; i++) {
            for (int j = 0; j < playerCount; j++) {
                hands[j][i] = deck.get(deckIndex++);
            }
        }

        int playerNo = 1;
        for (Card[] hand : hands) {
            pokerHands.add(new PokerHand(playerNo++, List.of(hand)));
        }
    }
}
