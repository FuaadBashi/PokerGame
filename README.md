# Poker Game

[![CI](https://github.com/FuaadBashi/PokerGame/actions/workflows/ci.yml/badge.svg)](https://github.com/FuaadBashi/PokerGame/actions/workflows/ci.yml)

A five-card draw poker round in Java: shuffle and cut the deck, deal to up to ten players, rank
every hand, pick the winner (or split the pot), and suggest which cards each player should draw.

```
---------------------------
4. ONE PAIR         [K♠, J♠, 9♥, 5♦, 5♠]                     Discards:[9♥]
2. HIGH CARD        [A♠, 9♠, 7♥, 3♥, 2♥]                     Discards:[2♥, 3♥, 7♥]
3. HIGH CARD        [10♥, 9♦, 7♦, 4♠, 2♦]                    Discards:[2♦, 4♠, 7♦]
1. HIGH CARD        [8♥, 7♣, 6♥, 4♥, 3♣]                     Discards:[3♣, 4♥, 6♥]
---------------------------
Player 4 wins with ONE PAIR
```

## Highlights

- **Complete hand evaluator.** Recognises all ten categories from high card to royal flush,
  including the A-2-3-4-5 "wheel" straight.
- **Correct tie-breaking.** `PokerHand` implements `Comparable`. Equal categories are separated by
  grouped ranks and then kickers, so a pair of kings beats a pair of queens and identical hands in
  different suits split the pot.
- **Draw strategy.** Made hands stand pat. Otherwise the matched cards are kept and up to three
  others are discarded, keeping high kickers.
- **Modern Java.** Records (`Card`), enums, streams, `Comparator` composition and switch
  expressions.
- **Deterministic tests.** The game accepts a seeded `Random`, so a whole round can be replayed.

## Getting started

Requires JDK 17+ and Maven.

```bash
git clone https://github.com/FuaadBashi/PokerGame.git
cd PokerGame
mvn package
java -jar target/poker-game.jar      # 8 players
java -jar target/poker-game.jar 4    # 2–10 players
```

`CardCollectionsDemo` is a separate walkthrough of `java.util.Collections` (shuffle, rotate,
binary search, frequency and so on) using the same `Card` type:

```bash
java -cp target/poker-game.jar dev.lpa.CardCollectionsDemo
```

## Project structure

```
src/main/java/dev/lpa/
├── Card.java                   record: suit, face, rank; standard deck factory
├── CardCollectionsDemo.java    Collections API walkthrough
└── games/
    ├── GameController.java     entry point
    └── poker/
        ├── PokerGame.java      shuffle, cut, deal, pick the winner
        ├── PokerHand.java      hand evaluation, comparison, discards
        └── Ranking.java        hand categories in strength order
```

## Tests

```bash
mvn verify
```

This runs the JUnit suite and checks formatting with google-java-format. The suite covers every
hand category, tie-breaks, the wheel, draw decisions, and a full seeded round.
