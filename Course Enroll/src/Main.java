import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Main class to launch JavaFX application
 */
public class Main extends Application{
    /**
     * Main launching point
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is called when the application is launched.
     * It initializes and displays the primary stage.
     * @param primaryStage The primary stage of the application.
     */
    public void start(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminSignIn.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}