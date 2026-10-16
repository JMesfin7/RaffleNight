package raffle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


/**
 * Tests for the entrant class which tests the methods of the entrant class
 *
 * @author NoahKitatani (Group 54)
 * @version 2026.10.14
 */
public class EntrantTest
{

    private Entrant sam;
    private Entrant messySam;
    private Entrant alex;

    /**
     * Sets up entrants used in the tests.
     */
    @BeforeEach
    public void setUp()
    {
        sam = new Entrant("Sam Lee");
        messySam = new Entrant("  sam   LEE  ");
        alex = new Entrant("Alex Kim");
    }


    // ----------------------------------------------------------
    /**
     * This tests the constructor when a null object is passed into it.
     */
    @Test
    public void testConstructorNull()
    {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, 
            () -> new Entrant(null)
        );

        assertEquals("Name cannot be blank.", thrown.getMessage());
    }


    /**
     * This tests the constructor of the class when an empty string is passed
     * into it
     */
    @Test
    public void testConstructorBlank()
    {
        assertThrows(IllegalArgumentException.class, () -> new Entrant(""));
        assertThrows(IllegalArgumentException.class, () -> new Entrant("   \t  "));
    }


    /**
     * This is the test class for the get display name getter method
     */
    @Test
    public void testGetDisplayName()
    {
        assertEquals("Sam Lee", sam.getDisplayName());
        assertEquals("sam LEE", messySam.getDisplayName());
    }


    /**
     * This tests the get key getter method for the class
     */
    @Test
    public void testGetKey()
    {
        assertEquals("sam lee", sam.getKey());
        assertEquals("sam lee", messySam.getKey());
        assertEquals("alex kim", alex.getKey());
    }


    /**
     * This is the test method for the different cases that the method can
     * output
     */
    @Test
    public void testNormalize()
    {
        assertEquals("", Entrant.normalize(null));
        assertEquals("", Entrant.normalize("   "));
        assertEquals("sam lee", Entrant.normalize("  SAM \t\n  Lee "));
        assertEquals("alex", Entrant.normalize("Alex"));
    }


    /**
     * This is the test method for the equals method when given different return
     * cases
     */
    @Test
    public void testEquals()
    {

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
    @Test
    public void testHashCode()
    {
        assertEquals(sam.hashCode(), messySam.hashCode());
        assertEquals("sam lee".hashCode(), sam.hashCode());
    }


    /**
     * This tests the toString method of the clas
     */
    @Test
    public void testToString()
    {
        assertEquals("Sam Lee", sam.toString());
        assertEquals("sam LEE", messySam.toString());
    }
}
