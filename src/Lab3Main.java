import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab3Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("CSV filename: ");
        String csvFileName = input.nextLine().trim();

        try{
            PropertyData data = new PropertyData(csvFileName);

            System.out.print("Assessment class: ");
            String assessmentClass = input.nextLine();

            assessmentByClass(data, assessmentClass);

        } catch (IOException e) {
            System.out.println("Failed to read " + csvFileName);
            System.exit(-1);
        }
    }

    public static void assessmentByClass(PropertyData data, String assessmentClass){
        //get list of PropertyAssessments in given class
        List<PropertyAssessment> propertiesInClass = data.getPropertiesInClass(assessmentClass);
        if (propertiesInClass.size() > 0){
            System.out.println("Statistics (assessment class = " + assessmentClass + ")");
            //create new PropertyData object with the previously acquired list of PropertyAssessments
            PropertyData classData = new PropertyData(propertiesInClass);
            //use the new PropertyData object to get the stats of the properties
            int[] assessmentData = classData.getAssessmentValueData();
            //print the data
            printListInfo(assessmentData);
        } else {
            System.out.println("No data found for assessment class " + assessmentClass);
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


