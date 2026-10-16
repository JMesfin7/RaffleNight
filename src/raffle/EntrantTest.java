package raffle;

import student.TestCase;

/**
 * Tests for the entrant class which tests the methods of the entrant class
 *
 * @author NoahKitatani
 * @version 2026.10.14
 */
public class EntrantTest extends TestCase {

    private Entrant sam;
    private Entrant messySam;
    private Entrant alex;

    /**
     * Sets up entrants used in the tests.
     */
    public void setUp() {
        sam = new Entrant("Sam Lee");
        messySam = new Entrant("  sam   LEE  ");
        alex = new Entrant("Alex Kim");
    }


    // ----------------------------------------------------------
    /**
     * This tests the constructor when a null object is passed into it.
     */
    public void testConstructorNull() {
      
        Exception thrown = null;
      
        try {
            new Entrant(null);
        }
        catch (Exception e) {
            thrown = e;
        }
      
        assertNotNull(thrown);
      
        assertTrue(thrown instanceof IllegalArgumentException);
      
        assertEquals("Name cannot be blank.", thrown.getMessage());
    }


    /**
     * This tests the constructor of the class when an empty 
     * string is passed into it
     */
    public void testConstructorBlank() {
      
        Exception thrown = null;
      
        try {
            new Entrant("");
        }
        catch (Exception e) {
            thrown = e;
        }
      
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
      
        try {
            new Entrant("   \t  ");
        }
        catch (Exception e) {
            thrown = e;
        }
      
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * This is the test class for the get display name getter method
     */
    public void testGetDisplayName() {
        assertEquals("Sam Lee", sam.getDisplayName());
        assertEquals("sam LEE", messySam.getDisplayName());
    }


    /**
     * This tests the get key getter method for the class
     */
    public void testGetKey() {
        assertEquals("sam lee", sam.getKey());
        assertEquals("sam lee", messySam.getKey());
        assertEquals("alex kim", alex.getKey());
    }


    /**
     * This is the test method for the different cases that the method can 
     output
     */
    public void testNormalize() {
        assertEquals("", Entrant.normalize(null));
        assertEquals("", Entrant.normalize("   "));
        assertEquals("sam lee", Entrant.normalize("  SAM \t\n  Lee "));
        assertEquals("alex", Entrant.normalize("Alex"));
    }


    /**
     * This is the test method for the equals method when given different 
     * return cases
     */
    public void testEquals() {
 
        assertTrue(sam.equals(sam));

        assertFalse(sam.equals(null));

        Object non = "sam lee";
        assertFalse(sam.equals(non));

        assertTrue(sam.equals(messySam));
        assertTrue(messySam.equals(sam));

        assertFalse(sam.equals(alex));
    }


    /**
     * Tests that equal entrants have equal hashcode values
     */
    public void testHashCode() {
        assertEquals(sam.hashCode(), messySam.hashCode());
        assertEquals("sam lee".hashCode(), sam.hashCode());
    }


    /**
     * This tests the toString method of the clas
     */
    public void testToString() {
        assertEquals("Sam Lee", sam.toString());
        assertEquals("sam LEE", messySam.toString());
    }
}
