import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainAyesha {
    public static void main(String[] args) throws IOException {
        //List<BusStops> stopsList = new ArrayList<>();
        
        //stopsList = ReadBusStops.readCSV();
        //System.out.println(stopsList);

        PropertyAssessmentDAO dao = new CsvPropertyAssessmentDAO();

        HashMap<String,Integer> he = dao.makeNeighbourhoodAssessments("Granville");
        System.out.println(he);
        //List<String> hi = dao.getNeighbourhoodLists();
        //System.out.println(hi);
    }
}
