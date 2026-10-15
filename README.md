# RaffleNight

CS 2114 Project 2 (Group 54). A text-menu raffle: sell tickets, add prizes,
draw random winners, undo the last draw, refund tickets, check odds, and
save/load to a text file.

## Files

All source files are in `src/raffle`:

- `LinkedChain.java` – generic linked bag (adds at the front) with `getEntryAt` and `removeIf`
- `Raffle.java` – the raffle rules; throws on any invalid action and leaves state unchanged
- `RaffleMenu.java` – keyboard menu; contains `main`
- `Entrant.java`, `Ticket.java`, `Prize.java`, `Winner.java` – data classes

The program uses only the standard Java library.

## Requirements

Java 8 or newer (`javac` and `java` on your PATH).

## Compile

From the `RaffleNight` folder (the one containing this README):

```
javac -d bin src/raffle/*.java
```

This puts the compiled classes in `bin/`.

## Run

From the same folder:

```
java -cp bin raffle.RaffleMenu
```

The program first asks for the ticket price in cents (for example `200` for
$2.00), then shows the menu:

```
1. Sell tickets
2. Add prize
3. Draw winner
4. Undo last draw
5. Refund tickets
6. Check a person's tickets and odds
7. Show status
8. Save
9. Load
0. Quit
```

Type a number and press Enter. Invalid input prints a message and returns to
the menu. Enter `0` to quit.
