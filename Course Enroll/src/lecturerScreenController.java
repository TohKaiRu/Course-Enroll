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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The lecturerScreenController class controls the functionality of the lecturer.
 */
public class lecturerScreenController implements Initializable {
    @FXML
    private AnchorPane ViewStudent;

    @FXML
    private ComboBox<String> selecedCourse;

    @FXML
    private Label display_course;

    @FXML
    private Label lecturerID;

    @FXML
    private Label lecturerName;

    @FXML
    private Button searchCourseBtn;

    @FXML
    private Button signOutBtn;

    @FXML
    private TableView<Enrollment> studentTable;

    @FXML
    private TableColumn<String, Enrollment> student_col_id;

    @FXML
    private TableColumn<String, Enrollment> student_col_name;

    @FXML
    private Button viewStudentBtn;

    /**
     * Displays lecturer id and name
     */
    public void displayLecturerInfo() {
        lecturerID.setText(String.valueOf(Data.lecturerID));
        lecturerName.setText(Data.lecturerName);
    }

    /**
     * Searches for students enrolled in the selected course and displays them in the table.
     */
    public void search() {

        String courseCode = selecedCourse.getSelectionModel().getSelectedItem();
        alertMessage alert = new alertMessage();
        // Check if a course is selected
        if (courseCode == null) {
            alert.errorMessage("Please select a course to search. ");
        }
        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        Course selectedCourse = courseUtil.findCourseByCode(courseCode);

        display_course.setText(courseCode);

        // Filter enrollments to include only those with selected course code
        List<Enrollment> currentEnroll = new ArrayList<>();
        for (Enrollment enroll : allCurrentEnroll) {
            if (enroll.getCourseCode().equals(selectedCourse.getCode())) {
                currentEnroll.add(enroll);
            }
        }

        ObservableList<Enrollment> studentList = FXCollections.observableArrayList(currentEnroll);

        student_col_id.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        student_col_name.setCellValueFactory(new PropertyValueFactory<>("StudentName"));

        studentTable.setItems(studentList);
    }

    /**
     * Set the combo box with courses assigned to the logged-in lecturer.
     */
    public void coursesList() {
        List<Course> courseAvailable = courseUtil.readCourseFromFile();
        String lecturerName = Data.lecturerName;
        
        // Filter the courses to include only those taught by the lecturer
        List<String> courses = new ArrayList<>();
        for (Course course : courseAvailable) {
            // for course which have more than one lecturer
            String[] lecturerNames = course.getLecturerName().split(",");
            // Check if the lecturer name matches any of the lecturer names for the course
            for (String name : lecturerNames) {
                if (name.trim().equals(lecturerName)) {
                    courses.add(course.getCode());
                    break;
                }
            }
        }
                
        // Convert the list of course codes into an ObservableList
        ObservableList<String> listData = FXCollections.observableList(courses);
        selecedCourse.setItems(listData);
    }

    /**
     * Clears the data to prepare for new entry
     */
    public void ClearField(){
        selecedCourse.getSelectionModel().clearSelection();
        display_course.setText("");
        studentTable.getItems().clear();;
    }

    /**
     * Switches between different dashboards based on the ActionEvent.
     * @param event Event triggered by the user interaction.
     */
    public void switchForm(ActionEvent event) {
        if (event.getSource() == viewStudentBtn) {
            ViewStudent.setVisible(true);
        }
        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("lecturerSignIn.fxml"));
                Stage signInStage = new Stage();
                signInStage.setTitle("Login");
                signInStage.setScene(new Scene(root));
                signInStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes the lecturer screen interface.
     * @param location The URL location of the FXML file.
     * @param resources The resources bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayLecturerInfo();
        coursesList();
        ClearField();
    }
}
