import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
 * This class represents the controller for the student sign-in functionality in a Course Management System.
 * It handles authentication of student credentials, provides a user interface for student sign-in, 
 * and facilitates navigation to different user interfaces based on user selection.
 */
public class studentSignInController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showpassword;

    @FXML
    private TextField login_studentID;

    @FXML
    private ComboBox<String> login_user;

    @FXML
    private CheckBox showPassword;

    @FXML
    private AnchorPane signin_page;

    /**
     * Attempts to authenticate the student using provided credentials and logs the student in if successful.
     * Displays appropriate error or success messages based on the authentication result.
     * Closes the login screen upon successful login and opens the student screen.
     * Resets the input fields after login attempt.
     *
     * @param event The ActionEvent triggered by the login button click.
     * @throws IOException if an I/O error occurs while reading or writing student data.
     */
    public void Login(ActionEvent event) throws IOException {
        alertMessage alert = new alertMessage();

        // check if have empty field
        if (login_studentID.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("All fields are neccessary to be filled.");
        } else {
            // Read student.csv to check if the ID and password exist
            List<Student> students = studentUtil.readStudentFromFile();
            boolean studentExists = false;

            int studentID = Integer.parseInt(login_studentID.getText().trim());

            for (Student student : students) {
                if ((studentID == student.getId()) && login_password.getText().equals(student.getPassword())) {
                    studentExists = true;
                    Data.studentID = studentID;
                    Data.studentName = student.getName();
                    break;
                } 
            }

            if (studentExists) {
                alert.successMessage("Login successful!\nWelcome, " + Data.studentName);
                // Close the login screen
                Stage loginStage = (Stage) loginBtn.getScene().getWindow();
                loginStage.close();
                // Open student screen
                Parent root = FXMLLoader.load(getClass().getResource("studentScreen.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Course Management System");
                stage.setScene(new Scene(root));
                stage.show();
                reset();
            } else {
                alert.errorMessage("Warning: Student with ID " + login_studentID.getText() + " does not exist or incorrect password.");
                reset();
            }
        }
    }

     /**
     * Resets the input fields of the student sign-in form.
     * Clears the student ID, password, and user selection fields.
     */
    public void reset() {
        login_studentID.setText("");
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
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIn.fxml"));
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
     * Initializes the student sign in screen interface.
     * @param location The URL location of the FXML file.
     * @param resources The resources bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList();
    }
}
