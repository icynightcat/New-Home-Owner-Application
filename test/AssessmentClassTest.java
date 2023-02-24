import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentClassTest {

    AssessmentClass ac1;
    AssessmentClass ac2;
    AssessmentClass ac3;

    @BeforeEach
    void setUp(){
        ac1 = new AssessmentClass(5.00, 10.00, 85.00, "class1", "class2", "class3");
        ac2 = new AssessmentClass(5.00, 10.00, 85.00, "class1", "class2", "class3");
        ac3 = new AssessmentClass(40.00, 10.00, 50.00, "class4", "class5", "class6");
    }

    @Test
    void testEquals() {
        assertTrue(ac1.equals(ac2) && ac2.equals(ac1));
        assertFalse(ac1.equals(ac3));
    }

    @Test
    void testHashCode() {
        assertEquals(ac1.hashCode(), ac2.hashCode());
    }

    @Test
    void getClass1Percent() {
        assertEquals(5.00, ac1.getClass1Percent());
    }

    @Test
    void getClass2Percent() {
        assertEquals(10.00, ac1.getClass2Percent());
    }

    @Test
    void getClass3Percent() {
        assertEquals(85.00, ac1.getClass3Percent());
    }

    @Test
    void getClass1() {
        assertEquals("class4", ac3.getClass1());
    }

    @Test
    void getClass2() {
        assertEquals("class5", ac3.getClass2());
    }

    @Test
    void getClass3() {
        assertEquals("class6", ac3.getClass3());
    }

    @Test
    void testToString() {
        assertEquals(ac1.toString(), "[" + ac1.getClass1() + " " + ac1.getClass1Percent() + "%, " + ac1.getClass2() + " " + ac1.getClass2Percent() + "%, " + ac1.getClass3() + " " + ac1.getClass3Percent() + "%]");
    }
}