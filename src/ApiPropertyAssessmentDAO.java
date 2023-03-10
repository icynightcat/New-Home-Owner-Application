import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO {

    /**
     * private helper method that returns a list of PropertyAssessments from data.edmonton.ca/resource/q7d6-ambg.csv
     * given a set of parameters to filter by. It is used by the search methods once the parameters are decided on
     * The parameters will also be correctly encoded if there are any. if there are no parameters, the first 1000
     * entries are returned
     * Note: Only works with correctly formatted query that does not already include a ?$where= parameter
     * @param params a correctly formatted query without ?$where= added
     * @return a list of PropertyAssessment objects that fit the search criteria
     */
    private List<PropertyAssessment> getListFrom(String params) {
        String url = "https://data.edmonton.ca/resource/q7d6-ambg.csv";

        //we want to encode the parameters (if there is any)
        if (params.length() > 0) {
            //first add where to the url
            url = url + "?$where=";
            //then encode the parameters and add them to the url
            params = URLEncoder.encode(params, StandardCharsets.UTF_8);
            url = url + params;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //get rid of all the extra quotes that were added
            String responseString = response.body().replace("\"", "");
            //get rid of the header
            responseString = responseString.replace("account_number,suite,house_number,street_name,garage,neighbourhood_id,neighbourhood,ward,assessed_value,latitude,longitude,point_location,tax_class_pct_1,tax_class_pct_2,tax_class_pct_3,mill_class_1,mill_class_2,mill_class_3", "");
            //split results into a list
            List<String> propList = new ArrayList<>(Arrays.asList(responseString.split("\n")));

            List<PropertyAssessment> properties = new ArrayList<>();

            //get every entry in the list and convert it to a PropertyAssessment object
            for (String property : propList) {
                property = property.replace("\n", "");
                List<String> entry = new ArrayList<>(Arrays.asList(property.split(",", 18)));

                if (entry.size() == 18) {
                    properties.add(new PropertyAssessment(entry));
                }
            }

            //return the list of PropertyAssessment objects
            return properties;

        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public List<PropertyAssessment> getProperties() {
        //empty url because we want no parameters
        String url = "";
        return getListFrom(url);

    }

    @Override
    public List<PropertyAssessment> advancedSearch(HashMap<String, String> searchCriteria) {
        //unload the hashmap
        String address = searchCriteria.get("address");
        String neighbourhood = searchCriteria.get("neighbourhood");
        String assessment = searchCriteria.get("assessment");
        String min = searchCriteria.get("min");
        String max = searchCriteria.get("max");

        String url = "";

        //address search
        if (address.length() > 0){
            address = address.toUpperCase();
            url = url + "(suite like '" + address + "%' OR house_number like '" + address + "%' OR street_name like '" + address + "%')";
        }

        //neighbourhood search
        if(neighbourhood.length() > 0){
            neighbourhood = neighbourhood.toUpperCase();
            if (address.length() > 0) {
                url = url + " and ";
            }
            url = url + "neighbourhood like'" + neighbourhood.toUpperCase() + "%'";
        }

        //assessment class search
        if(assessment.length() > 0){
            if(neighbourhood.length() > 0 || address.length() > 0){
                url = url + " and ";
            }
            assessment = assessment.toUpperCase();
            url = url +  "(mill_class_1 = '" + assessment + "' OR mill_class_2 = '" + assessment + "' OR mill_class_3 = '" + assessment + "')";

        }

        //value search
        if (min.length() > 0 || max.length() > 0){
            if(min.equals("")){
                min = "0";
            }
            if(max.equals("")){
                max = Integer.toString(MAX_VALUE);
            }
            if(neighbourhood.length() > 0 || address.length() > 0 || assessment.length() > 0){
                url = url + " and ";
            }
            url = url + "assessed_value between " + min + " and " + max;

        }

        return getListFrom(url);
    }

    @Override
    public List<PropertyAssessment> getByValue(String min, String max) {
        if(min.equals("")){
            min = "0";
        }
        if(max.equals("")){
            max = Integer.toString(MAX_VALUE);
        }
        String url = "assessed_value between " + min + " and " + max;

        return getListFrom(url);
    }

    @Override
    public List<String> getAssessmentClasses() {
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";
        String url = endpoint + "?$select=distinct+mill_class_1,+mill_class_2,+mill_class_3";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseString = response.body().replace("\"", "");
            responseString = responseString.replace("mill_class_1,mill_class_2,mill_class_3", "");
            responseString = responseString.replace(",", "\n");
            List<String> classes = new ArrayList<>(Arrays.asList(responseString.split("\n")));

            return classes.stream().distinct().sorted().toList();
        } catch (IOException | InterruptedException e) {
            return null;
        }

    }

    @Override
    public List<PropertyAssessment> getByAddress(String address) {
        address = address.toUpperCase();
        String url = "suite like '" + address + "%' OR house_number like '" + address + "%' OR street_name like '" + address + "%'";

        return getListFrom(url);
    }

    public PropertyAssessment getByAccountNumber(int accountNumber) {
        String endpoint = "https://data.edmonton.ca/resource/q7d6-ambg.csv";

        String url = endpoint + "?$where=account_number='" + accountNumber + "'";

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

            List<String> entry = new ArrayList<>(Arrays.asList(responseString.split(",", 18)));

            if (entry.size() == 18) {
                return new PropertyAssessment(entry);
            }

        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    public List<PropertyAssessment> getByNeighbourhood(String neighbourhood) {
        neighbourhood = neighbourhood.toUpperCase();
        String url = "neighbourhood like'" + neighbourhood + "%'";

        return getListFrom(url);
    }

    public List<PropertyAssessment> getByAssessmentClass(String assessmentClass) {
        assessmentClass = assessmentClass.toUpperCase();
        String url = "mill_class_1 = '" + assessmentClass + "' OR mill_class_2 = '" + assessmentClass + "' OR mill_class_3 = '" + assessmentClass + "'";

        return getListFrom(url);
    }
}
