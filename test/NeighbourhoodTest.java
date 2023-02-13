import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NeighbourhoodTest {

    Neighbourhood n1;
    Neighbourhood n2;
    Neighbourhood n3;

    @BeforeEach
    void setUp(){
        n1 = new Neighbourhood(12, "neighbourhood1", "ward1");
        n2 = new Neighbourhood(12, "neighbourhood1", "ward1");
        n3 = new Neighbourhood(15, "neighbourhood2", "ward2");
    }

    @Test
    void testEquals() {
        assertTrue(n1.equals(n2) && n2.equals(n1));
        assertFalse(n1.equals(n3));
    }

    @Test
    void testHashCode() {
        //just make sure that two equal objects have the same hash code
        assertEquals(n1.hashCode(), n2.hashCode());
    }

    @Test
    void getNeighbourhoodID() {
        assertEquals(15, n3.getNeighbourhoodID());
    }

    @Test
    void getNeighbourhood() {
        assertEquals("neighbourhood1", n2.getNeighbourhood());
    }

    @Test
    void getWard() {
        assertEquals("ward1", n1.getWard());
    }
}