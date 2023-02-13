import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {

    private final List<PropertyAssessment> properties;

    /**
     * Default constructor, uses "Property_Assessment_Data_2022.csv" as the default csv file is none are listed
     */
    public CsvPropertyAssessmentDAO() throws IOException {
        String csvFile = "Property_Assessment_Data_2022.csv";
        this.properties = readCSV(csvFile);
    }

    /**
     * Normal constructor takes in a specified csv file
     * @param csvFile a csv file holding property assessments
     */
    public CsvPropertyAssessmentDAO(String csvFile) throws IOException {
        this.properties = readCSV(csvFile);
    }

    /**
     * helper function to set up the list of property assessments from a csv file
     * @param csvFile given file holding property assessments
     * @return a list of property assessments to be stored in this.properties
     * @throws IOException in case the file is invalid
     */
    private List<PropertyAssessment> readCSV(String csvFile) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile));

        //Create list of string lists to store data from file
        List<PropertyAssessment> properties = new ArrayList<>();

        //first string list in the list will store the header data
        //read header
        String line;

        line = reader.readLine(); //read first line to get rid of header

        //iterate through file data
        while ((line = reader.readLine()) != null) {
            //store line data in data (first index will be header)
            //REF: https://stackoverflow.com/a/7488676
            List<String> entry = new ArrayList<>( Arrays.asList(line.split(",", 18)) );

            //make propertyassessment
            //REF for handling parsing empty entries: https://stackoverflow.com/a/35507836
            properties.add( new PropertyAssessment( Integer.parseInt("0" + entry.get(0)), entry.get(1), Integer.parseInt("0" + entry.get(2)), entry.get(3), entry.get(4).charAt(0), Integer.parseInt("0" + entry.get(5)), entry.get(6), entry.get(7), Integer.parseInt("0" + entry.get(8)), Double.parseDouble(entry.get(9)), Double.parseDouble(entry.get(10)), Double.parseDouble("0" + entry.get(12)), Double.parseDouble("0" + entry.get(13)), Double.parseDouble("0" + entry.get(14)), entry.get(15), entry.get(16), entry.get(17) ) );
        }
        return properties;
    }

    public List<PropertyAssessment> getProperties() {
        //REF: https://www.baeldung.com/java-copy-list-to-another
        // return a copy of the mutable object, not just the object
        //return List.copyOf(properties);
        //does not need a deep copy, as it is immutable (no setter)
        return new ArrayList<>(properties);

    }

    public PropertyAssessment getByAccountNumber(int accountNumber){
        for(PropertyAssessment property : getProperties()){
            if (property.getAccountNumber() == accountNumber) {
                return property;
            }
        }
        return null;
    }

    public List<PropertyAssessment> getByNeighbourhood(String neighbourhood){
        List<PropertyAssessment> propertiesInNeighbourhood = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            if (property.getNeighbourhood().getNeighbourhood().equalsIgnoreCase(neighbourhood)) {
                propertiesInNeighbourhood.add(property);
            }
        }
        return propertiesInNeighbourhood;
    }

    public List<PropertyAssessment> getByAssessmentClass(String assessmentClass){
        List<PropertyAssessment> propertiesInClass = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            if ( ( property.getAssessmentClass().getClass1().equalsIgnoreCase(assessmentClass) ) ||
                    ( property.getAssessmentClass().getClass2().equalsIgnoreCase(assessmentClass) ) ||
                    ( property.getAssessmentClass().getClass3().equalsIgnoreCase(assessmentClass) ) ) {
                propertiesInClass.add(property);
            }
        }
        return propertiesInClass;
    }

}
