import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The adminScreenController class controls the functionality of the admin.
 */
public class adminScreenController implements Initializable {

    @FXML
    private AnchorPane CourseInfo;

    @FXML
    private AnchorPane LecturerInfo;

    @FXML
    private AnchorPane StudentInfo;

    @FXML
    private AnchorPane SpecificCodeInfo;

    @FXML
    private Label adminID;

    @FXML
    private Label adminName;

    @FXML
    private Button courseListBtn;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TextField course_code;

    @FXML
    private TableColumn<String, Course> course_col_code;

    @FXML
    private TableColumn<String, Course> course_col_name;

    @FXML
    private TableColumn<String, Course> course_col_credit;

    @FXML
    private TableColumn<String, Course> course_col_lecturer;

    @FXML
    private TableColumn<String, Course> course_col_prerequisite;

    @FXML
    private TextField course_credithours;

    @FXML
    private TextField course_prerequisite;

    @FXML
    private TextField course_name;

    @FXML
    private Button lecturerListBtn;

    @FXML
    private TableView<Lecturer> lecturerTable;

    @FXML
    private TableColumn<String, Lecturer> lecturer_col_id;

    @FXML
    private TableColumn<String, Lecturer> lecturer_col_name;

    @FXML
    private TextField lecturer_id;

    @FXML
    private TextField lecturer_name;

    @FXML
    private Button signOutBtn;

    @FXML
    private Button specificCourseBtn;

    @FXML
    private Button studentListBtn;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<String, Student> student_col_id;

    @FXML
    private TableColumn<String, Student> student_col_name;

    @FXML
    private TextField student_id;

    @FXML
    private TextField student_name;

    @FXML
    private Button updateCourseBtn;

    @FXML
    private VBox displayLecturer;

    @FXML
    private Button addCourseBtn;

    @FXML
    private Button assignLecturerBtn;

    @FXML
    private Button searchCourseBtn;

    @FXML
    private TableView<Enrollment> specificCourseTable;

    @FXML
    private ComboBox<String> specificCourse_code;

    @FXML
    private TableColumn<String, Enrollment> specificCourse_col_studentid;

    @FXML
    private TableColumn<String, Enrollment> specificCourse_col_studentname;

    @FXML
    private Label display_code;

    @FXML
    private Label display_lecturer;

    /**
     * Displays admin id and name
     */
    public void displayAdminInfo() {
        adminID.setText(String.valueOf(Data.adminID));
        adminName.setText(Data.adminName);
    }

    // -----------------------------CreateNewStudent---------------------------------------

    /**
     * Displays the list of students retrieved from file in the TableView.
     */
    public void studentTable() {
        // reads data about students from file and returns as ObservableList
        ObservableList<Student> studentList = FXCollections.observableArrayList(studentUtil.readStudentFromFile());

        // Set up cell value factories for student ID and name columns by extracting the
        // "id" and "name" properties from student object
        student_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        student_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Sets the items to be displayed in the studentTable
        studentTable.setItems(studentList);
    }

