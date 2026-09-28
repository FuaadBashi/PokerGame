package dev.lpa.games.poker;

import dev.lpa.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A five-card hand that knows its {@link Ranking} and can be compared against other hands.
 *
 * <p>Hands compare first by category, then by tie-break ranks: grouped cards before kickers, each
 * group highest first. So a pair of kings beats a pair of queens, and two equal pairs are separated
 * by their kickers.
 */
public class PokerHand implements Comparable<PokerHand> {

    private static final int ACE = 12;
    private static final int FIVE = 3;
    private static final int JACK = 9;

    private final List<Card> hand;
    private final List<Card> keepers;
    private final List<Card> discards;
    private final int playerNo;
    private Ranking score = Ranking.HIGH_CARD;
    private List<Integer> tieBreakers = List.of();

    public PokerHand(int playerNo, List<Card> hand) {
        if (hand.size() != 5) {
            throw new IllegalArgumentException("A poker hand has 5 cards, got " + hand.size());
        }
        this.hand = new ArrayList<>(hand);
        this.hand.sort(Card.sortRankReversedSuit());
        this.playerNo = playerNo;
        keepers = new ArrayList<>(hand.size());
        discards = new ArrayList<>(hand.size());
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public Ranking getScore() {
        return score;
    }

    public List<Card> getDiscards() {
        return Collections.unmodifiableList(discards);
    }

    @Override
    public String toString() {
        return "%d. %-16s %-40s %s"
                .formatted(playerNo, score, hand, discards.isEmpty() ? "" : "Discards:" + discards);
    }

    public void evalHand() {
        keepers.clear();
        discards.clear();

        // Rank -> how many cards of that rank, highest rank first.
        Map<Integer, Integer> counts = new TreeMap<>(Comparator.reverseOrder());
        hand.forEach(card -> counts.merge(card.rank(), 1, Integer::sum));

        List<Integer> groupedRanks = new ArrayList<>(counts.keySet());
        groupedRanks.sort(
                Comparator.comparing((Integer rank) -> counts.get(rank))
                        .reversed()
                        .thenComparing(Comparator.reverseOrder()));
        List<Integer> groupSizes = groupedRanks.stream().map(counts::get).toList();

        boolean flush = hand.stream().map(Card::suit).distinct().count() == 1;
        int straightHigh = straightHighRank(groupedRanks);
        boolean straight = straightHigh >= 0;

        if (straight && flush) {
            score = straightHigh == ACE ? Ranking.ROYAL_FLUSH : Ranking.STRAIGHT_FLUSH;
        } else if (groupSizes.get(0) == 4) {
            score = Ranking.FOUR_OF_A_KIND;
        } else if (groupSizes.equals(List.of(3, 2))) {
            score = Ranking.FULL_HOUSE;
        } else if (flush) {
            score = Ranking.FLUSH;
        } else if (straight) {
            score = Ranking.STRAIGHT;
        } else if (groupSizes.get(0) == 3) {
            score = Ranking.THREE_OF_A_KIND;
        } else if (groupSizes.equals(List.of(2, 2, 1))) {
            score = Ranking.TWO_PAIR;
        } else if (groupSizes.get(0) == 2) {
            score = Ranking.ONE_PAIR;
        } else {
            score = Ranking.HIGH_CARD;
        }

        tieBreakers = straight ? List.of(straightHigh) : groupedRanks;

        pickDiscards(counts);
    }

    /** Returns the straight's top rank, or -1. The wheel (A-2-3-4-5) counts as five-high. */
    private static int straightHighRank(List<Integer> distinctRanksHighFirst) {
        if (distinctRanksHighFirst.size() != 5) {
            return -1;
        }
        int high = distinctRanksHighFirst.get(0);
        int low = distinctRanksHighFirst.get(4);
        if (high - low == 4) {
            return high;
        }
        if (high == ACE && distinctRanksHighFirst.get(1) == FIVE) {
            return FIVE;
        }
        return -1;
    }

    /**
     * Five-card-draw strategy: made hands (straight or better) stand pat. Otherwise keep the
     * matched cards and discard up to three of the rest, keeping a high kicker when holding only a
     * pair or nothing.
     */
    private void pickDiscards(Map<Integer, Integer> counts) {
        if (score.compareTo(Ranking.STRAIGHT) >= 0 && score != Ranking.FOUR_OF_A_KIND) {
            keepers.addAll(hand);
            return;
        }

        List<Card> unmatched = new ArrayList<>();
        for (Card card : hand) {
            (counts.get(card.rank()) > 1 ? keepers : unmatched).add(card);
        }

        int rankedCards = keepers.size();
        Collections.reverse(unmatched);
        int index = 0;
        for (Card c : unmatched) {
            if (index++ < 3 && (rankedCards > 2 || c.rank() < JACK)) {
                discards.add(c);
            } else {
                keepers.add(c);
            }
        }
    }

    @Override
    public int compareTo(PokerHand other) {
        int byCategory = score.compareTo(other.score);
        if (byCategory != 0) {
            return byCategory;
        }
        for (int i = 0; i < Math.min(tieBreakers.size(), other.tieBreakers.size()); i++) {
            int byRank = Integer.compare(tieBreakers.get(i), other.tieBreakers.get(i));
            if (byRank != 0) {
                return byRank;
            }
        }
        return 0;
    }
}
