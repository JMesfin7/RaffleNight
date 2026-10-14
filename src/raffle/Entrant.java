package raffle;

/**
 * A person who buys raffle tickets. Names are compared by a normalized key,
 * so "Sam Lee" and " sam  LEE " are the same entrant.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class Entrant {

    private final String displayName;
    private final String key;

    /**
     * Creates an entrant from a name.
     *
     * @param name
     *            the entrant's name
     * @throws IllegalArgumentException
     *             if name is null or blank
     */
    public Entrant(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank.");
        }
        displayName = name.trim().replaceAll("\\s+", " ");
        key = normalize(name);
    }


    /**
     * Normalizes a name for comparison: trims, collapses runs of whitespace
     * to one space, and lowercases.
     *
     * @param name
     *            the name to normalize
     * @return the normalized key, or "" if name is null
     */
    public static String normalize(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().replaceAll("\\s+", " ").toLowerCase();
    }


    /**
     * @return the cleaned-up name shown to the user
     */
    public String getDisplayName() {
        return displayName;
    }


    /**
     * @return the normalized name used for comparisons
     */
    public String getKey() {
        return key;
    }


    /**
     * Two entrants are equal when their keys match.
     *
     * @param o
     *            the object to compare with
     * @return true if o is an Entrant with the same key
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return key.equals(((Entrant)o).key);
    }


    /**
     * @return a hash code consistent with equals
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }


    /**
     * @return the display name
     */
    @Override
    public String toString() {
        return displayName;
    }
}
