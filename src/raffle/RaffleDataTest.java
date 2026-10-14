package raffle;

import student.TestCase;

/**
 * Tests for the data classes: Entrant, Ticket, Prize, and Winner.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class RaffleDataTest extends TestCase {

    private Entrant ana;

    /**
     * Creates a sample entrant before each test.
     */
    public void setUp() {
        ana = new Entrant("Ana Lee");
    }


    /**
     * The constructor cleans the display name and builds the key.
     */
    public void testEntrantConstructor() {
        Entrant e = new Entrant("  Ana   Lee  ");
        assertEquals("Ana Lee", e.getDisplayName());
        assertEquals("ana lee", e.getKey());
    }


    /**
     * The constructor rejects blank and null names.
     */
    public void testEntrantConstructorBad() {
        Exception thrown = null;
        try {
            new Entrant("");
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            new Entrant(null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * normalize trims, collapses spaces, and lowercases.
     */
    public void testNormalize() {
        assertEquals("ana lee", Entrant.normalize("  ANA   lee  "));
        assertEquals("", Entrant.normalize("   "));
        assertEquals("", Entrant.normalize(null));
    }


    /**
     * Entrants are equal by key.
     */
    public void testEntrantEquals() {
        Entrant upper = new Entrant("ANA LEE");
        assertTrue(ana.equals(upper));
        assertEquals(ana.hashCode(), upper.hashCode());
        assertTrue(ana.equals(ana));
        assertFalse(ana.equals(new Entrant("Ben")));
        assertFalse(ana.equals(null));
        assertFalse(ana.equals("ana lee"));
    }


    /**
     * toString shows the trimmed display name, not the key.
     */
    public void testEntrantToString() {
        assertEquals("ana LEE", new Entrant(" ana LEE ").toString());
    }


    /**
     * Ticket stores its id and owner.
     */
    public void testTicket() {
        Ticket t = new Ticket(7, ana);
        assertEquals(7, t.getId());
        assertSame(ana, t.getOwner());
    }


    /**
     * Ticket rejects a null owner.
     */
    public void testTicketNullOwner() {
        Exception thrown = null;
        try {
            new Ticket(7, null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * Tickets are equal by id.
     */
    public void testTicketEquals() {
        Ticket t = new Ticket(7, ana);
        assertTrue(t.equals(new Ticket(7, new Entrant("Ben"))));
        assertEquals(t.hashCode(), new Ticket(7, ana).hashCode());
        assertTrue(t.equals(t));
        assertFalse(t.equals(new Ticket(8, ana)));
        assertFalse(t.equals(null));
        assertFalse(t.equals("#7"));
    }


    /**
     * Ticket toString shows id and owner.
     */
    public void testTicketToString() {
        assertEquals("#12 – Ana Lee", new Ticket(12, ana).toString());
    }


    /**
     * Prize stores a trimmed name.
     */
    public void testPrize() {
        Prize p = new Prize(" Mug ");
        assertEquals("Mug", p.getName());
        assertEquals("Mug", p.toString());
    }


    /**
     * Prize rejects blank and null names.
     */
    public void testPrizeBad() {
        Exception thrown = null;
        try {
            new Prize("  ");
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            new Prize(null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * Winner returns what was passed in.
     */
    public void testWinner() {
        Ticket t = new Ticket(12, ana);
        Prize p = new Prize("Mug");
        Winner w = new Winner(t, p);
        assertSame(t, w.getTicket());
        assertSame(p, w.getPrize());
        assertSame(ana, w.getEntrant());
        assertEquals("#12 – Ana Lee wins Mug", w.toString());
    }


    /**
     * Winner rejects a null ticket or prize.
     */
    public void testWinnerBad() {
        Exception thrown = null;
        try {
            new Winner(null, new Prize("Mug"));
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            new Winner(new Ticket(1, ana), null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }
}
