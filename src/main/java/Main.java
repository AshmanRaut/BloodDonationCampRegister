import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the main FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root, 1200, 700);

            // Set up the primary stage
            primaryStage.setTitle("Community Blood Donation Camp Register");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Set minimum window size
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
