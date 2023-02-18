import java.io.IOException;
import java.util.List;

public class Lab5Main {
    public static void main(String[] args){
        try {
            PropertyAssessmentDAO dao = new CsvPropertyAssessmentDAO();
            PropertyAssessmentDAO dao3 = new CsvPropertyAssessmentDAO("Property_Assessment_Data_2022");
            PropertyAssessment property = dao.getByAccountNumber(1103530);
            PropertyAssessment property2 = dao3.getByAccountNumber(1103530);
            System.out.println(property);
            System.out.println(property2);
        } catch (IOException e) {
            System.out.println("Failed to read ");
            System.exit(-1);
        }

        PropertyAssessmentDAO dao2 = new ApiPropertyAssessmentDAO();

        PropertyAssessment property = dao2.getByAccountNumber(1103530);
        System.out.println(property);

        List<PropertyAssessment> properties = dao2.getByNeighbourhood("DOWNTOWN");
        System.out.println(properties.get(0));

        List<PropertyAssessment> propertiesByClass = dao2.getByAssessmentClass("FARMLAND");
        System.out.println(propertiesByClass.get(0));
        System.out.println(propertiesByClass.get(propertiesByClass.size()-1));


    }

}
