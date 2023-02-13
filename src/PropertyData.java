import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyData {
    private final List<PropertyAssessment> properties;

    /*
     * PropertyData can be constructed with an ArrayList of PropertyAssessment objects so the user can perform the
     * computations on a list of objects of interest instead of all at once.
     * There are multiple functions in PropertyData that creates a list of PropertyAssessments to be given back to
     * PropertyData and computed. These functions include getPropertiesInClass, getPropertiesInNeighbourhood, and
     * getPropertiesInWard.
     *
     * Example Usage of getting data from a subset of properties in PropertyData:
     * neighbourhood = "sherwood park"
     * //get list of PropertyAssessments in given class
     * ArrayList<PropertyAssessment> properties = data.getPropertiesInNeighbourhood(neighbourhood);
     *   if (properties.size() > 0){
     *     PropertyData neighbourhoodData = new PropertyData(properties);
     *     int[] assessmentData = neighbourhoodData.getAssessmentValueData();
     *     //print the stats in the list
     *   } else {
     *     System.out.println("No data found for " + neighbourhood);
     *     System.out.println();
     *   }
     *
     */
    PropertyData(List<PropertyAssessment> properties){
        this.properties = properties;
    }

    /**
     * Constructor that constructs a PropertyData class from a given property assessment csv file
     * This constructor makes each line in the csv file into its own PropertyAssessment object and adds it to an
     * ArrayList of PropertyAssessments for later computation
     * @param csvFile a correctly formatted csv file of property assessments
     * @throws IOException if the csv file does not exist
     */
    PropertyData(String csvFile) throws IOException {
        // Create a stream object to read the CSV file
        BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile));

        //Create list of string lists to store data from file
        this.properties = new ArrayList<>();

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
            this.properties.add( new PropertyAssessment( Integer.parseInt("0" + entry.get(0)), entry.get(1), Integer.parseInt("0" + entry.get(2)), entry.get(3), entry.get(4).charAt(0), Integer.parseInt("0" + entry.get(5)), entry.get(6), entry.get(7), Integer.parseInt("0" + entry.get(8)), Double.parseDouble(entry.get(9)), Double.parseDouble(entry.get(10)), Double.parseDouble("0" + entry.get(12)), Double.parseDouble("0" + entry.get(13)), Double.parseDouble("0" + entry.get(14)), entry.get(15), entry.get(16), entry.get(17) ) );
        }
    }

    public List<PropertyAssessment> getProperties() {
        //REF: https://www.baeldung.com/java-copy-list-to-another
        // return a copy of the mutable object, not just the object
        return List.copyOf(properties);
    }

    /**
     * Gets all properties with a matching ward from all property data located in this object
     * @param ward a ward that exists in the data file
     * @return an ArrayList of PropertyAssessment objects
     */
    public List<PropertyAssessment> getPropertiesInWard(String ward){
        ArrayList<PropertyAssessment> propertiesInWard = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            if (property.getNeighbourhood().getWard().equalsIgnoreCase(ward)) {
                propertiesInWard.add(property);
            }
        }
        return propertiesInWard;
    }

    /**
     * returns a list of PropertyAssessment objects from this PropertyData object which have a given assessment class
     * @param assessmentClass the assessment class you want to get a list of properties in
     * @return a list of PropertyAssessments of properties in the assessment class
     */
    public List<PropertyAssessment> getPropertiesInClass(String assessmentClass){
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

    /**
     * Gets a list of all PropertyAssessments in a given neighbourhood
     * @param neighbourhood the neighbourhood to collect property information about
     * @return an arraylist of PropertyAssessments from the given neighbourhood
     */
    public List<PropertyAssessment> getPropertiesInNeighbourhood(String neighbourhood){
        List<PropertyAssessment> propertiesInNeighbourhood = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            if (property.getNeighbourhood().getNeighbourhood().equalsIgnoreCase(neighbourhood)) {
                propertiesInNeighbourhood.add(property);
            }
        }
        return propertiesInNeighbourhood;
    }

    /**
     * searches PropertyAssessments for given account number and returns the property if it exists
     * @param accountNo the account number of the property to search for
     * @return a PropertyAssessment of the property with the given account number (or null if it doesnt exist)
     */
    public PropertyAssessment getPropertyByAccount(String accountNo){
        for(PropertyAssessment property : getProperties()){
            if (Integer.toString(property.getAccountNumber()).equals(accountNo)) {
                return property;
            }
        }
        return null;
    }

    /**
     * Gets a list of all the wards of the properties stored in this PropertyData object
     * @return ArrayList<String> holding all the wards
     */
    public List<String> getWards(){
        List<String> wards = new ArrayList<>();
        for(PropertyAssessment property : getProperties()){
            String current = property.getNeighbourhood().getWard();
            if(!wards.contains(current)){
                wards.add(current);
            }
        }
        return wards;
    }

    /**
     * Gets the n, minimum, maximum, range, mean, and median of assessment values of the properties stored in this object
     * This is all done in one function to minimize number of times the data needs to be parsed
     * NOTE: the separate functions are also provided in case only specific data is required.
     * @return array of 6 values, [0] n, [1] min, [2] max, [3] range, [4] mean, [5] median
     */
    public int[] getAssessmentValueData(){
        int[] data = new int[6];
        data[0] = properties.size(); //get number of elements
        int[] values = new int[data[0]]; //list of all the costs (for median later)
        //initialize the min and max values
        data[1] = Integer.MAX_VALUE;
        data[2] = Integer.MIN_VALUE;
        long total = 0;
        int i = 0;

        for(PropertyAssessment property : getProperties()){
            int current = property.getAssessedValue();
            total += current; //add to total for later mean calc
            values[i] = current; //add to array for later median calc

            if(current < data[1]){
                data[1] = current;
            }
            if (current > data[2]) {
                data[2] = current;
            }
            i++;
        }

        //range
        data[3] = data[2] - data[1]; //min - max
        //mean
        data[4] = (int) (total / data[0]);
        //median
        //REF: https://stackoverflow.com/a/19852999
        Arrays.sort(values);
        int middle = data[0]/2;
        if (data[0]%2 == 1) //if there is an odd number of values
            data[5] = values[middle]; //take middle value
        else //if there is an even number of values
            data[5] = (values[middle-1] + values[middle]) / 2; //take the average of the two middle values

        return data;
    }

    public int getNum() {
        return (properties.size());
    }

    public int getMax() {
        int max = Integer.MIN_VALUE;

        for (PropertyAssessment property : getProperties()) {
            int current = property.getAssessedValue();

            if (max < current) {
                max = current;
            }
        }
        return max;
    }

    public int getMin() {
        int min = Integer.MAX_VALUE;

        for (PropertyAssessment property : getProperties()) {
            int current = property.getAssessedValue();

            if (min > current) {
                min = current;
            }
        }
        return min;
    }

    public int getRange() {
        return (getMax() - getMin());
    }

    public int getMean() {
        long total = 0;

        for (PropertyAssessment property : getProperties()) {
            int current = property.getAssessedValue();
            total += current;
        }

        return ( (int) (total / getNum()) );
    }

    public int getMedian() {
        int[] values = new int[getNum()];
        int i = 0;

        for (PropertyAssessment property : getProperties()) {
            int current = property.getAssessedValue();
            values[i] = current;
            i++;
        }

        Arrays.sort(values);
        //REF: https://stackoverflow.com/a/19852999
        Arrays.sort(values);
        int middle = getNum() / 2;
        if (getNum() % 2 == 1) //if there is an odd number of values
            return (values[middle]); //take middle value
        else //if there is an even number of values
            return ((values[middle - 1] + values[middle]) / 2); //take the average of the two middle values
    }





}
