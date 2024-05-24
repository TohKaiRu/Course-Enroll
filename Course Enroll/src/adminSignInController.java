import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This class represents the controller for the administrator sign-in functionality in a Course Management System.
 * It handles authentication of admin credentials, provides a user interface for admin sign-in, and facilitates navigation to different user interfaces based on user selection.
 */
public class adminSignInController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField login_adminID;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showpassword;

    @FXML
    private ComboBox<String> login_user;

    @FXML
    private CheckBox showPassword;

    @FXML
    private AnchorPane signin_page;

    /**
     * Attempts to authenticate the admin using provided credentials and logs the admin in if successful.
     * Displays appropriate error or success messages based on the authentication result.
     * Closes the login screen upon successful login and opens the admin screen.
     * Resets the input fields after login attempt.
     *
     * @throws IOException if an I/O error occurs while reading or writing admin data.
     */
    public void Login() throws IOException {
        alertMessage alert = new alertMessage();

        // check if have empty field
        if (login_adminID.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("All fields are neccessary to be filled.");
        } else {
            // Read admin.csv to check if the ID and password exist
            List<Admin> admins = adminUtil.readAdminFromFile();
            boolean adminExists = false;

            int adminID = Integer.parseInt(login_adminID.getText().trim());

            for (Admin admin : admins) {
                if ((adminID == admin.getId()) && login_password.getText().equals(admin.getPassword())) {
                    adminExists = true;
                    Data.adminID = adminID;
                    Data.adminName = admin.getName();
                    break;
                }
            }

            if (adminExists) {
                alert.successMessage("Login successful!\nWelcome, " + Data.adminName);
                // Close the login screen
                Stage loginStage = (Stage) loginBtn.getScene().getWindow();
                loginStage.close();
                // Open admin screen
                Parent root = FXMLLoader.load(getClass().getResource("adminScreen.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Course Management System");
                stage.setScene(new Scene(root));
                stage.show();
                reset();
            } else {
                alert.errorMessage("Warning: Admin with ID " + login_adminID.getText() + " does not exist or incorrect password.");
                reset();
            }
        }
    }

    /**
     * Resets the input fields of the admin sign-in form.
     * Clears the admin ID, password, and user selection fields.
     */
    public void reset() {
        login_adminID.setText("");
        login_password.setText("");
        login_showpassword.setText("");
        showPassword.setSelected(false);
        login_showpassword.setVisible(false);
        login_password.setVisible(true);
        login_user.getSelectionModel().clearSelection();
    }

    /**
     * Toggles the visibility of the password field between masked and unmasked.
     * Shows the password in plain text when the checkbox is selected.
     * Hides the password and displays masked characters when the checkbox is deselected.
     */
    public void showPassword() {
        if (showPassword.isSelected()) {
            login_showpassword.setText(login_password.getText());
            login_showpassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showpassword.getText());
            login_showpassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    /**
     * Populates the user selection dropdown menu with available user types.
     */
    public void userList() {
        List<String> list = new ArrayList<>();

        for (String data : Data.user) {
            list.add(data);
        }
        ObservableList<String> listData = FXCollections.observableList(list);
        login_user.setItems(listData);
    }

    /**
     * Switches to a different sign-in page based on the selected user type.
     * Opens the corresponding sign-in form for Student, Admin, or Lecturer.
     * Hides the current sign-in form upon switching.
     */
    public void switchPage() {

        if (login_user.getSelectionModel().getSelectedItem() == "Student") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("studentSignIn.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (login_user.getSelectionModel().getSelectedItem() == "Admin") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIN.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (login_user.getSelectionModel().getSelectedItem() == "Lecturer") {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("lecturerSignIn.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        login_user.getScene().getWindow().hide();
    }

    /**
     * Initializes the admin sign in screen interface.
     * @param location The URL location of the FXML file.
     * @param resources The resources bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList();
    }

}
