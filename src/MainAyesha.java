import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAyesha {
    public static void main(String[] args) throws IOException {
        List<BusStops> stopsList = new ArrayList<>();
        
        stopsList = ReadBusStops.readCSV();
        System.out.println(stopsList);
    }
}
