package raffle;

/**
 * One raffle ticket with a unique ID and an owner.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class Ticket {

    private final int id;
    private final Entrant owner;

    /**
     * Creates a ticket.
     *
     * @param id
     *            the ticket's unique ID
     * @param owner
     *            the entrant who owns the ticket
     * @throws IllegalArgumentException
     *             if owner is null
     */
    public Ticket(int id, Entrant owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Ticket owner cannot be null.");
        }
        this.id = id;
        this.owner = owner;
    }


    /**
     * @return the ticket's ID
     */
    public int getId() {
        return id;
    }


    /**
     * @return the ticket's owner
     */
    public Entrant getOwner() {
        return owner;
    }


    /**
     * Two tickets are equal when their IDs match.
     *
     * @param o
     *            the object to compare with
     * @return true if o is a Ticket with the same ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return id == ((Ticket)o).id;
    }


    /**
     * @return a hash code consistent with equals
     */
    @Override
    public int hashCode() {
        return id;
    }


    /**
     * @return text like "#12 – Ana Lee"
     */
    @Override
    public String toString() {
        return "#" + id + " – " + owner;
    }
}
