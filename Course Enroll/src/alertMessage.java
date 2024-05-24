import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
/**
 * The AlertMessage class provides methods to display different types of alert messages.
 */
public class alertMessage {
    private Alert alert;
    /**
     * Displays an error message.
     * @param message The error message to be displayed.
     */
    public void errorMessage(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success message.
     * @param message The success message to be displayed.
     */
    public void successMessage(String message) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation message and returns true if the user confirms(click OK), false otherwise(click cancel).
     * @param message The confirmation message to be displayed.
     * @return true if the user confirms, false otherwise.
     */
    public boolean confirmMessage(String message) {
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();

        return option.get().equals(ButtonType.OK);  // return true, if click 'OK'
    }
}
