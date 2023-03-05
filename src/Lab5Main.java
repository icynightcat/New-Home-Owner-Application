import java.io.IOException;
import java.util.List;

public class Lab5Main {
    public static void main(String[] args){

        try {
            PropertyAssessmentDAO dao = new CsvPropertyAssessmentDAO();
            PropertyAssessment property = dao.getByAccountNumber(1066158);
            System.out.println(property);
        } catch (IOException e) {
            System.out.println("Failed to read ");
            System.exit(-1);
        }

        PropertyAssessmentDAO dao2 = new ApiPropertyAssessmentDAO();

        System.out.println(dao2.getAssessmentClasses());

        PropertyAssessment property = dao2.getByAccountNumber(1066158);
        System.out.println(property);

        List<PropertyAssessment> properties = dao2.getByNeighbourhood("jamieson place");
        System.out.println(properties.get(0));

        List<PropertyAssessment> propertiesByClass = dao2.getByAssessmentClass("FARMLAND");
        System.out.println(propertiesByClass.get(0));
        System.out.println(propertiesByClass.get(propertiesByClass.size()-1));


    }

}