    /**
     * Creates a new student with the provided ID and name.
     */
    public void createNewStudentBtn() {
        alertMessage alert = new alertMessage();

        if (student_id.getText().isEmpty() || student_name.getText().isEmpty()) { // ensure no input field is empty
            alert.errorMessage("Please fill all blank fields.");
        } else if(student_id.getText().length() != 4 || !student_id.getText().matches("\\d+")){
            alert.errorMessage("Invalid ID. Please enter excatly four digit.");
        } else {
            // read in data from input field
            int newId = Integer.parseInt(student_id.getText());
            String newName = student_name.getText();
            // set default password : name123(no spacing and all lower case)
            String newPassword = newName.replaceAll("\\s+", "").toLowerCase() + "123";
            // set default value for trimester and credits
            int trimester = 1;
            int currentCredits = 0;
            int totalCredits = 0;

            // check if student id already exist
            boolean idExist = false;
            // retrieves the list of students from file
            List<Student> studentList = studentUtil.readStudentFromFile();

            for (Student s : studentList) {
                // reject addition of new student with id that already exists
                if (newId == s.getId()) {
                    alert.errorMessage("Cannot add ID " + newId + " because it already exists.");
                    idExist = true;
                }
            }

            // if id of the new student doesn't already exist, the student is added to the
            // list and the updated list is saved to a file
            if (idExist == false) {
                studentList.add(new Student(newId, newName, newPassword, trimester, currentCredits, totalCredits));
                try {
                    studentUtil.saveStudentListToFile(studentList);
                    alert.successMessage("Student with name: " + newName + ", id: " + newId + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        ClearStudentField();
        StudentTableRefresh();
    }

    /**
     * Refreshes the TableView, displaying the updated list of students.
     */
    public void StudentTableRefresh() {
        ObservableList<Student> studentList = FXCollections.observableArrayList(studentUtil.readStudentFromFile());
        studentTable.getItems().clear(); // empty the table
        studentTable.getItems().addAll(studentList); // add updated student list
    }

    /**
     * Clears the input fields for adding a new student.
     */
    public void ClearStudentField() {
        student_id.setText("");
        student_name.setText("");
    }

    // -----------------------------CreateNewLecturer----------------------------------------
    /**
     * Displays the list of lecturer retrieved from file in the TableView.
     */
    public void lecturerTable() {
        ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList(lecturerUtil.readLecturerFromFile());

        lecturer_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        lecturer_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        lecturerTable.setItems(lecturerList);
    }

    /**
     * Creates a new lecturer with the provided ID and name.
     */
    public void createNewLecturerBtn() {
        alertMessage alert = new alertMessage();

        if (lecturer_id.getText().isEmpty() || lecturer_name.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields.");
        } else if(lecturer_id.getText().length() != 4 || !lecturer_id.getText().matches("\\d+")){
            alert.errorMessage("Invalid ID. Please enter excatly four digit.");
        } else {
            int newId = Integer.parseInt(lecturer_id.getText());
            String newName = lecturer_name.getText();
            // default password: name123
            String newPassword = newName.replaceAll("\\s+", "").toLowerCase() + "123";

            // check if lecturer id already exist
            boolean idExist = false;
            List<Lecturer> lecturerList = lecturerUtil.readLecturerFromFile();

            for (Lecturer l : lecturerList) {
                // reject addition of new lecturer with id that already exists
                if (newId == l.getId()) {
                    alert.errorMessage("Cannot add ID " + newId + " because it already exists.");
                    idExist = true;
                }
            }

            // save new Lecturer if id does not exist
            if (idExist == false) {
                lecturerList.add(new Lecturer(newId, newName, newPassword));
                try {
                    lecturerUtil.saveLecturerListToFile(lecturerList);
                    alert.successMessage("Lectuer with name: " + newName + ", id: " + newId + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        ClearLecturerField();
        LecturerTableRefresh();
    }

    /**
     * Refreshes the TableView, displaying the updated list of lecturer.
     */
    public void LecturerTableRefresh() {
        ObservableList<Lecturer> lecturerList = FXCollections.observableArrayList(lecturerUtil.readLecturerFromFile());
        lecturerTable.getItems().clear(); // remove all
        lecturerTable.getItems().addAll(lecturerList);
    }

    /**
     * Clears the input fields for adding a new lecturer.
     */
    public void ClearLecturerField() {
        lecturer_id.setText("");
        lecturer_name.setText("");
    }

    // -----------------------------Create New Course + Assign Course To Lecturer---------------------------
    /**
     * Displays the list of courses retrieved from file in the TableView.
     */
    public void courseTable() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());

        course_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        course_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col_credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        course_col_prerequisite.setCellValueFactory(new PropertyValueFactory<>("PR"));
        course_col_lecturer.setCellValueFactory(new PropertyValueFactory<>("LecturerName"));

        courseTable.setItems(courseList);
    }

    /**
     * Creates a new course with the provided ID and name.
     */
    public void createNewCourseBtn() {
        alertMessage alert = new alertMessage();

        // check if credit hours only contain digit
        boolean containsOnlyDigits = true;
        for (char c : course_credithours.getText().toCharArray()) {
            if (!Character.isDigit(c)) {
                containsOnlyDigits = false;
                break;
            }
        }

        if (course_code.getText().isEmpty() || course_name.getText().isEmpty()
                || course_credithours.getText().isEmpty()) {
            alert.errorMessage("Please fill course code, course name and credit hours.");
        } else if (!containsOnlyDigits){
            alert.errorMessage("Credit hours must contain only digits.");
        } else {
            String newCourseCode = course_code.getText();
            String newCourseName = course_name.getText();
            int newCredits = Integer.parseInt(course_credithours.getText());
            // If course prerequisite is empty, set to "Nil"
            // Else read in the prerequisite, replace commas with slashes 
            // (so that it is not mixed up with other data fields in CSV file)
            String newCoursePrerequisite = (course_prerequisite.getText().isEmpty()) ? "Nil"
                    : course_prerequisite.getText().replaceAll("\\s*,\\s*", "/");
            String newCourseLecturer = (getSelectedLecturers().isEmpty()) ? "None"
                    : getSelectedLecturers();

            // check if course code already exist
            boolean idExist = false;
            List<Course> courseList = courseUtil.readCourseFromFile();
            for (Course c : courseList) {
                // reject addition of new student with id that already exists
                if (newCourseCode.equals(c.getCode())) {
                    alert.errorMessage("Cannot add Course Code: " + newCourseCode + " because it already exists.");
                    idExist = true;
                }
            }

            // save new course if code does not exist
            if (idExist == false) {
                courseList.add(
                        new Course(newCourseCode, newCourseName, newCredits, newCourseLecturer, newCoursePrerequisite));
                try {
                    courseUtil.saveCourseListToFile(courseList);
                    alert.successMessage("Course with code: " + newCourseCode + " is successfully added");
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        updateCourseBtn.setVisible(false);
        ClearCourseField();
        CourseTableRefresh();
    }

    /**
     * Retrieves the selected course from the course table and displays its details.
     */
    public void assignNewCourseBtn() {

        alertMessage alert = new alertMessage();
        // Get the selected course from the courseTable
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        // Get the index of the selected item in the courseTable
        int num = courseTable.getSelectionModel().getSelectedIndex();

        // Check if item is selected
        if (num < 0) {
            alert.errorMessage("Please select the course first.");
            return;
        } else {
            // Set the details of the selected course to the corresponding fields
            course_code.setText(selectedCourse.getCode());
            course_name.setText(selectedCourse.getName());
            course_credithours.setText(String.valueOf(selectedCourse.getCredit()));
            course_prerequisite.setText(selectedCourse.getPR());

            // make other field non-editable
            course_code.setEditable(false);
            course_name.setEditable(false);
            course_credithours.setEditable(false);
            course_prerequisite.setEditable(false);

            // Disable the assignLecturerBtn and addCourseBtn
            assignLecturerBtn.setDisable(true);
            addCourseBtn.setDisable(true);

            // Make the updateCourseBtn visible
            updateCourseBtn.setVisible(true);
        }
    }

    /**
     * Updates the assigned lecturer to selected course 
     */
    public void updateNewCourseBtn() {
        alertMessage alert = new alertMessage();
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();

        if (getSelectedLecturers().isEmpty()) {
            alert.errorMessage("Please select lecturer to assign course:" + selectedCourse.getCode());
        } else {
            if (alert.confirmMessage("Are you sure you want to Update Course Code: "  + course_code.getText())) {
                // Retrieve new values from input fields
                String newCourseLecturer = (getSelectedLecturers().isEmpty()) ? "None": getSelectedLecturers();

                // Update the lecturer name of the course
                selectedCourse.setLecturerName(newCourseLecturer);

                List<Course> courseList = courseUtil.readCourseFromFile();
                // Find the index of the selected course in the list
                int index = courseUtil.indexOf(selectedCourse.getCode(), courseList);

                // Save the updated course list back to the file
                if (index != -1) {
                    // Update the course object in the list
                    courseList.set(index, selectedCourse);

                    // Save the updated course list back to the file
                    try {
                        courseUtil.saveCourseListToFile(courseList);
                        alert.successMessage(
                                "Course with code: " + selectedCourse.getCode() + " is successfully updated");
                    } catch (IOException ex) {
                        System.err.println("Error saving to " + ex.getMessage());
                    }
                }

                // update the assigned lecturer to enrollment.csv
                List<Enrollment> enrollments = enrollmentUtil.readEnrollmentFromFile();
                for (Enrollment e : enrollments) {
                    if (e.getCourseCode().equals(selectedCourse.getCode())) {
                        e.setLecturer(newCourseLecturer);
                    }
                }
                try {
                    enrollmentUtil.saveEnrollmentListToFile(enrollments);
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }
        }
        assignLecturerBtn.setDisable(false);
        addCourseBtn.setDisable(false);

        updateCourseBtn.setVisible(false);

        ClearCourseField();
        CourseTableRefresh();
    }

    /**
     * Retrieves the names of selected lecturers from check box and returns them as a string.
     * @return string contained name of selected lecturer 
     */
    public String getSelectedLecturers() {
        // Create a StringBuilder to store the selected lecturer names
        StringBuilder selectedLecturers = new StringBuilder();

        // Iterate through each child Node of displayLecturer VBox
        for (Node node : displayLecturer.getChildren()) {
            // Check if the Node is an instance of CheckBox
            if (node instanceof CheckBox) {
                // Then Cast the Node to CheckBox
                CheckBox checkBox = (CheckBox) node;

                // Check if the CheckBox is selected
                if (checkBox.isSelected()) {
                    // Split the text of the CheckBox by " - " and append the second part (lecturer name) to the StringBuilder
                    selectedLecturers.append(checkBox.getText().split(" - ")[1]).append("/");
                }
            }
        }

        // Remove the last "/" if there is at least one selected lecturer
        if (selectedLecturers.length() > 0) {
            selectedLecturers.deleteCharAt(selectedLecturers.length() - 1);
        }
        return selectedLecturers.toString();
    }
    
    /**
     * Displays a list of lecturers retrieved from the file as checkboxes for selection
     */
    public void selectLecturer() {
        // read lecturer name and id from file and store in map
        Map<Integer, String> lecturerMap = lecturerUtil.readLecturerIDandName();

        // Clear any existing items in the VBox
        displayLecturer.getChildren().clear();

        // Iterate over the entries of the map
        for (Map.Entry<Integer, String> entry : lecturerMap.entrySet()) {
            int lecturerId = entry.getKey();
            String lecturerName = entry.getValue();
            String info = lecturerId + " - " + lecturerName;

            // Create a CheckBox for each lecturer
            CheckBox checkBox = new CheckBox(info);
            displayLecturer.getChildren().add(checkBox);
        }
    }

    /**
     * Refreshes the TableView, displaying the updated list of course.
     */
    public void CourseTableRefresh() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());
        courseTable.getItems().clear(); // empty the table
        courseTable.getItems().addAll(courseList);  // add updated course list
    }

    /**
     * Clears the input fields for adding a new course.
     */
    public void ClearCourseField() {
        course_code.setText("");
        course_name.setText("");
        course_credithours.setText("");
        course_prerequisite.setText("");
        clearLecturerCheckBoxSelection();
    }

    /**
     * Clears the selection of all CheckBoxes in the displayLecturer VBox.
     */
    public void clearLecturerCheckBoxSelection() {
        for (Node node : displayLecturer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }

    // ----------------------------------View Student And Lecturer For Specific Course---------------------------
    /**
     * Displays information about a specific course including its code, lecturer, and enrolled students.
     */
    public void search() {
        alertMessage alert = new alertMessage();
        // Get the selected course code from the ComboBox
        String courseCode = specificCourse_code.getSelectionModel().getSelectedItem();
        if (courseCode == null) {
            alert.errorMessage("Please select a course to search. ");
        }

        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        // Find the course object corresponding to the selected course code
        Course selectedCourse = courseUtil.findCourseByCode(courseCode);

        display_code.setText(courseCode);
        display_lecturer.setText(selectedCourse.getLecturerName());

        // Filter the list of enrollments to include only those enrolled for the selected course code
        List<Enrollment> currentEnroll = new ArrayList<>();
        for (Enrollment enroll : allCurrentEnroll) {
            if (enroll.getCourseCode().equals(selectedCourse.getCode())) {
                currentEnroll.add(enroll);
            }
        }

        // Convert the filtered list of enrollments to an ObservableList
        ObservableList<Enrollment> studentList = FXCollections.observableArrayList(currentEnroll);

        specificCourse_col_studentid.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        specificCourse_col_studentname.setCellValueFactory(new PropertyValueFactory<>("StudentName"));

        specificCourseTable.setItems(studentList);
    }

    /**
     * Retrieves a list of available courses.
     */
    public void coursesList() {
        List<Course> courseAvailable = courseUtil.readCourseFromFile();

        // Map each course to its course code and collect them into a list
        List<String> courseCodes = new ArrayList<>();
        for (Course course : courseAvailable) {
            courseCodes.add(course.getCode());
        }

        ObservableList<String> listData = FXCollections.observableList(courseCodes);
        // set items of combo box
        specificCourse_code.setItems(listData);
    }

    /**
     * Clears the data to prepare for new entry
     */
    public void clearSpecificCourse() {
        specificCourse_code.getSelectionModel().clearSelection();
        display_code.setText("");
        display_lecturer.setText("");
        specificCourseTable.getItems().clear();
    }

    // ------------------------SwitchDashboard-----------------------------------
    /**
     * Switches between different dashboards based on the ActionEvent.
     * @param event Event triggered by the user interaction.
     */
    public void switchForm(ActionEvent event) {
        // switch to different dashboard
        if (event.getSource() == studentListBtn) {
            StudentInfo.setVisible(true);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(false);
        } else if (event.getSource() == lecturerListBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(true);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(false);
        } else if (event.getSource() == courseListBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(true);
            SpecificCodeInfo.setVisible(false);

            // update lecturer list so that new lecturer added will be display
            selectLecturer();

        } else if (event.getSource() == specificCourseBtn) {
            StudentInfo.setVisible(false);
            LecturerInfo.setVisible(false);
            CourseInfo.setVisible(false);
            SpecificCodeInfo.setVisible(true);
            
            // update course list so that new course added will be display
            coursesList();
            clearSpecificCourse();
        }

        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("adminSignIn.fxml"));
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
     * Initializes the admin screen interface.
     * @param location The URL location of the FXML file.
     * @param resources The resources bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayAdminInfo();
        studentTable();
        lecturerTable();
        courseTable();
        selectLecturer();
        coursesList();
        clearSpecificCourse();
    }
}
