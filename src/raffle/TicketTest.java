package raffle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * // -------------------------------------------------------------------------
 * /** JUnit Test for the Ticket Class
 * 
 * @author Group 54
 * @version Oct 15, 2026
 */

class TicketTest
{
    private Entrant testOwner;
    private LinkedChain<Ticket> ticketChain;

    /**
     * The setup for the test cases. Initializes a sample entrant and a ticket
     * chain before every test.
     */
    @BeforeEach
    void setUp()
    {
        testOwner = new Entrant("Ana Lee");
        ticketChain = new LinkedChain<>();
    }


    /**
     * Verifies Ticket constructor works properly under normal conditions.
     */
    @Test
    void testNormalCase()
    {
        Ticket ticket = new Ticket(42, testOwner);
        assertEquals(42, ticket.getId(), "Ticket ID should match input.");
        assertEquals(
            testOwner,
            ticket.getOwner(),
            "Ticket owner should match input.");
    }


    /**
     * Verifies the code can handle a bad-input case.
     */
    @Test
    void testNullOwnerThrowsException()
    {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                @SuppressWarnings("unused")
                Ticket unused = new Ticket(1, null);
            },
                "Constructor must throw IllegalArgumentException when owner is null.");

        assertEquals("Ticket owner cannot be null.", exception.getMessage());
    }


    /**
     * Verifies how the code handles an empty collection.
     */
    @Test
    void testInitiallyEmpty()
    {
        assertTrue(
            ticketChain.isEmpty(),
            "Ticket collection should be completely empty.");
        assertEquals(
            0,
            ticketChain.getCurrentSize(),
            "An empty collection must havea size of 0.");
    }


    /**
     * Verifies the code can handle unexpected removal requests.
     */
    @Test
    void testRemoveTicket()
    {
        Ticket missingTicket = new Ticket(999, testOwner);
        boolean removedEmpty = ticketChain.remove(missingTicket);
        assertFalse(
            removedEmpty,
            "Removing from an empty chain must return false.");
    }

}