import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            List<String> entry = new ArrayList<>( Arrays.asList(responseString.split(",", 18)) );

            return new PropertyAssessment(entry);

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public List<PropertyAssessment> getByNeighbourhood(String neighbourhood){
        neighbourhood = neighbourhood.toUpperCase();
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String url = endpoint + "?neighbourhood='" + neighbourhood + "'";
        url = url.replace(" ",  "+");

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
        assessmentClass = assessmentClass.toUpperCase();
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String url = endpoint + "?$where=mill_class_1 = '" + assessmentClass + "' OR mill_class_2 = '" + assessmentClass + "' OR mill_class_3 = '" + assessmentClass + "'";
        //csv?$where=mill_class_1 = 'FARMLAND' OR mill_class_2 = 'FARMLAND' or mill_class_3 = 'FARMLAND'

        url = url.replace(" ", "+");

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

    public List<PropertyAssessment> getByAssessmentClassLoop(String assessmentClass){
        assessmentClass = assessmentClass.toUpperCase();
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        List<PropertyAssessment> properties = new ArrayList<>();

        for (int j=1; j<4; j++) {
            String url = endpoint + "?mill_class_" + j + "=" + assessmentClass;

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


                for(String property : propList){
                    property = property.replace("\n", "");
                    List<String> entry = new ArrayList<>( Arrays.asList(property.split(",", 18)) );

                    if (entry.size() == 18){
                        properties.add(new PropertyAssessment(entry));
                    }
                }


            } catch (IOException | InterruptedException e) {
                return null;
            }

        }
        if (properties.size() > 0){
            return properties;
        } else {
            return null;
        }
    }

}
