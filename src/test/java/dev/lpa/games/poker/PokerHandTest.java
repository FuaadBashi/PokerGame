package dev.lpa.games.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.lpa.Card;
import dev.lpa.Card.Suit;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PokerHandTest {

    /** Parses "AH KS 10D 2C 2S": rank then suit initial. */
    static PokerHand hand(String cards) {
        List<Card> parsed =
                Arrays.stream(cards.split(" "))
                        .map(
                                code -> {
                                    String face = code.substring(0, code.length() - 1);
                                    Suit suit =
                                            switch (code.charAt(code.length() - 1)) {
                                                case 'C' -> Suit.CLUB;
                                                case 'D' -> Suit.DIAMOND;
                                                case 'H' -> Suit.HEART;
                                                case 'S' -> Suit.SPADE;
                                                default -> throw new IllegalArgumentException(code);
                                            };
                                    return Character.isDigit(face.charAt(0))
                                            ? Card.getNumericCard(suit, Integer.parseInt(face))
                                            : Card.getFaceCard(suit, face.charAt(0));
                                })
                        .toList();
        PokerHand hand = new PokerHand(1, parsed);
        hand.evalHand();
        return hand;
    }

    @ParameterizedTest(name = "{0} is {1}")
    @CsvSource({
        "AH KH QH JH 10H, ROYAL_FLUSH",
        "9C 8C 7C 6C 5C, STRAIGHT_FLUSH",
        "5D 4D 3D 2D AD, STRAIGHT_FLUSH",
        "7S 7H 7D 7C 2S, FOUR_OF_A_KIND",
        "QS QH QD 4C 4S, FULL_HOUSE",
        "KD 9D 6D 4D 2D, FLUSH",
        "10S 9H 8D 7C 6S, STRAIGHT",
        "5S 4H 3D 2C AS, STRAIGHT",
        "JS JH JD 8C 2S, THREE_OF_A_KIND",
        "9S 9H 4D 4C KS, TWO_PAIR",
        "AS AH 9D 6C 3S, ONE_PAIR",
        "AS QH 9D 6C 3S, HIGH_CARD",
        "KS AH 2D 3C 4S, HIGH_CARD",
    })
    void eachHandIsPlacedInTheRightCategory(String cards, Ranking expected) {
        assertEquals(expected, hand(cards).getScore());
    }

    @Test
    void aHigherCategoryBeatsALowerOneRegardlessOfCardRanks() {
        assertTrue(hand("2S 3S 4S 5S 7S").compareTo(hand("AS AH AD KC KS")) < 0);
    }

    @Test
    void aHigherPairBeatsALowerPair() {
        assertTrue(hand("KS KH 4D 3C 2S").compareTo(hand("QS QH AD JC 9S")) > 0);
    }

    @Test
    void equalPairsAreSeparatedByTheirKickers() {
        assertTrue(hand("8S 8H AD 4C 2S").compareTo(hand("8D 8C KD QC JS")) > 0);
    }

    @Test
    void theWheelIsTheLowestStraight() {
        assertTrue(hand("5S 4H 3D 2C AS").compareTo(hand("6S 5H 4D 3C 2S")) < 0);
    }

    @Test
    void identicalRanksInDifferentSuitsTie() {
        assertEquals(0, hand("AS KH 9D 6C 3S").compareTo(hand("AD KC 9H 6S 3D")));
    }

    @Test
    void aMadeHandStandsPatAndAPairDrawsThree() {
        assertTrue(hand("10S 9H 8D 7C 6S").getDiscards().isEmpty());
        assertEquals(3, hand("8S 8H 6D 4C 2S").getDiscards().size());
    }
}
