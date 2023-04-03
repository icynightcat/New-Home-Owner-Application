import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;


public class UserInterface extends Application {

    PropertyAssessmentDAO dao = null;

    String source = "n/a";

    String chosenNeighbourhood;

    BarChart<String,Integer> bc;

    final Stage[] legendWindow = new Stage[1];







    @Override
    public void start(final Stage primaryStage) {

        //create list for legend labels
        List<LegendLabel> legendLabels = new ArrayList<>();
        legendWindow[0] = spawnLegend(primaryStage, legendLabels);
        legendWindow[0].close();

        try {
            dao = new CsvPropertyAssessmentDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //filling legend label for the colour gradient
        List<Color> costColors = new ArrayList<>();
        costColors.add(Color.rgb(128, 8, 0));
        costColors.add(Color.rgb(255, 16, 0));
        costColors.add(Color.rgb(250, 101, 15));
        costColors.add(Color.rgb(255, 221, 0));
        costColors.add(Color.rgb(132, 255, 0));
        costColors.add(Color.rgb(16, 247, 0));
        costColors.add(Color.rgb(0, 112, 4));
        String[] ranges = {"$0 - $100,000", "$100,000 - $250,000", "$250,000 - $500,000", "$500,000 - $750,000",
                "$750,000 - $1,000,000", "$1,000,000 - $2,500,000", "$2,500,000+"};

        int zz = 0;
        for (Color color : costColors) {
            legendLabels.add(new LegendLabel(ranges[zz], color));
            zz++;
        }


        //table button
        Button tableButton = new Button("Open Table");
        tableButton.setPrefWidth(200);
        tableButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        tableButton.setLayoutX(10);
        tableButton.setLayoutY(560);

        //stats button
        Button statButton = new Button("Get Statistics");
        statButton.setPrefWidth(200);
        statButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        statButton.setLayoutX(10);
        statButton.setLayoutY(525);


        //map stuff
        //create map ---------------------------------------------------------------------------------------------------
        String apiKey = "AAPK9bcef3a7d7e94479aa9e61bc556247083Yorp1DmW9m8Ds1n71r4wSLxUXs8REDU0p_iQeGIhIuUGoZAdoXWroq2OXgjF3L-";
        //String apiKey = "AAPK2d72f63e78bf4080b08944c50bf8ad75ruhLpRbSL4KyC3vqDiqShUvlC6CbTgAisL18IPcVL07mroGcMAjoBDY_wru-ygRe";
       // ArcGISRuntimeEnvironment.setInstallDirectory("C:\\Users\\ayesh\\Desktop\\arcgis-maps-sdk-java-200.0.0");
        ArcGISRuntimeEnvironment.setApiKey(apiKey);


        //create mapView and map
        MapView mapView = new MapView();
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);

        //set map to mapview
        mapView.setMap(map);
        //set viewpoint to edmonton (third variable is camera height kinda)
        //longitude is negative for West
        mapView.setViewpoint(new Viewpoint(53.54, -113.4937, 275000));

        //create graphics overlay
        //REF: https://developers.arcgis.com/java/maps-2d/tutorials/add-a-point-line-and-polygon/
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);


        //add separator to split find and select
        Separator separator1 = new Separator();


        //add Neighbourhood drop down
        Label labelNei = new Label("Neighbourhood:");
        labelNei.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-width: 1.5px;" );
        labelNei.setLayoutX(10);
        labelNei.setLayoutY(130);

        ComboBox<String> neightbourDropDown = new ComboBox<>();
        neightbourDropDown.setItems(FXCollections.observableList(dao.getNeighbourhoodLists()));
        neightbourDropDown.setPrefWidth(200);
        neightbourDropDown.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        neightbourDropDown.setLayoutX(10);
        neightbourDropDown.setLayoutY(150);


        //add assessment class drop down
        Label labelAsCla = new Label("Assessment Class:");
        labelAsCla.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-width: 1.5px;" );
        labelAsCla.setLayoutX(10);
        labelAsCla.setLayoutY(80);

        ComboBox<String> assessmentClassDropDown = new ComboBox<>();
        assessmentClassDropDown.setItems(FXCollections.observableList(dao.getAssessmentClasses()));
        assessmentClassDropDown.setPrefWidth(200);
        assessmentClassDropDown.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        assessmentClassDropDown.setLayoutX(10);
        assessmentClassDropDown.setLayoutY(100);


