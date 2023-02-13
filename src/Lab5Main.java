import java.io.IOException;

public class Lab5Main {
    public static void main(String[] args){
        try {
            PropertyAssessmentDAO dao = new CsvPropertyAssessmentDAO();

            PropertyAssessment property = dao.getByAccountNumber(1103530);
            System.out.println(property);
        } catch (IOException e) {
            System.out.println("Failed to read ");
            System.exit(-1);
        }

    }

}
