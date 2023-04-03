import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        //unload the hashmap
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

        classes.addAll(getProperties().stream().map(d->d.getAssessmentClass().getClass1()).distinct().toList());
        classes.addAll(getProperties().stream().map(d->d.getAssessmentClass().getClass2()).distinct().toList());
        classes.addAll(getProperties().stream().map(d->d.getAssessmentClass().getClass3()).distinct().toList());

        return classes.stream().distinct().sorted().toList();
    }

    @Override
    public List<String> getNeighbourhoodLists() {
        List<String> neighbourhoods = new ArrayList<>();

        for (int i = 0; i < properties.size(); i++) {
            if (!neighbourhoods.contains(properties.get(i).getNeighbourhood().getNeighbourhood())) {
                if (!properties.get(i).getNeighbourhood().getNeighbourhood().equals("")) {
                    neighbourhoods.add(properties.get(i).getNeighbourhood().getNeighbourhood());
                }
            }
        }
        Collections.sort(neighbourhoods);
        //System.out.println(neighbourhoods);
        return neighbourhoods;
    }

    @Override
    public List<String> getWards() {
        List<String> wards = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            String current = property.getNeighbourhood().getWard();
            if(!wards.contains(current)){
                wards.add(current);
            }
        }
        return wards;
    }

    @Override
    public List<PropertyAssessment> getPropertiesInWard(String ward) {
        ArrayList<PropertyAssessment> propertiesInWard = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            if (property.getNeighbourhood().getWard().equalsIgnoreCase(ward)) {
                propertiesInWard.add(property);
            }
        }
        return propertiesInWard;
    }

    @Override
    //each value is from 1 to 7
    public void getCostRange(HashMap<String, List<PropertyAssessment>> costRange) {


        costRange.put("1", new ArrayList<PropertyAssessment>());
        costRange.put("2", new ArrayList<PropertyAssessment>());
        costRange.put("3", new ArrayList<PropertyAssessment>());
        costRange.put("4", new ArrayList<PropertyAssessment>());
        costRange.put("5", new ArrayList<PropertyAssessment>());
        costRange.put("6", new ArrayList<PropertyAssessment>());
        costRange.put("7", new ArrayList<PropertyAssessment>());
        properties.stream()
                .forEach(property -> {
                    int value = property.getAssessedValue();

                        if (value >= 2500000) {
                            costRange.get("7").add(property);
                        } else if (value >= 1000000) {
                            costRange.get("6").add(property);
                        } else if (value >= 750000) {
                            costRange.get("5").add(property);
                        } else if (value >= 500000) {
                            costRange.get("4").add(property);
                        } else if (value >= 250000) {
                            costRange.get("3").add(property);
                        } else if (value >= 100000) {
                            costRange.get("2").add(property);
                        } else if (value >= 0) {
                            costRange.get("1").add(property);
                        }

                });

    }

    @Override
    public void getCostOfList(HashMap<String, List<PropertyAssessment>> costRange, List<PropertyAssessment> addLst) {
        costRange.put("1", new ArrayList<PropertyAssessment>());
        costRange.put("2", new ArrayList<PropertyAssessment>());
        costRange.put("3", new ArrayList<PropertyAssessment>());
        costRange.put("4", new ArrayList<PropertyAssessment>());
        costRange.put("5", new ArrayList<PropertyAssessment>());
        costRange.put("6", new ArrayList<PropertyAssessment>());
        costRange.put("7", new ArrayList<PropertyAssessment>());
        addLst.stream()
                .forEach(property -> {
                    int value = property.getAssessedValue();

                    if (value >= 2500000) {
                        costRange.get("7").add(property);
                    } else if (value >= 1000000) {
                        costRange.get("6").add(property);
                    } else if (value >= 750000) {
                        costRange.get("5").add(property);
                    } else if (value >= 500000) {
                        costRange.get("4").add(property);
                    } else if (value >= 250000) {
                        costRange.get("3").add(property);
                    } else if (value >= 100000) {
                        costRange.get("2").add(property);
                    } else if (value >= 0) {
                        costRange.get("1").add(property);
                    }

                });
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

    @Override
    public HashMap<String, Integer> makeNeighbourhoodAssessments(String neighbourhood){
        String finalNeighbourhood = neighbourhood.toUpperCase();
        List<PropertyAssessment> filteredNeighbourhoods = new ArrayList<>();
        HashMap<String, Integer> assessmentsValues = new LinkedHashMap<>();

        filteredNeighbourhoods = this.getByNeighbourhood(neighbourhood);
        /*for (int i = 0; i < properties.size(); i++) { //loop through the list and if the neighbourhood matches, then put it in the new list
            if (properties.get(i).getNeighbourhood().getNeighbourhood().equalsIgnoreCase(neighbourhood)) {
                filteredNeighbourhoods.add(properties.get(i));
            }
        }*/

        int testN = 0; // a temp for the assessed value
        int maxN = filteredNeighbourhoods.get(0).getAssessedValue(); //sets the assessed value to maxN of the first property in the list
        for (int i = 1; i < filteredNeighbourhoods.size(); i++) { //start at the second property and if the value being checked is greater than the value saved, then
            testN = filteredNeighbourhoods.get(i).getAssessedValue();
            if (testN > maxN) {
                maxN = testN; //set it as the new max value
            }
        }

        int rangeBWNumbers = maxN/15;

        int count1st= 0, count2nd = 0, countThird = 0, countFourth = 0, countFifth = 0, countSixth = 0,
                countSeventh  = 0, countEight = 0, countNine = 0, countTen = 0, count11 = 0, count12 = 0,
                count13 = 0, count14 = 0, count15 = 0;

        for (int i = 0; i< filteredNeighbourhoods.size(); i++){
            if (filteredNeighbourhoods.get(i).getAssessedValue() <= rangeBWNumbers){
                count1st++;
            } else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 2 * rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() > rangeBWNumbers) {
                count2nd++;
            } else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 3 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() > 2 * rangeBWNumbers) {
                countThird++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 4 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >3 *rangeBWNumbers){
                countFourth++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 5 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >4 *rangeBWNumbers){
                countFifth++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 6 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >5 *rangeBWNumbers){
                countSixth++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 7 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >6 *rangeBWNumbers){
                countSeventh++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 8 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >7 *rangeBWNumbers){
                countEight++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 9 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >8 *rangeBWNumbers){
                countNine++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 10 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >9 *rangeBWNumbers){
                countTen++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 11 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >10 *rangeBWNumbers){
                count11++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 12 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >11 *rangeBWNumbers){
                count12++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 13 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >12 *rangeBWNumbers){
                count13++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() <= 14 *rangeBWNumbers && filteredNeighbourhoods.get(i).getAssessedValue() >13 *rangeBWNumbers){
                count14++;
            }
            else if (filteredNeighbourhoods.get(i).getAssessedValue() >14 *rangeBWNumbers){
                count15++;
            }
        }

        assessmentsValues.put("$0 - $" + String.valueOf(2*rangeBWNumbers),count1st);
        assessmentsValues.put("$" + String.valueOf(2*rangeBWNumbers)+ " - $" + String.valueOf(3*rangeBWNumbers), count2nd);
        assessmentsValues.put("$" + String.valueOf(3*rangeBWNumbers)+ " - $" + String.valueOf(4*rangeBWNumbers), countThird);
        assessmentsValues.put("$" + String.valueOf(4*rangeBWNumbers)+ " - $" + String.valueOf(5*rangeBWNumbers), countFourth);
        assessmentsValues.put("$" + String.valueOf(5*rangeBWNumbers)+ " - $" + String.valueOf(6*rangeBWNumbers), countFifth);
        assessmentsValues.put("$" + String.valueOf(6*rangeBWNumbers)+ " - $" + String.valueOf(7*rangeBWNumbers), countSixth);
        assessmentsValues.put("$" + String.valueOf(7*rangeBWNumbers)+ " - $" + String.valueOf(8*rangeBWNumbers), countSeventh);
        assessmentsValues.put("$" + String.valueOf(8*rangeBWNumbers)+ " - $" + String.valueOf(9*rangeBWNumbers), countEight);
        assessmentsValues.put("$" + String.valueOf(9*rangeBWNumbers)+ " - $" + String.valueOf(10*rangeBWNumbers), countNine);
        assessmentsValues.put("$" + String.valueOf(11*rangeBWNumbers)+ " - $" + String.valueOf(10*rangeBWNumbers), countTen);
        assessmentsValues.put("$" + String.valueOf(12*rangeBWNumbers)+ " - $" + String.valueOf(11*rangeBWNumbers), count11);
        assessmentsValues.put("$" + String.valueOf(13*rangeBWNumbers)+ " - $" + String.valueOf(12*rangeBWNumbers), count12);
        assessmentsValues.put("$" + String.valueOf(14*rangeBWNumbers)+ " - $" + String.valueOf(13*rangeBWNumbers), count13);
        assessmentsValues.put("$" + String.valueOf(15*rangeBWNumbers)+ " - $" + String.valueOf(14*rangeBWNumbers), count14);
        assessmentsValues.put(">$" + String.valueOf(15*rangeBWNumbers), count15);

        return assessmentsValues;
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