        //add ward drop down
        Label labelwar = new Label("Ward:");
        labelwar.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-width: 1.5px;" );
        labelwar.setLayoutX(10);
        labelwar.setLayoutY(180);

        ComboBox<String> wardDropDown = new ComboBox<>();
        wardDropDown.setItems(FXCollections.observableList(dao.getWards()));
        wardDropDown.setPrefWidth(200);
        wardDropDown.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        wardDropDown.setLayoutX(10);
        wardDropDown.setLayoutY(200);


        Button priceDropDown = new Button("Show All");
        priceDropDown.setPrefWidth(200);
        priceDropDown.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        priceDropDown.setLayoutX(10);
        priceDropDown.setLayoutY(280);


        //buttons for all search
        Button busStops = new Button("Show Bus Stops");
        busStops.setPrefWidth(200);
        busStops.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        busStops.setLayoutX(10);
        busStops.setLayoutY(340);

        //button for clearing the dots
        Button mapResetButton = new Button("Clear Map");
        mapResetButton.setPrefWidth(80);
        mapResetButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        mapResetButton.setLayoutX(240);
        mapResetButton.setLayoutY(560);


        //Assessed Value Range label
        final Label labelValue = new Label("Assessed Value Range:");
        labelValue.setFont(Font.font("Times", 15));

        /**
        //Two boxes for min and max value
        MinField = new TextField(); //min box
        MinField.setPromptText("Min Value");
        MaxField = new TextField(); //max box
        MaxField.setPromptText("Max Value");
        hBox1.setHgrow(MinField, Priority.ALWAYS);
        MinField.setMaxWidth(Double.MAX_VALUE);
        hBox1.setHgrow(MaxField, Priority.ALWAYS);
        MaxField.setMaxWidth(Double.MAX_VALUE);
        //add the text fields to the hbox
        hBox1.getChildren().addAll(MinField, MaxField);
        */

        //buttons for search and reset
        Button SearchButton = new Button("Search Single Choice"); //This is just slightly bigger
        SearchButton.setPrefWidth(200);
        SearchButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
        SearchButton.setLayoutX(10);
        SearchButton.setLayoutY(10);


        priceDropDown.setOnAction(event -> {
            graphicsOverlay.getGraphics().clear();
            legendWindow[0].close();

            if(neightbourDropDown.getValue() != null) {
                neightbourDropDown.getSelectionModel().clearSelection();
            }
            if(assessmentClassDropDown.getValue() != null) {
                assessmentClassDropDown.getSelectionModel().clearSelection();
            }
            if(wardDropDown.getValue() != null) {
                wardDropDown.getSelectionModel().clearSelection();
            }
            HashMap<String, List<PropertyAssessment>> costRange = new HashMap<>();

            dao.getCostRange(costRange);

            int i = 0;
            for( String key : costRange.keySet()){
                graphicsOverlay.getGraphics().addAll(getOverlayForProps(costRange.get(key), costColors.get(i)));
                i = i + 1;
            }

            legendWindow[0] = spawnLegend(primaryStage, legendLabels);
            //add the colors to the legend

        });

        SearchButton.setOnAction(event -> {
            legendWindow[0].close();
            graphicsOverlay.getGraphics().clear();
            String nei = neightbourDropDown.getValue();
            String asses = assessmentClassDropDown.getValue();
            String ward = wardDropDown.getValue();


            if(nei != null){
                List<PropertyAssessment> somethingCool = new ArrayList<>();
                HashMap<String, List<PropertyAssessment>> costRange = new HashMap<>();
                somethingCool = dao.getByNeighbourhood(nei);
                dao.getCostOfList(costRange, somethingCool);
                int i = 0;
                for( String key : costRange.keySet()){
                    graphicsOverlay.getGraphics().addAll(getOverlayForProps(costRange.get(key), costColors.get(i)));
                    i = i + 1;
                }
                legendWindow[0] = spawnLegend(primaryStage, legendLabels);
            } else if (asses != null) {
                List<PropertyAssessment> somethingCool = new ArrayList<>();
                HashMap<String, List<PropertyAssessment>> costRange = new HashMap<>();
                somethingCool = dao.getByAssessmentClass(asses);
                dao.getCostOfList(costRange, somethingCool);
                int i = 0;
                for( String key : costRange.keySet()){
                    graphicsOverlay.getGraphics().addAll(getOverlayForProps(costRange.get(key), costColors.get(i)));
                    i = i + 1;
                }
                legendWindow[0] = spawnLegend(primaryStage, legendLabels);
            } else if (ward != null) {
                List<PropertyAssessment> somethingCool = new ArrayList<>();
                HashMap<String, List<PropertyAssessment>> costRange = new HashMap<>();
                somethingCool = dao.getPropertiesInWard(ward);
                dao.getCostOfList(costRange, somethingCool);
                int i = 0;
                for( String key : costRange.keySet()){
                    graphicsOverlay.getGraphics().addAll(getOverlayForProps(costRange.get(key), costColors.get(i)));
                    i = i + 1;
                }
                legendWindow[0] = spawnLegend(primaryStage, legendLabels);

            }  //money
        });

