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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;


public class UserInterface extends Application {

    PropertyAssessmentDAO dao = null;
    String source = "n/a";

    @Override
    public void start(final Stage primaryStage) {

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

        //add point to graphics overlay (lon/lat order now because of course it is)
        Point point = new Point(-113.5064,53.5471, SpatialReferences.getWgs84());
        //create symbol to put on that point
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.PURPLE, 10);

        //create a graphic of the point to add to the graphics overlay
        Graphic pointGraphic = new Graphic(point, symbol);
        graphicsOverlay.getGraphics().add(pointGraphic);


        statButton.setOnAction(event -> {
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
        //root.setLeft(button);


        Scene scene = new Scene(root, 1250, 600);

        primaryStage.setTitle("Map");
        primaryStage.setScene(scene);
        root.setStyle("-fx-background-color: #F8F8FF;");
        primaryStage.show();
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