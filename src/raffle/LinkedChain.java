package raffle;

import java.util.function.Predicate;

/**
 * A generic linked bag of entries. New entries go at the front of the chain,
 * so adding is O(1). Extends the standard bag with getEntryAt (for the random
 * draw) and removeIf (to remove all of one person's tickets in one pass).
 *
 * @param <T>
 *            the type of entry stored in the chain
 * @author Group 54
 * @version 2026.10.14
 */
public class LinkedChain<T> {

    private Node firstNode;
    private int numberOfEntries;

    /**
     * Creates an empty chain.
     */
    public LinkedChain() {
        firstNode = null;
        numberOfEntries = 0;
    }


    /**
     * Adds a new entry to the front of the chain.
     *
     * @param newEntry
     *            the entry to add
     * @return true if added, false if newEntry is null
     */
    public boolean add(T newEntry) {
        if (newEntry == null) {
            return false;
        }
        firstNode = new Node(newEntry, firstNode);
        numberOfEntries++;
        return true;
    }


    /**
     * Removes and returns the entry at the front of the chain.
     *
     * @return the removed entry, or null if the chain is empty
     */
    public T remove() {
        if (firstNode == null) {
            return null;
        }
        T result = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return result;
    }


    /**
     * Removes one entry equal to anEntry, keeping the order of the rest.
     *
     * @param anEntry
     *            the entry to remove
     * @return true if an entry was removed, false if none matched
     */
    public boolean remove(T anEntry) {
        Node previous = null;
        Node current = firstNode;
        while (current != null) {
            if (current.data.equals(anEntry)) {
                unlink(previous, current);
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }


    /**
     * Returns the entry at the given position without removing it.
     *
     * @param index
     *            position from the front, starting at 0
     * @return the entry at that position
     * @throws IndexOutOfBoundsException
     *             if index is negative or not less than the size
     */
    public T getEntryAt(int index) {
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException("Index " + index
                + " is out of range for size " + numberOfEntries);
        }
        Node current = firstNode;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }


    /**
     * Unlinks every entry for which the test is true, in one pass.
     *
     * @param test
     *            the condition an entry must meet to be removed
     * @return how many entries were removed
     * @throws IllegalArgumentException
     *             if test is null
     */
    public int removeIf(Predicate<? super T> test) {
        if (test == null) {
            throw new IllegalArgumentException("Test cannot be null.");
        }
        int removed = 0;
        Node previous = null;
        Node current = firstNode;
        while (current != null) {
            Node next = current.next;
            if (test.test(current.data)) {
                unlink(previous, current);
                removed++;
            }
            else {
                previous = current;
            }
            current = next;
        }
        return removed;
    }


    /**
     * Checks whether any entry equals anEntry.
     *
     * @param anEntry
     *            the entry to look for
     * @return true if found
     */
    public boolean contains(T anEntry) {
        return getFrequencyOf(anEntry) > 0;
    }


    /**
     * Counts the entries equal to anEntry.
     *
     * @param anEntry
     *            the entry to count
     * @return the number of matching entries
     */
    public int getFrequencyOf(T anEntry) {
        int count = 0;
        Node current = firstNode;
        while (current != null) {
            if (current.data.equals(anEntry)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }


    /**
     * Returns the number of entries in the chain.
     *
     * @return the current size
     */
    public int getCurrentSize() {
        return numberOfEntries;
    }


    /**
     * Checks whether the chain has no entries.
     *
     * @return true if the size is 0
     */
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }


    /**
     * Removes all entries from the chain.
     */
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }


    /**
     * Copies the entries into a new array in chain order (front first).
     *
     * @return a new array of the entries
     */
    public Object[] toArray() {
        Object[] result = new Object[numberOfEntries];
        Node current = firstNode;
        for (int i = 0; i < numberOfEntries; i++) {
            result[i] = current.data;
            current = current.next;
        }
        return result;
    }


    /**
     * Unlinks current from the chain. previous is the node before it, or null
     * if current is the first node.
     */
    private void unlink(Node previous, Node current) {
        if (previous == null) {
            firstNode = current.next;
        }
        else {
            previous.next = current.next;
        }
        numberOfEntries--;
    }


    /**
     * One link in the chain.
     */
    private class Node {
        private T data;
        private Node next;

        /**
         * Creates a node holding data that points to next.
         */
        private Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}
