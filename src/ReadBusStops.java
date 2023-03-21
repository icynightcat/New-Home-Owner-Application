import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadBusStops {

    public static List<BusStops> readCSV() throws IOException {
        String fileName = "stops.txt";
        //BufferedReader myReader =
        List<BusStops> stopsList = new ArrayList<>();
        try {
            FileReader busAssesFile = new FileReader(fileName);
            BufferedReader myReader = new BufferedReader(busAssesFile);
            String temp = "";
            String[] arrays = null;
            myReader.readLine();
            while ((temp = myReader.readLine()) != null) { // as long as you still have a line, keep reading
                arrays = temp.split(",", 10); // will only read in 18 values after splitting with a comma
                int stopNum;
                stopNum = Integer.parseInt(arrays[0]);
                String stopName;
                stopName = arrays[2];

                BusStops busTemp = new BusStops(stopNum, arrays[2], Double.parseDouble(arrays[4]), Double.parseDouble(arrays[5])); // making a property assessment class

                stopsList.add(busTemp); // adding the property assessment class to the list
            }
            myReader.close(); //closing the reader after there are no more lines that need to be read in

        } catch (IOException e) { //if the file is not found, will give an error
            System.out.println("Error: File not read in\n");
            e.printStackTrace();
        }
        return stopsList;
    }

}
