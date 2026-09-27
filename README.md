# Poker and Card Collections — Java

A Java card-modeling project with deck operations and poker-hand components. The default entry point demonstrates collection operations on cards.

## Run locally

Use JDK 17 or later. From a POSIX shell:

```bash
git clone https://github.com/FuaadBashi/PokerGame.git
cd PokerGame
mkdir -p out
find . -name '*.java' > sources.txt
javac -d out @sources.txt
java -cp out dev.lpa.Main
```

## Code to explore

- [Card.java](Card.java): card representation and deck helpers.
- [Main.java](Main.java): shuffling, sorting, searching, rotating, and comparing card collections.
- [GameController.java](Game/GameController.java): game coordination.
- [PokerHand.java](Game/Poker/PokerHand.java) and [Ranking.java](Game/Poker/Ranking.java): hand evaluation components.

The command above runs the collection demonstration. The presence of poker classes should not be read as a claim that the default executable launches a complete interactive poker game.
