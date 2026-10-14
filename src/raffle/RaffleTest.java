package raffle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import student.TestCase;

/**
 * Tests for Raffle. Uses new Random(42) and a ticket price of 200 cents.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class RaffleTest extends TestCase {

    private Raffle raffle;
    private File file;

    /**
     * Creates a new raffle and a temp file name before each test.
     */
    public void setUp() throws IOException {
        raffle = new Raffle(200, new Random(42));
        file = File.createTempFile("raffle", ".txt");
        file.delete();
    }


    /**
     * Deletes the temp file after each test.
     */
    public void tearDown() {
        file.delete();
    }


    /**
     * A new raffle is empty.
     */
    public void testConstructor() {
        assertEquals(0, raffle.getRemainingTicketCount());
        assertEquals(0, raffle.getRemainingPrizeCount());
        assertEquals(0, raffle.getTotalRaisedCents());
        assertEquals(200, raffle.getTicketPriceCents());
        assertEquals(0, raffle.getTickets().length);
        assertEquals(0, raffle.getPrizes().length);
        assertEquals(0, raffle.getWinners().length);
    }


    /**
     * The constructor rejects a bad price or null Random.
     */
    public void testConstructorBad() {
        Exception thrown = null;
        try {
            new Raffle(0, new Random(42));
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            new Raffle(200, null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * Selling assigns IDs, adds money, and merges names by key.
     */
    public void testSellTickets() {
        raffle.sellTickets("Ana Lee", 3);
        assertEquals(3, raffle.getRemainingTicketCount());
        assertEquals(600, raffle.getTotalRaisedCents());

        boolean[] seen = new boolean[4];
        for (Object o : raffle.getTickets()) {
            seen[((Ticket)o).getId()] = true;
        }
        assertTrue(seen[1] && seen[2] && seen[3]);

        raffle.sellTickets("ana lee", 2);
        assertEquals(5, raffle.getTicketCount("Ana Lee"));
    }


    /**
     * Repeat buyers share one Entrant object.
     */
    public void testSellTicketsReusesEntrant() {
        raffle.sellTickets("Ana Lee", 1);
        raffle.sellTickets("  ANA lee ", 1);
        Object[] all = raffle.getTickets();
        assertSame(((Ticket)all[0]).getOwner(), ((Ticket)all[1]).getOwner());
    }


    /**
     * Selling rejects bad counts and blank names without changing state.
     */
    public void testSellTicketsBad() {
        int[] counts = { 0, -1 };
        for (int count : counts) {
            Exception thrown = null;
            try {
                raffle.sellTickets("Ana", count);
            }
            catch (IllegalArgumentException e) {
                thrown = e;
            }
            assertNotNull(thrown);
        }

        Exception thrown = null;
        try {
            raffle.sellTickets("   ", 2);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(0, raffle.getRemainingTicketCount());
        assertEquals(0, raffle.getTotalRaisedCents());
    }


    /**
     * Selling rejects a sale that would overflow the total.
     */
    public void testSellTicketsTooMany() {
        Exception thrown = null;
        try {
            raffle.sellTickets("Ana", Integer.MAX_VALUE);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(0, raffle.getTotalRaisedCents());
    }


    /**
     * addPrize adds one prize and rejects blank names.
     */
    public void testAddPrize() {
        raffle.addPrize("Gift card");
        assertEquals(1, raffle.getRemainingPrizeCount());

        Raffle other = new Raffle(200, new Random(42));
        Exception thrown = null;
        try {
            other.addPrize(" ");
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(0, other.getRemainingPrizeCount());
    }


    /**
     * getTicketCount matches names without case.
     */
    public void testGetTicketCount() {
        raffle.sellTickets("Ana", 3);
        raffle.sellTickets("Ben", 1);
        assertEquals(1, raffle.getTicketCount("BEN"));
        assertEquals(3, raffle.getTicketCount("ana"));
        assertEquals(0, raffle.getTicketCount("Cara"));
    }


    /**
     * A draw removes one ticket and one prize and records the winner.
     */
    public void testDrawWinner() {
        raffle.sellTickets("Ana", 3);
        raffle.sellTickets("Ben", 1);
        raffle.addPrize("Mug");
        raffle.addPrize("Gift card");

        Winner w = raffle.drawWinner();
        assertNotNull(w);
        assertEquals("Gift card", w.getPrize().getName());
        assertEquals(3, raffle.getRemainingTicketCount());
        assertEquals(1, raffle.getRemainingPrizeCount());
        assertEquals(1, raffle.getWinners().length);
        for (Object o : raffle.getTickets()) {
            assertFalse(o.equals(w.getTicket()));
        }
    }


    /**
     * Seeded draws are repeatable.
     */
    public void testDrawWinnerRepeatable() {
        Raffle other = new Raffle(200, new Random(42));
        raffle.sellTickets("Ana", 5);
        other.sellTickets("Ana", 5);
        raffle.addPrize("Mug");
        other.addPrize("Mug");
        assertEquals(raffle.drawWinner().getTicket().getId(), other
            .drawWinner().getTicket().getId());
    }


    /**
     * Drawing with no tickets or no prizes throws and changes nothing.
     */
    public void testDrawWinnerBad() {
        raffle.addPrize("Mug");
        Exception thrown = null;
        try {
            raffle.drawWinner();
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        Raffle other = new Raffle(200, new Random(42));
        other.sellTickets("Ana", 2);
        thrown = null;
        try {
            other.drawWinner();
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(2, other.getRemainingTicketCount());
    }


    /**
     * Undo puts the ticket and prize back and removes the winner.
     */
    public void testUndoLastDraw() {
        raffle.sellTickets("Ana", 3);
        raffle.sellTickets("Ben", 1);
        raffle.addPrize("Mug");
        raffle.addPrize("Gift card");
        Winner w = raffle.drawWinner();

        assertSame(w, raffle.undoLastDraw());
        assertEquals(4, raffle.getRemainingTicketCount());
        assertEquals(2, raffle.getRemainingPrizeCount());
        assertEquals(0, raffle.getWinners().length);
        assertEquals("Gift card", ((Prize)raffle.getPrizes()[0]).getName());
    }


    /**
     * Undo works back through several draws, newest first.
     */
    public void testUndoSeveral() {
        raffle.sellTickets("Ana", 4);
        raffle.addPrize("A");
        raffle.addPrize("B");
        Winner first = raffle.drawWinner();
        Winner second = raffle.drawWinner();
        assertSame(second, raffle.undoLastDraw());
        assertSame(first, raffle.undoLastDraw());
        assertEquals(4, raffle.getRemainingTicketCount());
    }


    /**
     * Undo with nothing drawn throws.
     */
    public void testUndoNothing() {
        Exception thrown = null;
        try {
            raffle.undoLastDraw();
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * Refund removes tickets and subtracts money; the price stays fixed.
     */
    public void testRefund() {
        raffle.sellTickets("Ana", 3);
        raffle.sellTickets("Ben", 1);
        raffle.refund("ANA", 1);
        assertEquals(2, raffle.getTicketCount("Ana"));
        assertEquals(1, raffle.getTicketCount("Ben"));
        assertEquals(600, raffle.getTotalRaisedCents());
        assertEquals(200, raffle.getTicketPriceCents());
    }


    /**
     * Refund rejects more tickets than owned, and bad counts.
     */
    public void testRefundBad() {
        raffle.sellTickets("Ana", 3);
        Exception thrown = null;
        try {
            raffle.refund("Ana", 5);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(3, raffle.getTicketCount("Ana"));

        thrown = null;
        try {
            raffle.refund("Ana", 0);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            raffle.refund("Nobody", 1);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(600, raffle.getTotalRaisedCents());
    }


    /**
     * Odds are a share of all tickets, or 0.0 with none.
     */
    public void testGetOdds() {
        assertEquals(0.0, raffle.getOdds("Ana"), 0.0001);
        raffle.sellTickets("Ana", 3);
        raffle.sellTickets("Ben", 1);
        assertEquals(0.75, raffle.getOdds("Ana"), 0.0001);
        assertEquals(0.0, raffle.getOdds("Cara"), 0.0001);
    }


    /**
     * save writes one line per ticket, prize, and winner.
     */
    public void testSave() {
        raffle.sellTickets("Ana", 2);
        raffle.addPrize("Mug");
        raffle.addPrize("Hat");
        raffle.drawWinner();
        raffle.save(file.getPath());
        assertTrue(file.exists());
        // 3 header lines + 1 ticket + 1 prize + 1 winner
        assertEquals(6, fileLines(file.getPath()));
    }


    /**
     * save to a bad path throws and leaves the raffle alone.
     */
    public void testSaveBadPath() {
        raffle.sellTickets("Ana", 2);
        Exception thrown = null;
        try {
            raffle.save("/no/such/folder/raffle.txt");
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(2, raffle.getRemainingTicketCount());
    }


    /**
     * load restores everything save wrote, including undo history.
     */
    public void testSaveThenLoad() {
        raffle.sellTickets("Ana Lee", 3);
        raffle.sellTickets("Ben", 1);
        raffle.addPrize("Mug");
        raffle.addPrize("Gift card");
        raffle.addPrize("Hat");
        Winner w = raffle.drawWinner();
        raffle.save(file.getPath());

        Raffle copy = new Raffle(500, new Random(1));
        copy.load(file.getPath());
        assertEquals(3, copy.getRemainingTicketCount());
        assertEquals(2, copy.getRemainingPrizeCount());
        assertEquals(800, copy.getTotalRaisedCents());
        assertEquals(200, copy.getTicketPriceCents());
        assertEquals(1, copy.getWinners().length);
        assertEquals(raffle.getPrizes()[0].toString(), copy.getPrizes()[0]
            .toString());
        assertEquals(raffle.getTickets()[0], copy.getTickets()[0]);

        Winner undone = copy.undoLastDraw();
        assertEquals(w.getTicket(), undone.getTicket());
        assertEquals(4, copy.getRemainingTicketCount());
        assertEquals(3, copy.getTicketCount("ana lee"));
        assertEquals(1, copy.getTicketCount("Ben"));

        copy.sellTickets("Cara", 1);
        boolean found = false;
        for (Object o : copy.getTickets()) {
            if (((Ticket)o).getId() == 5) {
                found = true;
            }
        }
        assertTrue(found);
    }


    /**
     * load of a missing file throws and keeps the old state.
     */
    public void testLoadMissing() {
        raffle.sellTickets("Ana", 2);
        Exception thrown = null;
        try {
            raffle.load(file.getPath());
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(2, raffle.getRemainingTicketCount());
        assertEquals(400, raffle.getTotalRaisedCents());
    }


    /**
     * load of a malformed file throws and keeps the old state.
     */
    public void testLoadMalformed() throws IOException {
        raffle.sellTickets("Ana", 2);
        FileWriter out = new FileWriter(file);
        out.write("PRICE\t200\nTICKET\tnot-a-number\tAna\n");
        out.close();
        Exception thrown = null;
        try {
            raffle.load(file.getPath());
        }
        catch (IllegalStateException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals(2, raffle.getRemainingTicketCount());
    }


    /**
     * Remaining counts and total track sales and draws.
     */
    public void testCountsAndTotal() {
        raffle.sellTickets("Ana", 2);
        assertEquals(2, raffle.getRemainingTicketCount());
        assertEquals(400, raffle.getTotalRaisedCents());
        assertEquals(2, raffle.getTickets().length);
        raffle.addPrize("Mug");
        raffle.addPrize("Hat");
        raffle.drawWinner();
        assertEquals(1, raffle.getRemainingPrizeCount());
    }


    /**
     * Counts the lines in a file.
     */
    private int fileLines(String path) {
        try (java.io.BufferedReader in = new java.io.BufferedReader(
            new java.io.FileReader(path))) {
            int lines = 0;
            while (in.readLine() != null) {
                lines++;
            }
            return lines;
        }
        catch (IOException e) {
            return -1;
        }
    }
}
