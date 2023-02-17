import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class UserInterface extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Test");
        FlowPane rootNode = new FlowPane();
        Scene scene = new Scene(rootNode, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
