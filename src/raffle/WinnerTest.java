package raffle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * // -------------------------------------------------------------------------
 * /** JUnit Test for the Winner Class
 * 
 * @author Group 54
 * @version Oct 15, 2026
 */

class WinnerTest
{
    private Entrant testEntrant;
    private Ticket testTicket;
    private Prize testPrize;
    private LinkedChain<Winner> winnerChain;

    /**
     * The setup for the test cases. Initializes the objects and a winner chain
     * before every test.
     */
    @BeforeEach
    void setUp()
    {
        testEntrant = new Entrant("Ana Lee");
        testTicket = new Ticket(12, testEntrant);
        testPrize = new Prize("Mug");
        winnerChain = new LinkedChain<>();
    }


    /**
     * Tests a valid Winner object creation and its basic getters.
     */
    @Test
    void testNormalCase()
    {
        Winner winner = new Winner(testTicket, testPrize);
        assertEquals(
            testTicket,
            winner.getTicket(),
            "The winning ticket should match input.");
        assertEquals(
            testPrize,
            winner.getPrize(),
            "The prize won should match input.");
        assertEquals(
            testEntrant,
            winner.getEntrant(),
            "The entrant should match the ticket owner.");

        String expectedString =
            testTicket.toString() + " wins " + testPrize.toString();
        assertEquals(
            expectedString,
            winner.toString(),
            "toString format must match standard.");
    }


    /**
     * Tests that passing a null ticket or a null prize throws an exception.
     */
    @Test
    void testNullArgumentsThrowException()
    {
        // Case A: Null Ticket
        @SuppressWarnings("unused")
        IllegalArgumentException ticketException =
            assertThrows(IllegalArgumentException.class, () -> {
                new Winner(null, testPrize);
            },
                "Constructor must throw IllegalArgumentException when ticket is null.");
        assertEquals(
            "Winner needs both a ticket and a prize.",
            ticketException.getMessage());

        // Case B: Null Prize
        @SuppressWarnings("unused")
        IllegalArgumentException prizeException =
            assertThrows(IllegalArgumentException.class, () -> {
                new Winner(testTicket, null);
            },
                "Constructor must throw IllegalArgumentException when prize is null.");
        assertEquals(
            "Winner needs both a ticket and a prize.",
            prizeException.getMessage());
    }


    /**
     * Verifies the collection reports empty properties
     */
    @Test
    void testWinnerEmpty()
    {
        assertTrue(
            winnerChain.isEmpty(),
            "The winner chain must start completely empty.");
        assertEquals(
            0,
            winnerChain.getCurrentSize(),
            "An empty winner chain must report a size of 0.");
    }


    /**
     * Attempts to remove a missing winner.
     */
    @Test
    void testRemoveWinner()
    {
        Winner missingWinner = new Winner(testTicket, testPrize);
        boolean removed = winnerChain.remove(missingWinner);
        assertFalse(
            removed,
            "removing from an empty winner chain must be false.");

        Ticket altTicket = new Ticket(99, testEntrant);
        Winner activeWinner = new Winner(altTicket, testPrize);
        winnerChain.add(activeWinner);

        boolean removedMissing = winnerChain.remove(missingWinner);
        assertFalse(
            removedMissing,
            "removing a non-existent winner must return false.");
        assertEquals(
            1,
            winnerChain.getCurrentSize(),
            "Chain size must remain unchanged.");
    }
}