        neightbourDropDown.setOnMouseClicked(actionEvent -> {
            if(assessmentClassDropDown.getValue() != null) {
                assessmentClassDropDown.getSelectionModel().clearSelection();
            }
            if(wardDropDown.getValue() != null) {
                wardDropDown.getSelectionModel().clearSelection();
            }
        });
        assessmentClassDropDown.setOnMouseClicked(actionEvent -> {
            if(neightbourDropDown.getValue() != null) {
                neightbourDropDown.getSelectionModel().clearSelection();
            }
            if(wardDropDown.getValue() != null) {
                wardDropDown.getSelectionModel().clearSelection();
            }

        });
        wardDropDown.setOnMouseClicked(actionEvent -> {
            if(neightbourDropDown.getValue() != null) {
                neightbourDropDown.getSelectionModel().clearSelection();
            }
            if(assessmentClassDropDown.getValue() != null) {
                assessmentClassDropDown.getSelectionModel().clearSelection();
            }
        });

        busStops.setOnAction(event -> {
            try {
                List<BusStops> busStop = ReadBusStops.readCSV();
                graphicsOverlay.getGraphics().addAll(getOverlayForBus(busStop, Color.BLUE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mapResetButton.setOnAction(event -> {
            graphicsOverlay.getGraphics().clear();
            legendWindow[0].close();
        });


        statButton.setOnAction(event -> {
            //Initialize window
            BorderPane thirdLayout = new BorderPane();
            Scene thirdScene = new Scene(thirdLayout, 1100, 600);
            Stage newWindow2 = new Stage();
            newWindow2.setTitle("Statistics Window");
            thirdLayout.setStyle("-fx-background-color: #F8F8FF;");
            newWindow2.setScene(thirdScene);

            // Specifies the modality for new window.
            newWindow2.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            newWindow2.initOwner(primaryStage);

            // Set position of second window, related to primary window.
            newWindow2.setX(primaryStage.getX() + 50);
            newWindow2.setY(primaryStage.getY() + 25);

            //making the side stuff
            thirdLayout.setLeft(addVBox2(newWindow2));

            //making the bar chart
            thirdLayout.setCenter(addVBox());

            //showing bus stops

            // show window
            newWindow2.show();

        });
        //open table window
        tableButton.setOnAction(event -> {

            BorderPane secondaryLayout = new BorderPane();
            Scene secondScene = new Scene(secondaryLayout, 1100, 600);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Table Search");
            secondaryLayout.setStyle("-fx-background-color: #F8F8FF;");
            newWindow.setScene(secondScene);

            // Specifies the modality for new window.
            newWindow.initModality(Modality.WINDOW_MODAL);

            // Specifies the owner Window (parent) for new window
            newWindow.initOwner(primaryStage);

            // Set position of second window, related to primary window.
            newWindow.setX(primaryStage.getX() + 50);
            newWindow.setY(primaryStage.getY() + 25);

            VBox menu = new VBox();
            menu.setSpacing(5.0);
            menu.setPadding(new Insets(5, 5, 5, 5));
            menu.setPrefWidth(220);
            //REF: https://www.tabnine.com/code/java/methods/javafx.scene.layout.Pane/setBorder
            menu.setBorder(new Border(new BorderStroke(Color.DIMGREY, BorderStrokeStyle.SOLID, null, null)));
            //menu.setStyle("-fx-background-color: #ffd68a;");

            Separator separator = new Separator();

            // Labels
            Font roboto = Font.font("Roboto", FontWeight.BOLD, 16);
            Font smallboto = new Font("Roboto", 14);
            Label sourceLabel = new Label("Select Data Source");
            sourceLabel.setFont(roboto);
            sourceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label paLabel = new Label("Find Property Assessment");
            paLabel.setFont(roboto);
            paLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label accNoLabel = new Label("Account Number:");
            accNoLabel.setFont(smallboto);
            accNoLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label addressLabel = new Label("Address:");
            addressLabel.setFont(smallboto);
            addressLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label neighbourhoodLabel = new Label("Neighbourhood:");
            neighbourhoodLabel.setFont(smallboto);
            neighbourhoodLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label assessClassLabel = new Label("Assessment Class:");
            assessClassLabel.setFont(smallboto);
            assessClassLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            Label assessValueLabel = new Label("Assessed Value Range:");
            assessValueLabel.setFont(smallboto);
            assessValueLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");

            // Create combo boxes
            String[] sources = {"CSV File", "Edmonton's Open Data Portal"};
            ComboBox<String> sourceSelectBox = new ComboBox<>(FXCollections.observableArrayList(sources));
            sourceSelectBox.setPrefWidth(200);
            sourceSelectBox.setStyle("-fx-text-fill: #540054; -fx-border-width: 1.5px;" );
            //automatically fills after source is loaded
            ComboBox<String> classSelectBox = new ComboBox<>();
            classSelectBox.setPrefWidth(150);
            classSelectBox.setStyle("-fx-text-fill: #540054; -fx-border-width: 1.5px;" );

            //create input boxes
            TextField acctNoField = new TextField();
            TextField addressField = new TextField();
            addressField.setPromptText("(#suite #house street)");
            TextField neighbourhoodField = new TextField();
            TextField minValueField = new TextField();
            minValueField.setPromptText("Min Value");
            minValueField.setPrefWidth(95);
            TextField maxValueField = new TextField();
            maxValueField.setPromptText("Max value");
            maxValueField.setPrefWidth(95);

            //create buttons
            Button readDataButton = new Button("Read Data");
            readDataButton.setPrefWidth(200);
            readDataButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
            Button searchButton = new Button("Search");
            searchButton.setPrefWidth(95);
            searchButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054; -fx-border-color: #540054; -fx-border-width: 1.5px;" );
            Button resetButton = new Button("Reset");
            resetButton.setPrefWidth(95);
            resetButton.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054; -fx-border-color: #540054; -fx-border-width: 1.5px;" );

            //create flow pane for data source
            VBox dataSource = new VBox();
            dataSource.setPrefWidth(180);
            dataSource.setSpacing(5.0);
            dataSource.setPadding(new Insets(1, 1, 1, 1));
            dataSource.setBorder(new Border(new BorderStroke(Color.rgb(200, 200, 200), BorderStrokeStyle.SOLID, null, null)));
            dataSource.getChildren().addAll(sourceLabel, sourceSelectBox, readDataButton);

            //create flow pane for the simple/advanced search
            VBox searchPane = new VBox();
            searchPane.setPrefWidth(180);
            searchPane.setSpacing(5.0);
            searchPane.setPadding(new Insets(1, 1, 1, 1));
            //searchPane.setBorder(new Border(new BorderStroke(Color.rgb(200, 200, 200), BorderStrokeStyle.SOLID, null, null)));
            searchPane.getChildren().addAll(paLabel, accNoLabel, acctNoField,
                    addressLabel, addressField, neighbourhoodLabel,
                    neighbourhoodField, assessClassLabel, classSelectBox);

            //create flow pane for the assessed value search
            FlowPane valueSearchPane = new FlowPane();
            valueSearchPane.setHgap(10);
            valueSearchPane.setPrefWidth(180);
            valueSearchPane.getChildren().addAll(assessValueLabel, minValueField, maxValueField, searchButton, resetButton);
            //valueSearchPane.setBorder(new Border(new BorderStroke(Color.rgb(200, 200, 200), BorderStrokeStyle.SOLID, null, null)));
            valueSearchPane.setPadding(new Insets(1, 1, 1, 1));

            //add everything to the left menu
            menu.getChildren().addAll(dataSource, separator, searchPane, valueSearchPane);
            secondaryLayout.setLeft(menu);

            //create list to display property assessments
            ObservableList<PropertyAssessment> propertyDisplay = observableArrayList();

            VBox center = new VBox();
            Label title = new Label("Edmonton Property Assessments");
            title.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
            title.setFont(roboto);
            center.getChildren().add(title);

            TableView<PropertyAssessment> table = makeTable(primaryStage, propertyDisplay);
            center.getChildren().add(table);
            secondaryLayout.setCenter(center);

            //READ BUTTON ACTION
            readDataButton.setOnAction(actionEvent -> {
                if (!sourceSelectBox.getSelectionModel().isEmpty()) {
                    String choice = sourceSelectBox.getValue();
                    if (choice.equals(sources[0])) {
                        //set source global variable to CSV if they chose csv
                        source = "CSV";
                        try {
                            //then set dao to use CsvDAO
                            dao = new CsvPropertyAssessmentDAO("Property_Assessment_Data_2022.csv");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        List<PropertyAssessment> properties = dao.getProperties();
                        propertyDisplay.clear();
                        propertyDisplay.addAll(properties);

                    } else if (choice.equals(sources[1])) {
                        //set source global variable to API if they chose edmonton open data portal
                        source = "API";
                        //otherwise set DAO to use ApiDao
                        dao = new ApiPropertyAssessmentDAO();
                        List<PropertyAssessment> properties = dao.getProperties();
                        propertyDisplay.clear();
                        propertyDisplay.addAll(properties);
                    }
                    //get assessment classes to add to the dropdown
                    List<String> list = dao.getAssessmentClasses();
                    classSelectBox.setItems(FXCollections.observableList(list));
                    //NOTE: the blank item is intended so that users can select no assessment class without needing to reset
                } else {
                    showAlert("No source selected!");
                }
            });

            //reset button event
            resetButton.setOnAction(actionEvent -> {
                //empty all fields
                acctNoField.setText("");
                addressField.setText("");
                neighbourhoodField.setText("");
                classSelectBox.getSelectionModel().clearSelection();
                minValueField.setText("");
                maxValueField.setText("");
                //reset properties in table
                List<PropertyAssessment> properties = dao.getProperties();
                propertyDisplay.clear();
                propertyDisplay.addAll(properties);
            });

            //search button event
            searchButton.setDefaultButton(true); //allows search by clicking enter
            searchButton.setOnAction(actionEvent -> {
                List<PropertyAssessment> properties = new ArrayList<>();
                String acctNo = acctNoField.getText();
                String address = addressField.getText();
                String neighbourhood = neighbourhoodField.getText();
                String assessment = classSelectBox.getValue();
                //if value is null set it to an empty string so I can use it the same as the other fields
                if (assessment == null) {
                    assessment = "";
                }
                String minVal = minValueField.getText();
                String maxVal = maxValueField.getText();

                //if a dao has not been selected yet
                if (source.equals("n/a")) {
                    showAlert("Select data source first!");
                    return;
                }

                //if there is nothing in any field
                if (acctNo.length() == 0 && address.length() == 0 && neighbourhood.length() == 0 && assessment.length() == 0 && (minVal.length() == 0 && maxVal.length() == 0)) {
                    //get all properties
                    properties = dao.getProperties();
                }

                //if there is anything in the account number field
                if (!acctNo.equals("")) {
                    //just search by that only, regardless of if there is something in other fields (makes more sense to me)
                    try {
                        PropertyAssessment prop = dao.getByAccountNumber(Integer.parseInt(acctNo));
                        if (prop != null) {
                            properties.add(prop);
                        }
                    } catch (NumberFormatException e) {
                        properties = new ArrayList<>();
                    }
                }
                //if only address is filled
                else if (address.length() > 0 && neighbourhood.length() == 0 && assessment.length() == 0 && (minVal.length() == 0 && maxVal.length() == 0)) {
                    properties = dao.getByAddress(address);
                }
                //else if only neighbourhood is filled
                else if (address.length() == 0 && neighbourhood.length() > 0 && assessment.length() == 0 && (minVal.length() == 0 && maxVal.length() == 0)) {
                    properties = dao.getByNeighbourhood(neighbourhood);
                }
                //else if only assessment class is filled
                else if (address.length() == 0 && neighbourhood.length() == 0 && assessment.length() > 0 && (minVal.length() == 0 && maxVal.length() == 0)) {
                    properties = dao.getByAssessmentClass(assessment);
                }
                //else if only one of the value ranges are filled
                else if (address.length() == 0 && neighbourhood.length() == 0 && assessment.length() == 0 && (minVal.length() > 0 || maxVal.length() > 0)) {
                    properties = dao.getByValue(minVal, maxVal);
                } else {
                    //this means that there are multiple search terms filled
                    //I realize that I can use this for every search now that I made it, but I made it last
                    //The other ones are still good to have for when there is only one search criteria, as they are more
                    //optimized, no reason to do an advanced search if we don't need to...
                    //make a hashmap of search criteria to send to advanced search method
                    HashMap<String, String> searchCriteria = new HashMap<>();
                    searchCriteria.put("address", address);
                    searchCriteria.put("neighbourhood", neighbourhood);
                    searchCriteria.put("assessment", assessment);
                    searchCriteria.put("min", minVal);
                    searchCriteria.put("max", maxVal);

                    properties = dao.advancedSearch(searchCriteria);
                }

                //clear the previous properties out of the table
                propertyDisplay.clear();
                //check if any properties were returned by the searches
                if (properties.size() == 0) {
                    showAlert("No properties fit the search criteria.");
                } else {
                    //otherwise add the found properties to the table
                    propertyDisplay.addAll(properties);
                }

            });

            newWindow.show();
        });

        Pane root = new Pane();

        mapView.setMinSize(1025, 589);
        mapView.setLayoutX(215);
        mapView.setLayoutY(2);

        //adding to window
        root.getChildren().add(tableButton);
        root.getChildren().add(statButton);
        root.getChildren().add(mapView);
        root.getChildren().add(assessmentClassDropDown);
        root.getChildren().add(neightbourDropDown);
        root.getChildren().add(SearchButton);
        root.getChildren().add(labelNei);
        root.getChildren().add(labelAsCla);
        root.getChildren().add(labelwar);
        root.getChildren().add(wardDropDown);
        root.getChildren().add(priceDropDown);
        root.getChildren().addAll(busStops, mapResetButton);


        Scene scene = new Scene(root, 1250, 600);

        primaryStage.setTitle("Map");
        primaryStage.setScene(scene);
        root.setStyle("-fx-background-color: #F8F8FF;");
        primaryStage.show();

        /*
        //Example for the legend usage -----------------------------

        //this is how I have to initialize it, so I can access it in a button (idk its weird)
        legendWindow[0] = spawnLegend(primaryStage, legendLabels);
        //call legendWindow.close() before changing the legend and making a new one
        Button closeLegendButton = new Button("Close Legend");
        closeLegendButton.setLayoutY(330);
        closeLegendButton.setLayoutX(1100);
        root.getChildren().add(closeLegendButton);
        closeLegendButton.setOnAction(event -> {
            //you have to use it kinda weird to use it in the button
            legendWindow[0].close();

            legendWindow[0] = spawnLegend(primaryStage, Arrays.asList(new LegendLabel("new", Color.PINK)));
        } );
        //Example for the legend usage -----------------------------

        //testing adding properties and add bus to map
        */

        /*
        try {
            dao = new CsvPropertyAssessmentDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Random rand = new Random();

        for (String assClass : dao.getAssessmentClasses()){
            List<PropertyAssessment> props = dao.getByAssessmentClass(assClass);

            //function that takes a list of property assessments and a colour and adds them as points to a graphics overlay
            graphicsOverlay.getGraphics().addAll(getOverlayForProps(props, Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255))));
        }

        try {
            List<BusStops> busStops = ReadBusStops.readCSV();
            graphicsOverlay.getGraphics().addAll(getOverlayForBus(busStops, Color.ORANGE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

         */
        //end testing


    }

    /**
     * This creates bar chart for the Edmonton Neighbourhood picked
     * @return a v box with a bar chart
     */
    private Node addVBox() {
        VBox bar = new VBox();
        bar.setPadding(new Insets(10));
        bar.setSpacing(8);
        bar.setAlignment(Pos.CENTER);
        //bar.setStyle("-fx-background-color: #D8BFD8;"); //thistle

        //this creates title for bar graph
        Label chartTitle = new Label("Assessment Values in Neighbourhood");
        chartTitle.setFont(new Font("Arial", 15));
        chartTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
        bar.getChildren().add(chartTitle);

        //x axis
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Assessment Values");
        x.setAnimated(false);
        //y axis
        NumberAxis y = new NumberAxis();
        y.setLabel("Count");

        //creating bar chart
        bc = new BarChart(x, y);
        bc.setMaxHeight(550);

        //adding bar chart to vbox
        bar.getChildren().add(bc);

        return bar;
    }

    /**
     * This creates a list view for Edmonton Neighbourhoods and adds data to bar graph
     * @param newWindow2 the screen you want to create the listview on
     * @return a v box with a list view
     */
    private Node addVBox2(Stage newWindow2) {
        ObservableList<String> neighbourhoodDisplay;

        VBox menu = new VBox();
        menu.setPadding(new Insets(15));
        menu.setSpacing(8);

        //making list view of neighbourhoods
        Label tableTitle = new Label("Edmonton Neighbourhoods");
        tableTitle.setFont(new Font("Arial", 15));
        tableTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #540054");
        menu.getChildren().add(tableTitle);

        //making the list view
        List<String> neighbourhoods = new ArrayList<String>();

        try {
            dao = new CsvPropertyAssessmentDAO("Property_Assessment_Data_2022.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> properties = dao.getNeighbourhoodLists();
        neighbourhoodDisplay = observableArrayList(properties);

        final ListView listView = new ListView(neighbourhoodDisplay);
        listView.setPrefSize(200, 500);
        listView.setEditable(true);

        menu.getChildren().add(listView);

        //send neighbourhood button
        Button submit = new Button("Check Neighbourhood");
        submit.setMaxWidth(Double.MAX_VALUE);
        submit.setFont(new Font("Arial",12));
        submit.setStyle("-fx-font-weight: bold; -fx-text-fill: #483D8B; -fx-border-color: #483D8B; -fx-border-width: 1.5px;" );
        menu.getChildren().add(submit);

        EventHandler<ActionEvent> showNeighbourhoods =
                E -> {
                    Object selectedItem = listView.getSelectionModel().getSelectedItem();
                    chosenNeighbourhood = selectedItem.toString();
                    System.out.println(chosenNeighbourhood);

                    HashMap<String, Integer> assessmentsValues;

                    bc.getData().clear();
                    bc.layout();
                    XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
                    assessmentsValues = dao.makeNeighbourhoodAssessments(chosenNeighbourhood);
                    for (String i : assessmentsValues.keySet()){
                        String ranges = i;
                        int value = assessmentsValues.get(i);
                        if (value!= 0) {
                            XYChart.Data<String, Integer> b = new XYChart.Data<>(ranges, value);
                            Label cx = new Label(String.valueOf(value));
                            series1.getData().add(b);
                            //series1.setName(String.valueOf(value));
                        }
                    }
                    bc.getData().add(series1);
                };

        submit.setOnAction(showNeighbourhoods);

        return menu;
    }

    /**
     * given a list of LegendLabels, this creates a new screen in the bottom left corner of the current one that shows
     * the legend created from the LegendLabels
     * @param primaryStage the screen you want to create the legend on top of
     * @param labels A list of LegendLabel objects which hold the name and colour of each legend entry
     * @return the stage that was created so that it can be closed when needed
     */
    private Stage spawnLegend(Stage primaryStage, List<LegendLabel> labels){
        GridPane legendPane = new GridPane();
        Scene legend = new Scene(legendPane, 200, 250);
        Font roboto = Font.font("Roboto", FontWeight.BOLD, 15);

        //set properties of the GridPane
        legendPane.setHgap(5);
        legendPane.setVgap(5);
        legendPane.setPadding(new Insets(5, 5, 5, 5));

        // New window (Stage)
        Stage legendWindow = new Stage();
        legendWindow.initStyle(StageStyle.TRANSPARENT);
        legendPane.setStyle("-fx-background-color: rgba(0,0,0,0.1);"); //0.1 alpha value so it is transparent
        legend.setFill(Color.TRANSPARENT);
        legendWindow.setScene(legend);

        // Specifies the owner Window (parent) for new window
        legendWindow.initOwner(primaryStage);

        // Set position of second window, related to primary window.
        legendWindow.setX(primaryStage.getX() + primaryStage.getWidth() - 200);
        legendWindow.setY(primaryStage.getY() + primaryStage.getHeight() - 250);

        //for the grid pane, so each entry can be in the right y
        int y =0;

        //add labels to legend
        for (LegendLabel label : labels){
            Rectangle rect = new Rectangle(20, 20 );
            rect.setFill(label.getColor());
            rect.setStroke(Color.BLACK);
            //Pad the string to take up the rest of the line
            //REF: https://stackoverflow.com/a/391978
            Label name = new Label(label.getName());
            name.setFont(roboto);
            legendPane.add(rect, 0, y);
            legendPane.add(name, 1, y);
            y++;
        }

        legendWindow.show();

        return legendWindow;
    }



    /**
     * Returns a list of graphics to add to the map overlay when given a list of properties and a colour to display the
     * property markers in
     * example usage: graphicsOverlay.getGraphics().addAll(getOverlayForProps(props, Color.CYAN))
     * @param properties A list of Property objects
     * @param color a Color object
     * @return a list of Graphics objects that can be added to the map using overlay.getGraphics().add(pointGraphic);
     */
    private List<Graphic> getOverlayForProps(List<PropertyAssessment> properties, Color color) {
        //create symbol to put on that point
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 3);

        //create list to hold graphics
        List<Graphic> graphics = new ArrayList<>();

        for (PropertyAssessment property : properties) {
            if(property != null){
                //add point to graphics overlay (lon/lat order now because of course it is)
                Point point = new Point(property.getLocation().getLongitude(),property.getLocation().getLatitude(), SpatialReferences.getWgs84());

                //create graphic using the point and the color graphic
                graphics.add(new Graphic(point, symbol));
            }
        }
        return graphics;
    }

    /**
     * Returns a list of graphics to add to the map overlay when given a list of bus stops and a colour to display the
     * bus stop markers in.
     * example usage: graphicsOverlay.getGraphics().addAll(getOverlayForBus(busStops, Color.ORANGE));
     * @param busStops A list of BusStops objects
     * @param color a Color object
     * @return a list of Graphics objects that can be added to the map using overlay.getGraphics().add(pointGraphic);
     */
    private List<Graphic> getOverlayForBus(List<BusStops> busStops, Color color) {
        //create symbol to put on that point
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, 4);

        //create list to hold graphics
        List<Graphic> graphics = new ArrayList<>();

        for (BusStops busStop : busStops) {
            //add point to graphics overlay (lon/lat order now because of course it is)
            Point point = new Point(busStop.getLongitude(),busStop.getLatitude(), SpatialReferences.getWgs84());

            //create graphic using the point and the color graphic
            graphics.add(new Graphic(point, symbol));
        }
        return graphics;
    }

    public void showAlert(String message) {
        //Adapted from REF: https://stackoverflow.com/a/36137669
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    private TableView<PropertyAssessment> makeTable(Stage primaryStage, ObservableList<PropertyAssessment> propertyDisplay) {
        TableView<PropertyAssessment> table = new TableView<>();
        table.prefWidthProperty().bind(primaryStage.widthProperty());
        table.prefHeightProperty().bind(primaryStage.heightProperty());
        table.setItems(propertyDisplay);

        TableColumn<PropertyAssessment, Integer> idCol = new TableColumn<>("Account");
        idCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        idCol.setPrefWidth(60);
        table.getColumns().add(idCol);

        TableColumn<PropertyAssessment, Address> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(175);
        table.getColumns().add(addressCol);

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMaximumFractionDigits(0);
        TableColumn<PropertyAssessment, String> valueCol = new TableColumn<>("Assessed Value");
        //REF: https://edencoding.com/tableview-customization-cellfactory/
        valueCol.setCellValueFactory(cellData -> {
            String formattedCost = currency.format(cellData.getValue().getAssessedValue());
            return new SimpleStringProperty(formattedCost);
        });
        valueCol.setPrefWidth(90);
        table.getColumns().add(valueCol);

        TableColumn<PropertyAssessment, AssessmentClass> classCol = new TableColumn<>("Assessment Class");
        classCol.setCellValueFactory(new PropertyValueFactory<>("assessmentClass"));
        classCol.setPrefWidth(150);
        table.getColumns().add(classCol);

        TableColumn<PropertyAssessment, Neighbourhood> neighbourhoodCol = new TableColumn<>("Neighbourhood");
        neighbourhoodCol.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));
        neighbourhoodCol.setPrefWidth(200);
        table.getColumns().add(neighbourhoodCol);

        TableColumn<PropertyAssessment, Neighbourhood> locCol = new TableColumn<>("(Latitude, Longitude)");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locCol.setPrefWidth(200);
        table.getColumns().add(locCol);

        return table;

    }


    public static void main(String[] args) {
        launch();
    }

}