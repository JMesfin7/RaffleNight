package raffle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

/**
 * // -------------------------------------------------------------------------
 * JUnit Test for the Raffle class.
 *
 * @author Group 54
 * @version Oct 15, 2026
 */
class RaffleTest
{
    private Raffle raffle;
    private File saveFile;

    /**
     * Sets up an empty raffle before every test.
     */
    @BeforeEach
    void setUp()
    {
        raffle = new Raffle(500, new Random(1));
        saveFile = new File("testRaffleSave.txt");
    }


    /**
     * Removes the test save file after each test.
     */
    @AfterEach
    void tearDown()
    {
        if (saveFile.exists())
        {
            saveFile.delete();
        }
    }


    /**
     * Tests creating a raffle with a valid ticket price.
     */
    @Test
    void testNormalCaseRaffle()
    {
        assertEquals(
            500,
            raffle.getTicketPriceCents(),
            "Ticket price should be 500 cents.");

        assertEquals(
            0,
            raffle.getTotalRaisedCents(),
            "A new raffle should have raised no money.");

        assertEquals(
            0,
            raffle.getRemainingTicketCount(),
            "A new raffle should have no tickets.");

        assertEquals(
            0,
            raffle.getRemainingPrizeCount(),
            "A new raffle should have no prizes.");

        assertEquals(
            0,
            raffle.getWinners().length,
            "A new raffle should have no winners.");
    }


    /**
     * Tests invalid ticket prices and null random generators.
     */
    @Test
    void testBadConstructorInputs()
    {
        assertEquals(
            "Ticket price must be more than 0 cents.",
            assertThrows(
                IllegalArgumentException.class,
                () -> new Raffle(0, new Random()),
                "A zero ticket price should be rejected.").getMessage());

        assertThrows(
            IllegalArgumentException.class,
            () -> new Raffle(-100, new Random()),
            "A negative ticket price should be rejected.");

        assertEquals(
            "Random cannot be null.",
            assertThrows(
                IllegalArgumentException.class,
                () -> new Raffle(500, null),
                "A null random generator should be rejected.").getMessage());
    }


    /**
     * Tests selling tickets and calculating money raised.
     */
    @Test
    void testSellTickets()
    {
        raffle.sellTickets("Sam Lee", 3);

        assertEquals(
            3,
            raffle.getRemainingTicketCount(),
            "Selling three tickets should add three tickets.");

        assertEquals(
            1500,
            raffle.getTotalRaisedCents(),
            "Three tickets at 500 cents should raise 1500 cents.");

        assertEquals(
            3,
            raffle.getTicketCount("Sam Lee"),
            "Sam should own three tickets.");
    }


    /**
     * Tests that equivalent names share the same ticket count.
     */
    @Test
    void testSellTicketsWithEquivalentNames()
    {
        raffle.sellTickets("Sam Lee", 2);
        raffle.sellTickets("  SAM   LEE ", 3);

        assertEquals(
            5,
            raffle.getTicketCount("sam lee"),
            "Equivalent names should share the same ticket count.");

        assertEquals(
            5,
            raffle.getRemainingTicketCount(),
            "All five tickets should remain in the raffle.");

        assertEquals(
            2500,
            raffle.getTotalRaisedCents(),
            "All five ticket sales should be included in the total.");
    }


