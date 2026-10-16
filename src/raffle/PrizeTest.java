package raffle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * // -------------------------------------------------------------------------
 * /** JUnit Test for the Prize class.
 * 
 * @author Group 54
 * @version Oct 15, 2026
 */

class PrizeTest
{
    private Prize standardPrize;
    private LinkedChain<Prize> prizeChain;

    /**
     * The setup for the test cases Initializes a sample prize and a prize chain
     * before every test.
     */
    @BeforeEach
    void setUp()
    {
        standardPrize = new Prize("Mug");
        prizeChain = new LinkedChain<>();
    }


    /**
     * Tests valid Prize object creation.
     */
    @Test
    void testNormalCasePrize()
    {
        Prize prize = new Prize("    IPad Pro     ");
        assertEquals(
            "IPad Pro",
            prize.getName(),
            "The prize name should be trimmed.");
        assertEquals(
            "IPad Pro",
            prize.toString(),
            "toString must return the clean prize name.");
    }


    /**
     * Tests cases passing null or blank names throws an exception.
     */
    @Test
    void testBadInputCaseBlankNamesThrowException()
    {
        @SuppressWarnings("unused")
        IllegalArgumentException nullException =
            assertThrows(IllegalArgumentException.class, () -> {
                new Prize(null);
            },
                "Constructor must throw IllegalArgumentException when name is null.");
        assertEquals("Prize name cannot be blank.", nullException.getMessage());

        @SuppressWarnings("unused")
        IllegalArgumentException emptyException =
            assertThrows(IllegalArgumentException.class, () -> {
                new Prize("");
            },
                "Constructor must throw IllegalArgumentException when name is empty.");
        assertEquals(
            "Prize name cannot be blank.",
            emptyException.getMessage());

        @SuppressWarnings("unused")
        IllegalArgumentException spaceException =
            assertThrows(IllegalArgumentException.class, () -> {
                new Prize("    ");
            },
                "Constructor must throw IllegalArgumentException when name is only spaces.");
        assertEquals(
            "Prize name cannot be blank.",
            spaceException.getMessage());
    }


    /**
     * Verifies that the collection reports empty properties.
     */
    @Test
    void testPrizeCollectionEmpty()
    {
        assertTrue(
            prizeChain.isEmpty(),
            "The prize chain must start completely empty.");
        assertEquals(
            0,
            prizeChain.getCurrentSize(),
            "An empty prize chain must report a size of 0.");
    }


    /**
     * Confirms that attempting to remove a missing prize returns false.
     */
    @Test
    void testRemovePrize()
    {
        Prize missingPrize = new Prize("Laptop");
        boolean removed = prizeChain.remove(missingPrize);
        assertFalse(removed, "Removing from an empty chain must return false.");

        prizeChain.add(standardPrize);

        boolean removedMissing = prizeChain.remove(missingPrize);
        assertFalse(
            removedMissing,
            "Removing a non-existent prize must return false.");
        assertEquals(
            1,
            prizeChain.getCurrentSize(),
            "The chain size must remain unchanged.");
    }

}
