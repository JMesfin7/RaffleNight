package raffle;

import student.TestCase;

/**
 * Tests for LinkedChain, using LinkedChain&lt;String&gt;.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class LinkedChainTest extends TestCase {

    private LinkedChain<String> chain;

    /**
     * Creates an empty chain before each test.
     */
    public void setUp() {
        chain = new LinkedChain<String>();
    }


    /**
     * Builds a chain whose front-to-back order is the given values.
     */
    private void fill(String... values) {
        for (int i = values.length - 1; i >= 0; i--) {
            chain.add(values[i]);
        }
    }


    /**
     * A new chain is empty, and chains do not share state.
     */
    public void testConstructor() {
        assertEquals(0, chain.getCurrentSize());
        assertTrue(chain.isEmpty());

        LinkedChain<String> other = new LinkedChain<String>();
        chain.add("a");
        assertEquals(0, other.getCurrentSize());
    }


    /**
     * add puts an entry in and rejects null.
     */
    public void testAdd() {
        assertTrue(chain.add("a"));
        assertEquals(1, chain.getCurrentSize());

        LinkedChain<String> empty = new LinkedChain<String>();
        assertFalse(empty.add(null));
        assertEquals(0, empty.getCurrentSize());
    }


    /**
     * remove() takes the front entry, or returns null when empty.
     */
    public void testRemoveFront() {
        fill("b", "a");
        assertEquals("b", chain.remove());
        assertEquals(1, chain.getCurrentSize());

        chain.clear();
        assertNull(chain.remove());
        assertEquals(0, chain.getCurrentSize());
    }


    /**
     * remove(T) unlinks a matching entry and keeps the rest in order.
     */
    public void testRemoveEntry() {
        fill("a", "b", "c");
        assertTrue(chain.remove("b"));
        assertEquals(2, chain.toArray().length);
        assertEquals("a", chain.toArray()[0]);
        assertEquals("c", chain.toArray()[1]);

        assertFalse(chain.remove("z"));
        assertEquals(2, chain.getCurrentSize());
    }


    /**
     * remove(T) works on the first and last entries.
     */
    public void testRemoveEntryEnds() {
        fill("a", "b", "c");
        assertTrue(chain.remove("a"));
        assertTrue(chain.remove("c"));
        assertEquals(1, chain.getCurrentSize());
        assertEquals("b", chain.getEntryAt(0));
    }


    /**
     * getEntryAt returns without removing.
     */
    public void testGetEntryAt() {
        fill("c", "b", "a");
        assertEquals("b", chain.getEntryAt(1));
        assertEquals(3, chain.getCurrentSize());
    }


    /**
     * getEntryAt rejects indexes outside the chain.
     */
    public void testGetEntryAtOutOfRange() {
        fill("c", "b", "a");
        Exception thrown = null;
        try {
            chain.getEntryAt(3);
        }
        catch (IndexOutOfBoundsException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            new LinkedChain<String>().getEntryAt(0);
        }
        catch (IndexOutOfBoundsException e) {
            thrown = e;
        }
        assertNotNull(thrown);

        thrown = null;
        try {
            chain.getEntryAt(-1);
        }
        catch (IndexOutOfBoundsException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * removeIf unlinks every match in one pass.
     */
    public void testRemoveIf() {
        fill("x", "y", "x");
        assertEquals(2, chain.removeIf(s -> s.equals("x")));
        assertEquals(1, chain.getCurrentSize());
        assertEquals("y", chain.getEntryAt(0));
    }


    /**
     * removeIf with no matches leaves the chain alone.
     */
    public void testRemoveIfNoMatch() {
        fill("x", "y");
        assertEquals(0, chain.removeIf(s -> s.equals("z")));
        assertEquals(2, chain.getCurrentSize());
    }


    /**
     * removeIf rejects a null test.
     */
    public void testRemoveIfNull() {
        Exception thrown = null;
        try {
            chain.removeIf(null);
        }
        catch (IllegalArgumentException e) {
            thrown = e;
        }
        assertNotNull(thrown);
    }


    /**
     * contains finds entries and is false on an empty chain.
     */
    public void testContains() {
        assertFalse(chain.contains("a"));
        fill("a", "b");
        assertTrue(chain.contains("a"));
        assertFalse(chain.contains("z"));
    }


    /**
     * getFrequencyOf counts matches.
     */
    public void testGetFrequencyOf() {
        fill("a", "b", "a");
        assertEquals(2, chain.getFrequencyOf("a"));
        assertEquals(0, chain.getFrequencyOf("z"));
    }


    /**
     * getCurrentSize and isEmpty track adds.
     */
    public void testSizeAndIsEmpty() {
        assertTrue(chain.isEmpty());
        chain.add("a");
        assertFalse(chain.isEmpty());
        chain.add("b");
        chain.add("c");
        assertEquals(3, chain.getCurrentSize());
    }


    /**
     * clear empties the chain, even if it was already empty.
     */
    public void testClear() {
        chain.clear();
        assertEquals(0, chain.getCurrentSize());
        fill("a", "b", "c");
        chain.clear();
        assertEquals(0, chain.getCurrentSize());
        assertTrue(chain.isEmpty());
    }


    /**
     * toArray lists entries front first.
     */
    public void testToArray() {
        assertEquals(0, chain.toArray().length);
        chain.add("a");
        chain.add("b");
        Object[] array = chain.toArray();
        assertEquals(2, array.length);
        assertEquals("b", array[0]);
        assertEquals("a", array[1]);
    }
}
