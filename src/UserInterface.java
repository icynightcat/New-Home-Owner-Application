import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TableView;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;


public class UserInterface extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("JavaFX Test");
        FlowPane rootNode = new FlowPane();
        Scene scene = new Scene(rootNode, 1250, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Label title = new Label("Edmonton Property Assessments");
        title.setFont(new Font("Roboto", 16));
        rootNode.getChildren().add(title);

        ObservableList<PropertyAssessment> propertyDisplay = FXCollections.observableArrayList();

        //get and add properties to the display list
        PropertyAssessmentDAO apiDao = new ApiPropertyAssessmentDAO();
        PropertyAssessment property = apiDao.getByAccountNumber(1103530);
        propertyDisplay.add(property);
        PropertyAssessmentDAO dao = new CsvPropertyAssessmentDAO("Property_Assessment_Data_2022.csv");
        List<PropertyAssessment> properties = dao.getByAssessmentClass("FARMLAND");
        propertyDisplay.addAll(properties);

        TableView<PropertyAssessment> table = new TableView<>();
        table.prefWidthProperty().bind(primaryStage.widthProperty());
        table.prefHeightProperty().bind(primaryStage.heightProperty());
        table.setItems(propertyDisplay);

        rootNode.getChildren().add(table);

        TableColumn<PropertyAssessment, Integer> idCol = new TableColumn<>("Account");
        idCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        table.getColumns().add(idCol);

        TableColumn<PropertyAssessment, Address> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        table.getColumns().add(addressCol);

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMaximumFractionDigits(0);
        TableColumn<PropertyAssessment, String> valueCol = new TableColumn<>("Assessed Value");
        //REF: https://edencoding.com/tableview-customization-cellfactory/
        valueCol.setCellValueFactory(cellData -> {
            String formattedCost = currency.format(cellData.getValue().getAssessedValue());
            return new SimpleStringProperty(formattedCost);
        });
        table.getColumns().add(valueCol);

        TableColumn<PropertyAssessment, AssessmentClass> classCol = new TableColumn<>("Assessment Class");
        classCol.setCellValueFactory(new PropertyValueFactory<>("assessmentClass"));
        table.getColumns().add(classCol);

        TableColumn<PropertyAssessment, Neighbourhood> neighbourhoodCol = new TableColumn<>("Neighbourhood");
        neighbourhoodCol.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));
        table.getColumns().add(neighbourhoodCol);

        TableColumn<PropertyAssessment, Neighbourhood> locCol = new TableColumn<>("(Latitude, Longitude)");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        table.getColumns().add(locCol);

    }
}
