package raffle;

import java.util.Random;
import java.util.Scanner;
import student.TestCase;

/**
 * Tests for RaffleMenu. Each test feeds the menu a fixed String of input.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class RaffleMenuTest extends TestCase {

    private Raffle raffle;

    /**
     * Creates a new raffle before each test.
     */
    public void setUp() {
        raffle = new Raffle(200, new Random(42));
    }


    /**
     * Runs the menu with the given lines of input and returns the output.
     */
    private String run(String... lines) {
        String text = String.join("\n", lines) + "\n";
        new RaffleMenu(raffle, new Scanner(text)).run();
        return systemOut().getHistory();
    }


    /**
     * The menu ends cleanly when there is no input.
     */
    public void testNoInput() {
        new RaffleMenu(raffle, new Scanner("")).run();
        assertTrue(systemOut().getHistory().contains("Goodbye"));
    }


    /**
     * Selling tickets shows the new total.
     */
    public void testSellThenQuit() {
        String out = run("1", "Ana", "2", "0");
        assertTrue(out.contains("$4.00"));
        assertEquals(2, raffle.getTicketCount("Ana"));
    }


    /**
     * Typing a non-number re-prompts instead of crashing.
     */
    public void testBadNumber() {
        String out = run("abc", "0");
        assertTrue(out.contains("Please enter a whole number."));
        assertTrue(out.contains("Goodbye"));
    }


    /**
     * A blank name re-prompts.
     */
    public void testBlankName() {
        String out = run("1", "   ", "Ana", "1", "0");
        assertTrue(out.contains("Please type something."));
        assertEquals(1, raffle.getTicketCount("Ana"));
    }


    /**
     * A choice not on the menu is reported.
     */
    public void testUnknownChoice() {
        String out = run("42", "0");
        assertTrue(out.contains("Please choose a number from the menu."));
    }


    /**
     * Drawing with no prizes prints an error and the menu keeps going.
     */
    public void testDrawWithNoPrizes() {
        String out = run("1", "Ana", "1", "3", "1", "Ben", "1", "0");
        assertTrue(out.contains("Error: There are no prizes left."));
        assertEquals(1, raffle.getTicketCount("Ben"));
    }


    /**
     * A bad sale count prints the error from Raffle.
     */
    public void testBadCount() {
        String out = run("1", "Ana", "0", "0");
        assertTrue(out.contains("Error:"));
        assertEquals(0, raffle.getRemainingTicketCount());
    }


    /**
     * Add a prize, draw, undo, refund, odds, and status all work.
     */
    public void testFullSession() {
        String out = run("1", "Ana Lee", "3", "1", "Ben", "1", "2", "Mug",
            "3", "4", "5", "ana lee", "1", "6", "Ana Lee", "7", "0");
        assertTrue(out.contains("Prizes remaining: 1"));
        assertTrue(out.contains("Winner: #"));
        assertTrue(out.contains("Undid draw: #"));
        assertTrue(out.contains("Refunded $2.00. Total raised: $6.00"));
        assertTrue(out.contains("Ana Lee has 2 ticket(s), 66.7% chance"));
        assertTrue(out.contains("Tickets (3):"));
        assertTrue(out.contains("Total raised: $6.00"));
    }


    /**
     * Saving to a bad path prints an error.
     */
    public void testSaveError() {
        String out = run("8", "/no/such/folder/raffle.txt", "0");
        assertTrue(out.contains("Error: Could not save"));
    }


    /**
     * main starts the menu after a valid price.
     */
    public void testMain() {
        setSystemIn("200", "1", "Ana", "2", "0");
        RaffleMenu.main(new String[0]);
        String out = systemOut().getHistory();
        assertTrue(out.contains("=== RaffleNight ==="));
        assertTrue(out.contains("$4.00"));
    }


    /**
     * main re-asks for the price after a bad one.
     */
    public void testMainBadPrice() {
        setSystemIn("0", "abc", "150", "0");
        RaffleMenu.main(new String[0]);
        String out = systemOut().getHistory();
        assertTrue(out.contains("Ticket price must be more than 0 cents."));
        assertTrue(out.contains("Please enter a whole number."));
        assertTrue(out.contains("=== RaffleNight ==="));
    }


    /**
     * main ends quietly if input runs out before a price.
     */
    public void testMainNoInput() {
        setSystemIn();
        RaffleMenu.main(new String[0]);
        assertFalse(systemOut().getHistory().contains("=== RaffleNight ==="));
    }
}
