package raffle;

/**
 * A prize that can be awarded to a winning ticket.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class Prize {

    private final String name;

    /**
     * Creates a prize.
     *
     * @param name
     *            the name or description of the prize
     * @throws IllegalArgumentException
     *             if name is null or blank
     */
    public Prize(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Prize name cannot be blank.");
        }
        this.name = name.trim();
    }


    /**
     * @return the prize name
     */
    public String getName() {
        return name;
    }


    /**
     * @return the prize name
     */
    @Override
    public String toString() {
        return name;
    }
}
