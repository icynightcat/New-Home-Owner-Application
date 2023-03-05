import java.beans.PropertyEditor;
import java.util.List;

public interface PropertyAssessmentDAO {

    /**
     * gets all data from the given source
     * @return all data received
     */
    List<PropertyAssessment> getProperties();


    /**
     * gets all properties from the data source with assessed values between the two given numbers
     * @param min minimum value
     * @param max maximum value
     * @return list of property assessments with assessed values between min and max
     */
    List<PropertyAssessment> getByValue(String min, String max);

    /**
     * gets all the assessment classes from given data source
     * @return list of all distinct assessment classes
     */
    List<String> getAssessmentClasses();


    /**
     * searches the dao of properties by a given address
     * @param address given address to search for, can be partial address
     * @return list of property assessments that have the address
     */
    List<PropertyAssessment> getByAddress(String address);

    /**
     * searches PropertyAssessments for given account number and returns the property if it exists
     * @param accountNumber the account number of the property to search for
     * @return a PropertyAssessment of the property with the given account number (or null if it doesn't exist)
     */
    PropertyAssessment getByAccountNumber(int accountNumber);

    /**
     * Gets a list of all PropertyAssessments in a given neighbourhood
     * @param neighbourhood the neighbourhood to collect property information about
     * @return an arraylist of PropertyAssessments from the given neighbourhood
     */
    List<PropertyAssessment> getByNeighbourhood(String neighbourhood);

    /**
     * returns a list of PropertyAssessment objects from this PropertyData object which have a given assessment class
     * @param assessmentClass the assessment class you want to get a list of properties in
     * @return a list of PropertyAssessments of properties in the assessment class
     */
    List<PropertyAssessment> getByAssessmentClass(String assessmentClass);


}
