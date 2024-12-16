# PokerGame
# Poker Game Application

This repository contains a Java-based Poker game implementation. The project includes features for shuffling, dealing cards, and evaluating hands for multiple players in a Five-Card Draw poker game. The structure is modular, with components for deck management, game control, and hand evaluation.

## Features

1. **Deck Management**:
   - Generate a standard 52-card deck.
   - Shuffle, sort, rotate, and manipulate the deck.

2. **Game Setup**:
   - Configure player count and number of cards per hand.
   - Deal cards to players.

3. **Poker Hand Evaluation**:
   - Analyze hands to determine rankings such as pairs, three of a kind, full house, etc.
   - Identify the best and worst cards in a hand.

## Code Structure

### `Main.java`
- Entry point for the application.
- Demonstrates various deck operations such as shuffling, sorting, reversing, and rotating.
- Includes utility methods for printing and manipulating decks.

### `GameController.java`
- Initializes and starts a new Poker game.
- Uses the `PokerGame` class to manage gameplay.

### `PokerGame.java`
- Manages the core gameplay logic, including:
  - Shuffling and rotating the deck.
  - Dealing cards to players.
  - Evaluating hands using the `PokerHand` class.
- Displays remaining cards after dealing.

### `PokerHand.java`
- Represents a player's hand in the game.
- Evaluates the hand to assign rankings based on poker rules.
- Manages "keepers" (valuable cards) and "discards" (less valuable cards).
- Provides a detailed string representation of the hand.

## Prerequisites
- Java Development Kit (JDK) version 11 or higher.

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/poker-game
   ```

2. Navigate to the project directory:
   ```bash
   cd poker-game
   ```

3. Compile the Java files:
   ```bash
   javac dev/lpa/**/*.java
   ```

4. Run the application:
   ```bash
   java dev.lpa.games.GameController
   ```

## Example Output
- Displays shuffled decks, dealt hands, and hand evaluations with rankings.
- Outputs remaining cards in the deck.

## Future Enhancements
- Add support for more poker variants.
- Enhance UI with a graphical interface.
- Implement multiplayer support over a network.


Enjoy playing Poker and exploring the code!

