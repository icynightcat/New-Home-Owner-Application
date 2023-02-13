import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentTest {

    PropertyAssessment pa1;
    PropertyAssessment pa2;
    PropertyAssessment pa3;


    @BeforeEach
    void setUp(){
        pa1 = new PropertyAssessment(1, "suite1", 1, "street1", 'Y', 1, "neighbourhood1", "ward1", 123456, 2.000, 3.000, 20.00, 30.00, 50.00, "class1", "class2", "class3");
        pa2 = new PropertyAssessment(1, "suite1", 1, "street1", 'Y', 1, "neighbourhood1", "ward1", 123456, 2.000, 3.000, 20.00, 30.00, 50.00, "class1", "class2", "class3");
        pa3 = new PropertyAssessment(3, "suite3", 3, "street3", 'N', 3, "neighbourhood3", "ward2", 150000, 23.000, 32.000, 10.00, 40.00, 50.00, "class1", "class2", "class3");
    }


    @Test
    void testEquals() {
        assertTrue(pa1.equals(pa2) && pa2.equals(pa1));
        assertFalse(pa1.equals(pa3));
    }

    @Test
    void testHashCode() {
        assertEquals(pa1.hashCode(), pa2.hashCode());
    }

    @Test
    void getAccountNumber() {
        assertEquals(1, pa1.getAccountNumber());
    }

    @Test
    void getAddress() {
        Address add = new Address("suite1", 1, "street1");
        assertEquals(add, pa1.getAddress());
    }

    @Test
    void getGarage() {
        assertEquals('Y', pa1.getGarage());
    }

    @Test
    void getNeighbourhood() {
        Neighbourhood neigh = new Neighbourhood(1, "neighbourhood1", "ward1");
        assertEquals(neigh, pa2.getNeighbourhood());
    }

    @Test
    void getAssessedValue() {
        assertEquals(123456, pa2.getAssessedValue());
    }

    @Test
    void getLocation() {
        Location loc = new Location(23.000, 32.000);
        assertEquals(loc, pa3.getLocation());
    }

    @Test
    void getAssessmentClass() {
        AssessmentClass ac = new AssessmentClass(10.00, 40.00, 50.00, "class1", "class2", "class3");
        assertEquals(ac, pa3.getAssessmentClass());
    }

    @Test
    void compareTo() {
        assertEquals(0, pa1.compareTo(pa2));
        assertTrue(pa1.compareTo(pa3) < 0);
        assertTrue(pa3.compareTo(pa1) > 0);
        assertThrows(NullPointerException.class, () -> pa1.compareTo(null));
    }
}