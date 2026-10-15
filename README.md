# RaffleNight

CS 2114 Project 2 (Group 54). A text-menu raffle: sell tickets, add prizes,
draw random winners, undo the last draw, refund tickets, check odds, and
save/load to a text file.

## Classes (`src/raffle`)

- `LinkedChain<T>` – generic linked bag (adds at the front) with `getEntryAt` and `removeIf`
- `Raffle` – the raffle rules; throws on any invalid action and leaves state unchanged
- `RaffleMenu` – keyboard menu and `main`; re-prompts on bad input
- `Entrant`, `Ticket`, `Prize`, `Winner` – data classes

## Running

From the project folder:

```
javac -d bin src/raffle/*.java
java -cp bin raffle.RaffleMenu
```
