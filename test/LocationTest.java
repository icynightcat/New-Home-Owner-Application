import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    Location l1;
    Location l2;
    Location l3;

    @BeforeEach
    void setUp(){
        l1 = new Location(53.5471, 113.5064);
        l2 = new Location(53.5471, 113.5064);
        l3 = new Location(1.000, -1.000);
    }


    @Test
    void testEquals() {
        assertTrue(l1.equals(l2) && l2.equals(l1));
        assertFalse(l1.equals(l3));
    }

    @Test
    void testHashCode() {
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void getLatitude() {
        assertEquals(1.000, l3.getLatitude());
    }

    @Test
    void getLongitude() {
        assertEquals(-1.000, l3.getLongitude());
    }

    @Test
    void testToString() {
        assertEquals(l1.toString(), "(" + l1.getLatitude() + ", " + l1.getLongitude() + ")");
    }
}