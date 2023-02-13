import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * An example of how to read and process a simple CSV file.
 * This code uses only basic features of the Java language.
 * Feel free to modify this example by using a List (instead of array)
 * and other collections.
 */
public class Lab2Main {
    public static void main(String[] args) {
        //Input REF: https://www.w3schools.com/java/java_user_input.asp

        Scanner input = new Scanner(System.in);
        System.out.print("CSV filename: ");
        String csvFileName = input.nextLine().trim();

        //String csvFileName = "Property_Assessment_Data_2022.csv";


        //similar to try except statement from python
        try {
            System.out.println("Descriptive statistics of all property assessments");
            PropertyData newData = new PropertyData(csvFileName);
            int[] assessmentData = newData.getAssessmentValueData();
            printListInfo(assessmentData);

            System.out.print("Find a property assessment by account number: ");
            String acctNo = input.nextLine();
            assessmentByAccount(newData, acctNo);

            System.out.print("Neighbourhood: ");
            String neighbourhood = input.nextLine().strip();
            assessmentByNeighbourhood(newData, neighbourhood);

        } catch (IOException e) {
            System.out.println("Failed to read " + csvFileName);
            System.exit(-1);
        }
        input.close();

    }

    /**
     * Prints property assessment details for a given neighbourhood
     * @param data a PropertyData object holding respective property assessment data
     * @param neighbourhood a neighbourhood to print data about
     */
    public static void assessmentByNeighbourhood(PropertyData data, String neighbourhood){
        //get list of PropertyAssessments in given class
        List<PropertyAssessment> properties = data.getPropertiesInNeighbourhood(neighbourhood);
        if (properties.size() > 0){
            //create new PropertyData object with the previously acquired list of PropertyAssessments
            PropertyData neighbourhoodData = new PropertyData(properties);
            //use the new PropertyData object to get the stats of the properties
            int[] assessmentData = neighbourhoodData.getAssessmentValueData();
            //print the data
            printListInfo(assessmentData);
        } else {
            System.out.println("No data found for " + neighbourhood);
            System.out.println();
        }
    }

    /**
     * Prints out a property assessment for given account number
     * @param data a PropertyData object holding all respective properties
     * @param acctNo an account number to print assessment data for
     */
    public static void assessmentByAccount(PropertyData data, String acctNo){
        PropertyAssessment current = data.getPropertyByAccount(acctNo);
        if (current != null){
            System.out.println(current);
            System.out.println();
        } else {
            System.out.println("Error: invalid account number");
            System.out.println();
        }
    }

    /**
     * Prints out assessment data of all PropertyAssessment objects stored in a given PropertyData object
     * @param data object holding a list of PropertyAssessment objects
     */
    public static void printListInfo(int[] data){

        System.out.printf("n = %d%n", data[0]);
        System.out.printf("min = $%,d%n", data[1] );
        System.out.printf("max = $%,d%n", data[2] );
        System.out.printf("range = $%,d%n", data[3] );
        System.out.printf("mean = $%,d%n", data[4] );
        System.out.printf("median = $%,d%n", data[5] );
        System.out.println();
    }


}
