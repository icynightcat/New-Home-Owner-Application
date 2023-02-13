import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    Address a1;
    Address a2;
    Address a3;

    @BeforeEach
    void setUp(){
        a1 = new Address("suite1", 1,"street1");
        a2 = new Address("suite1", 1,"street1");
        a3 = new Address("suite2", 22,"street2");
    }


    @Test
    void testEquals() {
        assertTrue(a1.equals(a2) && a2.equals(a1));
        assertFalse(a1.equals(a3));
    }

    @Test
    void testHashCode() {
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void getSuite() {
        assertEquals("suite1", a1.getSuite());
    }

    @Test
    void getHouseNumber() {
        assertEquals(1, a2.getHouseNumber());
    }

    @Test
    void getStreetName() {
        assertEquals("street2", a3.getStreetName());
    }
}