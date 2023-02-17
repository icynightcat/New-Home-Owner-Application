import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO {


    public PropertyAssessment getByAccountNumber(int accountNumber){

        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String url = endpoint + "?account_number=" + accountNumber;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseString = response.body().replace("\"", "");
            responseString = responseString.replace("account_number,suite,house_number,street_name,garage,neighbourhood_id,neighbourhood,ward,assessed_value,latitude,longitude,point_location,tax_class_pct_1,tax_class_pct_2,tax_class_pct_3,mill_class_1,mill_class_2,mill_class_3", "");
            responseString = responseString.replace("\n", "");

            List<String> entry = new ArrayList<String>( Arrays.asList(responseString.split(",", 18)) );

            return new PropertyAssessment(entry);

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public List<PropertyAssessment> getByNeighbourhood(String neighbourhood){
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String url = endpoint + "?neighbourhood=" + neighbourhood;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseString = response.body().replace("\"", "");
            responseString = responseString.replace("account_number,suite,house_number,street_name,garage,neighbourhood_id,neighbourhood,ward,assessed_value,latitude,longitude,point_location,tax_class_pct_1,tax_class_pct_2,tax_class_pct_3,mill_class_1,mill_class_2,mill_class_3", "");
            //responseString = responseString.replace("\n", "");
            List<String> propList = new ArrayList<>(Arrays.asList(responseString.split("\n")));

            List<PropertyAssessment> properties = new ArrayList<>();

            for(String property : propList){
                property = property.replace("\n", "");
                List<String> entry = new ArrayList<>( Arrays.asList(property.split(",", 18)) );

                if (entry.size() == 18){
                    properties.add(new PropertyAssessment(entry));
                }
            }

            return properties;

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public List<PropertyAssessment> getByAssessmentClass(String assessmentClass){
        return null;
    }

}
