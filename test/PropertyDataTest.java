import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyDataTest {


    private PropertyAssessment prop1;
    private PropertyAssessment prop2;
    private PropertyAssessment prop3;
    private ArrayList<PropertyAssessment> properties;
    private PropertyData data;

    @BeforeEach
    void setUp(){
        String line = "1194653,,8319,156 AVENUE NW,N,2050,BELLE RIVE,tastawiyiniwak Ward,257000,53.616237298079405,-113.46941226946224,POINT (-113.46941226946224 53.616237298079405),100,,,RESIDENTIAL,,";
        ArrayList<String> entry = new ArrayList<>( Arrays.asList(line.split(",", 18)) );
        prop1 = new PropertyAssessment( Integer.parseInt("0" + entry.get(0)), entry.get(1), Integer.parseInt("0" + entry.get(2)), entry.get(3), entry.get(4).charAt(0), Integer.parseInt("0" + entry.get(5)), entry.get(6), entry.get(7), Integer.parseInt("0" + entry.get(8)), Double.parseDouble(entry.get(9)), Double.parseDouble(entry.get(10)), Double.parseDouble("0" + entry.get(12)), Double.parseDouble("0" + entry.get(13)), Double.parseDouble("0" + entry.get(14)), entry.get(15), entry.get(16), entry.get(17) );

        line = "1066158,,14904,167 AVENUE NW,N,,,,86000,53.63049663158542,-113.5804742693191,POINT (-113.5804742693191 53.63049663158542),100,,,RESIDENTIAL,,";
        entry = new ArrayList<>( Arrays.asList(line.split(",", 18)) );
        prop2 = new PropertyAssessment( Integer.parseInt("0" + entry.get(0)), entry.get(1), Integer.parseInt("0" + entry.get(2)), entry.get(3), entry.get(4).charAt(0), Integer.parseInt("0" + entry.get(5)), entry.get(6), entry.get(7), Integer.parseInt("0" + entry.get(8)), Double.parseDouble(entry.get(9)), Double.parseDouble(entry.get(10)), Double.parseDouble("0" + entry.get(12)), Double.parseDouble("0" + entry.get(13)), Double.parseDouble("0" + entry.get(14)), entry.get(15), entry.get(16), entry.get(17) );

        line = "1222967,,16327,64 STREET NW,N,2500,MATT BERRY,Dene Ward,140500,53.62542238247154,-113.43657471493623,POINT (-113.43657471493623 53.62542238247154),100,,,COMMERCIAL,,";
        entry = new ArrayList<>( Arrays.asList(line.split(",", 18)) );
        prop3 = new PropertyAssessment( Integer.parseInt("0" + entry.get(0)), entry.get(1), Integer.parseInt("0" + entry.get(2)), entry.get(3), entry.get(4).charAt(0), Integer.parseInt("0" + entry.get(5)), entry.get(6), entry.get(7), Integer.parseInt("0" + entry.get(8)), Double.parseDouble(entry.get(9)), Double.parseDouble(entry.get(10)), Double.parseDouble("0" + entry.get(12)), Double.parseDouble("0" + entry.get(13)), Double.parseDouble("0" + entry.get(14)), entry.get(15), entry.get(16), entry.get(17) );

        properties = new ArrayList<>();
        properties.add(prop1);
        properties.add(prop2);
        properties.add(prop3);
        data = new PropertyData(properties);
    }

    @Test
    void getProperties() {
        List<PropertyAssessment> getProps = data.getProperties();

        for (int i = 0; i < getProps.size(); i++){
            assertEquals(properties.get(i), getProps.get(i));
        }
        assertEquals(properties.size(), getProps.size());
    }

    @Test
    void getPropertiesInWard() {
        //this should get only one property
        List<PropertyAssessment> got = data.getPropertiesInWard("Dene Ward");
        //grab that property
        PropertyAssessment gotten = got.get(0);
        //make sure it is the one in that ward
        assertEquals(prop3, gotten);
    }

    @Test
    void getPropertiesInClass() {
        List<PropertyAssessment> got = data.getPropertiesInClass("Residential");
        //here are the properties in that class
        List<PropertyAssessment> resProp = new ArrayList<>();
        //make a list of them to make sure getPropertiesInClass is the same
        resProp.add(prop1);
        resProp.add(prop2);

        for (int i = 0; i < got.size(); i++){
            assertEquals(properties.get(i), got.get(i));
        }
        assertEquals(resProp.size(), got.size());
    }


    @Test
    void getPropertiesInNeighbourhood() {
        List<PropertyAssessment> got = data.getPropertiesInClass("Residential");
        //here are the properties in that class
        List<PropertyAssessment> nProp = new ArrayList<>();
        //make a list of them to make sure getPropertiesInClass is the same
        nProp.add(prop1);
        nProp.add(prop2);

        for (int i = 0; i < got.size(); i++){
            assertEquals(nProp.get(i), got.get(i));
        }
        assertEquals(nProp.size(), got.size());
    }

    @Test
    void getPropertyByAccount() {
        PropertyAssessment prop = data.getPropertyByAccount("1222967");
        //this should get prop3
        //lets check
        assertEquals(prop3, prop);
    }

    @Test
    void getWards() {
        List<String> gotWards = data.getWards();

        List<String> wards = new ArrayList<>();
        wards.add(prop1.getNeighbourhood().getWard());
        wards.add(prop2.getNeighbourhood().getWard());
        wards.add(prop3.getNeighbourhood().getWard());

        for (int i = 0; i < gotWards.size(); i++) {
            assertEquals(wards.get(i), gotWards.get(i));
        }
        assertEquals(wards.size(), gotWards.size());
    }

    @Test
    void getAssessmentValueData() {
        //the assessed values of the 3 properties are: 257000, 86000, 140500
        //so the values we want to check for are:
        int n = 3;
        int min = 86000;
        int max = 257000;
        int range = (257000 - 86000);
        int mean = ((257000 + 86000 + 140500)/3);
        int median = 140500;

        int[] values = data.getAssessmentValueData();

        assertEquals(n, values[0]);
        assertEquals(min, values[1]);
        assertEquals(max, values[2]);
        assertEquals(range, values[3]);
        assertEquals(mean, values[4]);
        assertEquals(median, values[5]);
    }


    @Test
    void getNum() {
        assertEquals(3, data.getNum());
    }

    @Test
    void getMax() {
        assertEquals(257000, data.getMax());
    }

    @Test
    void getMin() {
        assertEquals(86000, data.getMin());
    }

    @Test
    void getRange() {
        assertEquals((257000 - 86000), data.getRange());
    }

    @Test
    void getMean() {
        assertEquals(((257000 + 86000 + 140500)/3), data.getMean());
    }

    @Test
    void getMedian() {
        assertEquals(140500, data.getMedian());
    }
}