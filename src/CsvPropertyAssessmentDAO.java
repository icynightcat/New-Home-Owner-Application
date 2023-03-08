import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

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

        reader.readLine(); //read first line to get rid of header

        //iterate through file data
        while ((line = reader.readLine()) != null) {
            //store line data in data (first index will be header)
            //REF: https://stackoverflow.com/a/7488676
            List<String> entry = new ArrayList<>( Arrays.asList(line.split(",", 18)) );

            //make propertyAssessment
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

    @Override
    public List<PropertyAssessment> advancedSearch(HashMap<String, String> searchCriteria) {
        String address = searchCriteria.get("address");
        String neighbourhood = searchCriteria.get("neighbourhood");
        String assessment = searchCriteria.get("assessment");
        String min = searchCriteria.get("min");
        String max = searchCriteria.get("max");

        List<PropertyAssessment> matches = new ArrayList<>();

        //address search
        if (address.length() > 0){
            String finalAddress = address.toUpperCase();
            //add all properties that fit into matches
            matches.addAll( getProperties().stream().filter(d->d.getAddress().getSuite().contains(finalAddress)).toList() );
            matches.addAll( getProperties().stream().filter(d->d.getAddress().getStreetName().contains(finalAddress)).toList() );
            matches.addAll( getProperties().stream().filter(d->Integer.toString(d.getAddress().getHouseNumber()).contains(finalAddress) ).toList());
            if (matches.size() == 0){
                //if the operation didn't return any properties then we can return early
                return matches;
            }
        }

        //neighbourhood search
        if(neighbourhood.length() > 0){
            String finalNeighbourhood = neighbourhood.toUpperCase();
            if (address.length() > 0) {
                //matches has the properties from address in it, so we need to filter through those to see which are in
                //the correct neighbourhood
                matches = matches.stream().filter(d->d.getNeighbourhood().getNeighbourhood().contains(finalNeighbourhood)).toList();
            } else {
                //matches has no properties in it yet, so we add the ones that fit the neighbourhood search
                matches.addAll( getProperties().stream().filter(d->d.getNeighbourhood().getNeighbourhood().contains(finalNeighbourhood)).toList() );
            }
            if (matches.size() == 0){
                //if the operation didn't return any properties then we can return early
                return matches;
            }
        }

        //assessment class search
        if(assessment.length() > 0){
            String finalAssessment = assessment.toUpperCase();
            if(neighbourhood.length() > 0 || address.length() > 0){
                //matches has properties from one of the previous searches in it, so we filter through those
                matches = matches.stream().filter(d->d.getAssessmentClass().getClass1().equals(finalAssessment) ||
                        d.getAssessmentClass().getClass2().equals(assessment) ||
                        d.getAssessmentClass().getClass3().equals(assessment)).toList();
            } else{
                //else there is no properties in matches yet
                matches.addAll(getProperties().stream().filter(d->d.getAssessmentClass().getClass1().equals(finalAssessment) ||
                        d.getAssessmentClass().getClass2().equals(assessment) ||
                        d.getAssessmentClass().getClass3().equals(assessment)).toList());
            }
            if (matches.size() == 0){
                //if the operation didn't return any properties then we can return early
                return matches;
            }
        }

        //value search
        if (min.length() > 0 || max.length() > 0){
            if(min.equals("")){
                min = "0";
            }
            if(max.equals("")){
                max = Integer.toString(MAX_VALUE);
            }
            long minn = Long.parseLong(min);
            long maxx = Long.parseLong(max);
            if(neighbourhood.length() > 0 || address.length() > 0 || assessment.length() > 0){
                //there is already properties in matches, we need to filter those
                matches = matches.stream().filter(d->d.getAssessedValue()>minn).filter(d->d.getAssessedValue()<maxx).toList();
            } else {
                //there is no properties yet so add to empty list
                matches.addAll(getProperties().stream().filter(d->d.getAssessedValue()>minn).filter(d->d.getAssessedValue()<maxx).toList());
            }
        }

        return matches;
    }

    @Override
    public List<PropertyAssessment> getByValue(String min, String max) {
        if(min.equals("")){
            min = "0";
        }
        if(max.equals("")){
            max = Integer.toString(MAX_VALUE);
        }
        //gives more space for super large numbers
        long minn = Long.parseLong(min);
        long maxx = Long.parseLong(max);
        return getProperties().stream().filter(d->d.getAssessedValue()>minn).filter(d->d.getAssessedValue()<maxx).toList();
    }

    @Override
    public List<String> getAssessmentClasses() {
        List<String> classes = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            String current = property.getAssessmentClass().getClass1();
            classes.add(current);
            current = property.getAssessmentClass().getClass2();
            classes.add(current);
            current = property.getAssessmentClass().getClass3();
            classes.add(current);

        }
        return classes.stream().distinct().sorted().toList();
    }

    @Override
    public List<PropertyAssessment> getByAddress(String address) {
        String finalAddress = address.toUpperCase();

        List<PropertyAssessment> matches = new ArrayList<>();

        matches.addAll( getProperties().stream().filter(d->d.getAddress().getSuite().contains(finalAddress)).toList() );
        matches.addAll( getProperties().stream().filter(d->d.getAddress().getStreetName().contains(finalAddress)).toList() );
        matches.addAll( getProperties().stream().filter(d->Integer.toString(d.getAddress().getHouseNumber()).contains(finalAddress) ).toList());

        return matches;
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
        String finalNeighbourhood = neighbourhood.toUpperCase();

        return getProperties().stream().filter(d->d.getNeighbourhood().getNeighbourhood().contains(finalNeighbourhood)).toList();
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
