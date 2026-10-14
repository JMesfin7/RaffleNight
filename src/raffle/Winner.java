package raffle;

/**
 * A drawn ticket paired with the prize it won.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class Winner {

    private final Ticket ticket;
    private final Prize prize;

    /**
     * Creates a winner.
     *
     * @param ticket
     *            the ticket that was drawn
     * @param prize
     *            the prize it won
     * @throws IllegalArgumentException
     *             if either argument is null
     */
    public Winner(Ticket ticket, Prize prize) {
        if (ticket == null || prize == null) {
            throw new IllegalArgumentException(
                "Winner needs both a ticket and a prize.");
        }
        this.ticket = ticket;
        this.prize = prize;
    }


    /**
     * @return the winning ticket
     */
    public Ticket getTicket() {
        return ticket;
    }


    /**
     * @return the prize won
     */
    public Prize getPrize() {
        return prize;
    }


    /**
     * @return the owner of the winning ticket
     */
    public Entrant getEntrant() {
        return ticket.getOwner();
    }


    /**
     * @return text like "#12 – Ana Lee wins Mug"
     */
    @Override
    public String toString() {
        return ticket + " wins " + prize;
    }
}
