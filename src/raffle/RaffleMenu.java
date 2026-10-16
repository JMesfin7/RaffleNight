package raffle;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * Text menu for running a raffle. Handles all keyboard input and re-prompts
 * on badly shaped input; the Raffle enforces the rules.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class RaffleMenu {

    private Raffle raffle;
    private Scanner input;

    /**
     * Creates a menu for a raffle.
     *
     * @param raffle
     *            the raffle to manage
     * @param input
     *            where user input is read from
     */
    public RaffleMenu(Raffle raffle, Scanner input) {
        this.raffle = raffle;
        this.input = input;
    }


    /**
     * Reads the ticket price, builds the raffle, and runs the menu.
     *
     * @param args
     *            not used
     */
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to RaffleNight!");
        Raffle raffle = null;
        while (raffle == null) {
            System.out.print("Ticket price in cents (e.g. 200 for $2.00): ");
            if (!input.hasNextLine()) {
                return;
            }
            String line = input.nextLine().trim();
            try {
                raffle = new Raffle(Integer.parseInt(line), new Random());
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        new RaffleMenu(raffle, input).run();
    }


    /**
     * Shows the menu and carries out choices until the user quits or input
     * runs out.
     */
    public void run() {
        try {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt("Choice: ");
                try {
                    running = handle(choice);
                }
                catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        catch (NoSuchElementException e) {
            // Input ran out; end the same way Quit does.
        }
        System.out.println("Goodbye! Total raised: " + formatMoney(raffle
            .getTotalRaisedCents()));
    }


    /**
     * Carries out one menu choice.
     *
     * @return false if the user chose Quit
     */
    private boolean handle(int choice) {
        switch (choice) {
            case 1:
                String buyer = readName("Buyer name: ");
                int count = readInt("How many tickets: ");
                raffle.sellTickets(buyer, count);
                System.out.println("Sold " + count + " ticket(s) to " + buyer
                    .trim() + ". Total raised: " + formatMoney(raffle
                        .getTotalRaisedCents()));
                break;
            case 2:
                raffle.addPrize(readName("Prize name: "));
                System.out.println("Prize added. Prizes remaining: " + raffle
                    .getRemainingPrizeCount());
                break;
            case 3:
                System.out.println("Winner: " + raffle.drawWinner());
                break;
            case 4:
                System.out.println("Undid draw: " + raffle.undoLastDraw());
                break;
            case 5:
                String name = readName("Name to refund: ");
                int refunds = readInt("How many tickets: ");
                raffle.refund(name, refunds);
                System.out.println("Refunded " + formatMoney(refunds * raffle
                    .getTicketPriceCents()) + ". Total raised: " + formatMoney(
                        raffle.getTotalRaisedCents()));
                break;
            case 6:
                String who = readName("Name: ");
                System.out.printf("%s has %d ticket(s), %.1f%% chance next "
                    + "draw.%n", who.trim(), raffle.getTicketCount(who), raffle
                        .getOdds(who) * 100);
                break;
            case 7:
                printStatus();
                break;
            case 8:
                String saveFile = readName("File name: ");
                raffle.save(saveFile.trim());
                System.out.println("Saved to " + saveFile.trim() + ".");
                break;
            case 9:
                String loadFile = readName("File name: ");
                raffle.load(loadFile.trim());
                System.out.println("Loaded " + loadFile.trim() + ".");
                break;
            case 0:
                return false;
            default:
                System.out.println("Please choose a number from the menu.");
        }
        return true;
    }


    /**
     * Prints the numbered options.
     */
    private void printMenu() {
        System.out.println();
        System.out.println("=== RaffleNight ===");
        System.out.println("1. Sell tickets");
        System.out.println("2. Add prize");
        System.out.println("3. Draw winner");
        System.out.println("4. Undo last draw");
        System.out.println("5. Refund tickets");
        System.out.println("6. Check a person's tickets and odds");
        System.out.println("7. Show status");
        System.out.println("8. Save");
        System.out.println("9. Load");
        System.out.println("0. Quit");
    }


    /**
     * Prints the tickets, prizes, winners, and money raised.
     */
    private void printStatus() {
        System.out.println("Ticket price: " + formatMoney(raffle
            .getTicketPriceCents()) + "   Total raised: " + formatMoney(
                raffle.getTotalRaisedCents()));
        printList("Tickets", raffle.getTickets());
        printList("Prizes (next first)", raffle.getPrizes());
        printList("Winners (latest first)", raffle.getWinners());
    }


    /**
     * Prints a heading with a count, then one item per line.
     */
    private void printList(String heading, Object[] items) {
        System.out.println(heading + " (" + items.length + "):");
        for (Object item : items) {
            System.out.println("  " + item);
        }
    }


    /**
     * Prompts until a whole number is typed.
     *
     * @throws NoSuchElementException
     *             if input runs out
     */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                return Integer.parseInt(line);
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }


    /**
     * Prompts until a non-blank line is typed.
     *
     * @throws NoSuchElementException
     *             if input runs out
     */
    private String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();
            if (!line.trim().isEmpty()) {
                return line;
            }
            System.out.println("Please type something.");
        }
    }


    /**
     * Formats cents as dollars, so 1250 becomes "$12.50".
     */
    private String formatMoney(int cents) {
        String sign = cents < 0 ? "-" : "";
        long abs = Math.abs((long)cents);
        return String.format("%s$%d.%02d", sign, abs / 100, abs % 100);
    }
}