    /**
     * Tests that invalid ticket sales do not change the raffle.
     */
    @Test
    void testBadTicketSales()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.sellTickets("Sam Lee", 0),
            "Selling zero tickets should throw an exception.");

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.sellTickets("Sam Lee", -1),
            "Selling a negative number of tickets should throw an exception.");

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.sellTickets("   ", 1),
            "A blank entrant name should be rejected.");

        assertEquals(
            0,
            raffle.getRemainingTicketCount(),
            "Rejected sales must not add tickets.");

        assertEquals(
            0,
            raffle.getTotalRaisedCents(),
            "Rejected sales must not change the money raised.");
    }


    /**
     * Tests adding prizes and rejecting invalid prize names.
     */
    @Test
    void testAddPrize()
    {
        raffle.addPrize("Gift Card");
        raffle.addPrize("Mug");

        assertEquals(
            2,
            raffle.getRemainingPrizeCount(),
            "Both prizes should be available.");

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.addPrize("   "),
            "A blank prize name should be rejected.");

        assertEquals(
            2,
            raffle.getRemainingPrizeCount(),
            "A rejected prize should not change the prize count.");
    }


    /**
     * Tests drawing a winner when tickets and prizes are available.
     */
    @Test
    void testDrawWinner()
    {
        raffle.sellTickets("Sam Lee", 2);
        raffle.addPrize("Gift Card");

        Winner winner = raffle.drawWinner();

        assertNotNull(winner, "Drawing should return a winner.");

        assertEquals(
            1,
            raffle.getRemainingTicketCount(),
            "Drawing should remove one ticket.");

        assertEquals(
            0,
            raffle.getRemainingPrizeCount(),
            "Drawing should award and remove one prize.");

        assertEquals(
            1,
            raffle.getWinners().length,
            "The winner should be recorded.");
    }


    /**
     * Tests drawing when no tickets or prizes are available.
     */
    @Test
    void testDrawWinnerWithoutTicketsOrPrizes()
    {
        assertEquals(
            "There are no tickets to draw.",
            assertThrows(
                IllegalStateException.class,
                () -> raffle.drawWinner(),
                "Drawing without tickets should fail.").getMessage());

        raffle.sellTickets("Sam Lee", 1);

        assertEquals(
            "There are no prizes left.",
            assertThrows(
                IllegalStateException.class,
                () -> raffle.drawWinner(),
                "Drawing without prizes should fail.").getMessage());

        assertEquals(
            1,
            raffle.getRemainingTicketCount(),
            "Failed draws should leave tickets unchanged.");
    }


    /**
     * Tests undoing a draw.
     */
    @Test
    void testUndoLastDraw()
    {
        raffle.sellTickets("Sam Lee", 2);
        raffle.addPrize("Gift Card");

        Winner winner = raffle.drawWinner();
        Winner undone = raffle.undoLastDraw();

        assertSame(
            winner,
            undone,
            "Undo should return the winner from the most recent draw.");

        assertEquals(
            2,
            raffle.getRemainingTicketCount(),
            "Undo should restore the drawn ticket.");

        assertEquals(
            1,
            raffle.getRemainingPrizeCount(),
            "Undo should restore the prize.");

        assertEquals(
            0,
            raffle.getWinners().length,
            "Undo should remove the winner from the winner list.");
    }


    /**
     * Tests that undoing without a previous draw throws an exception.
     */
    @Test
    void testUndoWithoutDraw()
    {
        assertEquals(
            "There is no draw to undo.",
            assertThrows(
                IllegalStateException.class,
                () -> raffle.undoLastDraw(),
                "Undo should fail when there are no previous draws.")
                    .getMessage());
    }


    /**
     * Tests refunding tickets and updating the money raised.
     */
    @Test
    void testRefund()
    {
        raffle.sellTickets("Sam Lee", 4);
        raffle.refund("Sam Lee", 2);

        assertEquals(
            2,
            raffle.getTicketCount("Sam Lee"),
            "Refunding two tickets should leave two tickets.");

        assertEquals(
            2,
            raffle.getRemainingTicketCount(),
            "Refunded tickets should be removed from the raffle.");

        assertEquals(
            1000,
            raffle.getTotalRaisedCents(),
            "Refunding two tickets should subtract 1000 cents.");
    }


    /**
     * Tests that invalid refunds do not change the raffle.
     */
    @Test
    void testBadRefund()
    {
        raffle.sellTickets("Sam Lee", 2);

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.refund("Sam Lee", 0),
            "Refunding zero tickets should fail.");

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.refund("Sam Lee", 3),
            "Refunding more tickets than owned should fail.");

        assertThrows(
            IllegalArgumentException.class,
            () -> raffle.refund("Alex Smith", 1),
            "Refunding tickets owned by someone else should fail.");

        assertEquals(
            2,
            raffle.getRemainingTicketCount(),
            "Rejected refunds should not remove tickets.");

        assertEquals(
            1000,
            raffle.getTotalRaisedCents(),
            "Rejected refunds should not change the total.");
    }


    /**
     * Tests calculating odds for an entrant.
     */
    @Test
    void testGetOdds()
    {
        raffle.sellTickets("Sam Lee", 2);
        raffle.sellTickets("Alex Smith", 2);

        assertEquals(
            0.5,
            raffle.getOdds("Sam Lee"),
            0.0001,
            "Two out of four tickets should give 50 percent odds.");

        assertEquals(
            0.0,
            raffle.getOdds("Unknown Person"),
            0.0001,
            "A person without tickets should have zero odds.");

        raffle.refund("Sam Lee", 2);
        raffle.refund("Alex Smith", 2);

        assertEquals(
            0.0,
            raffle.getOdds("Sam Lee"),
            0.0001,
            "Odds should be zero when no tickets remain.");
    }


    /**
     * Tests saving and loading raffle state.
     */
    @Test
    void testSaveAndLoad()
    {
        raffle.sellTickets("Sam Lee", 3);
        raffle.addPrize("Gift Card");
        raffle.addPrize("Mug");

        raffle.save(saveFile.getPath());

        Raffle loaded = new Raffle(100, new Random(2));
        loaded.load(saveFile.getPath());

        assertEquals(
            500,
            loaded.getTicketPriceCents(),
            "Loading should restore the saved ticket price.");

        assertEquals(
            1500,
            loaded.getTotalRaisedCents(),
            "Loading should restore the money raised.");

        assertEquals(
            3,
            loaded.getRemainingTicketCount(),
            "Loading should restore remaining tickets.");

        assertEquals(
            2,
            loaded.getRemainingPrizeCount(),
            "Loading should restore remaining prizes.");

        assertEquals(
            3,
            loaded.getTicketCount("Sam Lee"),
            "Loading should restore ticket ownership.");
    }


    /**
     * Tests loading a nonexistent file.
     */
    @Test
    void testLoadMissingFile()
    {
        assertThrows(
            IllegalStateException.class,
            () -> raffle.load("missingRaffleFile123.txt"),
            "Loading a nonexistent file should throw an exception.");

        assertEquals(
            500,
            raffle.getTicketPriceCents(),
            "A failed load should preserve the original ticket price.");

        assertEquals(
            0,
            raffle.getRemainingTicketCount(),
            "A failed load should preserve the original tickets.");
    }


    /**
     * Tests loading a file with an invalid save format.
     *
     * @throws Exception
     *             if the test file cannot be created
     */
    @Test
    void testLoadInvalidFile()
        throws Exception
    {
        try (PrintWriter writer = new PrintWriter(saveFile))
        {
            writer.println("INVALID\tDATA");
        }

        assertThrows(
            IllegalStateException.class,
            () -> raffle.load(saveFile.getPath()),
            "An invalid save file should be rejected.");

        assertEquals(
            500,
            raffle.getTicketPriceCents(),
            "An invalid file should not replace the ticket price.");

        assertEquals(
            0,
            raffle.getRemainingTicketCount(),
            "An invalid file should not add tickets.");
    }
}
