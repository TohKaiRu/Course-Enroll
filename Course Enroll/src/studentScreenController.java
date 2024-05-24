import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The studentScreenController class controls the functionality of the student.
 */
public class studentScreenController implements Initializable {
    @FXML
    private AnchorPane AddDropCourse;

    @FXML
    private Button viewSubjectListBtn;

    @FXML
    private Button addDropCourseBtn;

    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<String, Course> course_col_code;

    @FXML
    private TableColumn<String, Course> course_col_credithours;

    @FXML
    private TableColumn<String, Course> course_col_lecturer;

    @FXML
    private TableColumn<String, Course> course_col_name;

    @FXML
    private TableColumn<String, Course> course_col_prerequisite;

    @FXML
    private Label currentTrimesterCreditHours;

    @FXML
    private TableView<Enrollment> enrollmentTable;

    @FXML
    private TableColumn<String, Enrollment> enroll_col_code;

    @FXML
    private TableColumn<String, Enrollment> enroll_col_credithours;


    @FXML
    private Button dropBtn;

    @FXML
    private Button enrollBtn;

    @FXML
    private ComboBox<String> displayDropCourseList;

    @FXML
    private ComboBox<String> displayEnrollCourseList;

    @FXML
    private Button signOutBtn;

    @FXML
    private Label studentID;

    @FXML
    private Label studentName;

    @FXML
    private Label totalCreditHours;

    @FXML
    private Label currentTrimester;

    @FXML
    private Button changeTrimesterBtn;

    @FXML
    private Label trimester1_course;

    @FXML
    private Label trimester2_course;

    @FXML
    private Label trimester3_course;

    @FXML
    private Label selectDrop;

    @FXML
    private Label selectEnroll;

    @FXML
    private Label congratMessage;

    @FXML
    private TableView<PastSubject> pastSubjectTable;

    @FXML
    private TableColumn<String, PastSubject> past_col_code;

    @FXML
    private TableColumn<String, PastSubject> past_col_credit;

    @FXML
    private TableColumn<String, PastSubject> past_col_name;

    @FXML
    private TableView<Enrollment> currentSubjectTable;

    @FXML
    private TableColumn<String, Enrollment> current_col_code;

    @FXML
    private TableColumn<String, Enrollment> current_col_credit;

    @FXML
    private TableColumn<String, Enrollment> current_col_name;

    @FXML
    private TableView<Course> futureSubjectTable;

    @FXML
    private TableColumn<String, Course> future_col_code;

    @FXML
    private TableColumn<String, Course> future_col_credit;

    @FXML
    private TableColumn<String, Course> future_col_name;

    @FXML
    private AnchorPane viewSubjectList;

    /**
     * Displays student id and name
     */
    public void displayStudentInfo() {
        studentID.setText(String.valueOf(Data.studentID));
        studentName.setText(Data.studentName);
    }

    // -------------------------------Add/DropCourse-------------------------------------
    /**
     * Displays the list of courses retrieved from file in the TableView.
     */
    public void courseTable() {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courseUtil.readCourseFromFile());

        course_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        course_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        course_col_credithours.setCellValueFactory(new PropertyValueFactory<>("credit"));
        course_col_prerequisite.setCellValueFactory(new PropertyValueFactory<>("PR"));
        course_col_lecturer.setCellValueFactory(new PropertyValueFactory<>("LecturerName"));

        courseTable.setItems(courseList);
    }

    /**
     * Displays the list of enrollment retrieved from file in the TableView.
     */
    public void enrollmentTable() {
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        // Filter enrollments to get only those related to the selected student
        List<Enrollment> studentEnrollments = new ArrayList<>();
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getStudentId() == selectedStudent.getId())
                studentEnrollments.add(enrollment);
        }

        ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList(studentEnrollments);

        enroll_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        enroll_col_credithours.setCellValueFactory(new PropertyValueFactory<>("Credit"));

        enrollmentTable.setItems(enrollmentList);
    }

    /**
     * Enrolls a student in a selected course.
     *
     * @throws IOException if an I/O error occurs while reading or writing files
     */
    public void enrollBtn() throws IOException {
        alertMessage alert = new alertMessage();

        String selectedAddCourse = displayEnrollCourseList.getSelectionModel().getSelectedItem();
        // Check if a course is selected
        if (selectedAddCourse == null) {
            alert.errorMessage("Please select a course to enroll. ");
        }

        Course selectedCourse = courseUtil.findCourseByCode(selectedAddCourse);
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        boolean alreadyEnrolled = false;
        boolean currentTriCreditFull = false;
        boolean prerequisitesMet = false;

        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        List<PastSubject> pastSubjects = pastSubjectUtil.readPastSubjectFromFile();

        // Calculate current and total credits after adding the selected course
        int currentCredits = selectedStudent.getCredit().getCurrentTrimesterCredits();
        int afterAddCurrentCredits = currentCredits + selectedCourse.getCredit();
        int totalCredits = selectedStudent.getCredit().getTotalCredits();
        int afterAddTotalCredits = totalCredits + selectedCourse.getCredit();

        // Check if the total credits exceed the limit (30)
        if (afterAddTotalCredits > 30) {
            alert.errorMessage("Cannot enroll. You have already completed 30 credits required for program completion.!");
            return;
        }

        // a. check if the course is already added by the student
        int currentTrimester = selectedStudent.getTrimester();
        // currently in trimester 1
        // check the current trimester's enrollments
        if (currentTrimester == 1) {
            for (Enrollment enrollment : allEnrollments) {
                if (enrollment.getStudentId() == selectedStudent.getId()
                        && enrollment.getCourseCode().equals(selectedAddCourse)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
        } else {
            // currently in trimester 2/3
            // check with previous Trimester enrollment
            for (PastSubject p : pastSubjects) {
                if (p.getStudentId() == selectedStudent.getId() && p.getCourseCode().equals(selectedAddCourse)) {
                    // trimester 2, check trimester 1 enrollment
                    if (currentTrimester == 2 && p.getTrimester() == 1) {
                        alreadyEnrolled = true;
                        break;
                        // trimester 3, check trimester 1 and 2 enrollment
                    } else if (currentTrimester == 3 && (p.getTrimester() == 1 || p.getTrimester() == 2)) {
                        alreadyEnrolled = true;
                        break;
                    }
                }
            }
            // check the current trimester's enrollments
            for (Enrollment enrollment : allEnrollments) {
                if (enrollment.getStudentId() == selectedStudent.getId()
                        && enrollment.getCourseCode().equals(selectedAddCourse)) {
                    alreadyEnrolled = true;
                    break;
                }
            }
        }

        // b. check if credits exceed limit (12) for current trimester
        if (afterAddCurrentCredits > 12) {
            currentTriCreditFull = true;
        }

        // c. check prerequisites
        String[] prerequisiteCourses = selectedCourse.getPR().split(", ");
        if (currentTrimester == 1) {
            // for tri 1, all course which have prerequisites cannot be enroll
            prerequisitesMet = "Nil".equals(prerequisiteCourses[0]);
        } else { // for tri 2 and 3
            Student s = studentUtil.findStudentById(Data.studentID);
            int total = s.getCredit().getTotalCredits();
            if (!"Nil".equals(prerequisiteCourses[0])) {
                // Check if all prerequisites are completed
                boolean allPrerequisitesCompleted = Arrays.stream(prerequisiteCourses)
                        .allMatch(course -> {
                            if (course.startsWith("completed at least ")) {
                                int requiredCredits = Integer.parseInt(course.split(" ")[3]);
                                return total >= requiredCredits;
                            }
                            // check if prerequisites course are in past enrollments
                            List<String> pastEnroll = new ArrayList<>();
                            for (PastSubject p : pastSubjects) {
                                if (p.getStudentId() == selectedStudent.getId()) {
                                    pastEnroll.add(p.getCourseCode());
                                }
                            }
                            return pastEnroll.contains(course);

                        });

                if (allPrerequisitesCompleted) {
                    prerequisitesMet = true;
                }
            } else {
                // If no prerequisites('Nil'), set prerequisitesMet to true
                prerequisitesMet = true;
            }
        }

        if (alreadyEnrolled) {
            alert.errorMessage("You are already enrolled in this course.");
        } else if (currentTriCreditFull) {
            alert.errorMessage(
                    "Cannot enroll, reach maximun credit hours. To enroll for next trimester, please click on Change Trimester.");
        } else if (!prerequisitesMet) {
            alert.errorMessage("Cannot enroll. Prerequisites for this course are not met.");
        } else {
            // set current trimester credit hours
            selectedStudent.getCredit().setCurrentTrimesterCredits(afterAddCurrentCredits);
            // set total credits
            selectedStudent.getCredit().setTotalCredits(afterAddTotalCredits);
            // update credits into student csv file
            List<Student> studentList = studentUtil.readStudentFromFile();
            // Find the index of the selected student in the list
            int index = studentUtil.indexOf(selectedStudent.getId(), studentList);

            // Save the updated student list back to the file
            if (index != -1) {
                // Update the course object in the list
                studentList.set(index, selectedStudent);

                // Save the updated student list back to the file
                try {
                    studentUtil.saveStudentListToFile(studentList);
                } catch (IOException ex) {
                    System.err.println("Error saving to " + ex.getMessage());
                }
            }

            // Enroll the student in the course
            Enrollment newEnrollment = new Enrollment(selectedStudent.getId(), selectedAddCourse,
                    selectedCourse.getLecturerName().replaceAll("\\s*,\\s*", "/"),
                    selectedCourse.getCredit());
            // Add the new enrollment to the list
            allEnrollments.add(newEnrollment);
            // Save the updated list to file
            enrollmentUtil.saveEnrollmentListToFile(allEnrollments);
            alert.successMessage("Enrollment successful!");
        }

        // check if complete 30 credits - complete program(graduate)
        if(afterAddTotalCredits == 30 && !alreadyEnrolled && !currentTriCreditFull && prerequisitesMet){
            boolean result = alert.confirmMessage("30 credits registered, meets program completion. By confirming, you'll be considered eligible for graduation.");
            if(result){
                // hide enroll and drop course section
                enrollBtn.setVisible(false);
                dropBtn.setVisible(false);
                displayEnrollCourseList.setVisible(false);
                displayDropCourseList.setVisible(false);
                selectEnroll.setVisible(false);
                selectDrop.setVisible(false);
                // show congrat messages
                congratMessage.setVisible(true);

                // Remove enrolled courses from enrollment.csv and add to pastSubject.csv
                List<Enrollment> EnrollmentList = enrollmentUtil.readEnrollmentFromFile();
                // Creates an iterator to loop through Enrollments list. 
                Iterator<Enrollment> iterator = EnrollmentList.iterator();
                List<PastSubject> pastEnrollList = pastSubjectUtil.readPastSubjectFromFile();

                while (iterator.hasNext()) {
                    Enrollment enrollment = iterator.next();
                    if (selectedStudent.getId() == enrollment.getStudentId()) {
                        // Creates PastSubject object using information from the enrollment
                        PastSubject pastSubject = new PastSubject(enrollment.getStudentId(),
                                selectedStudent.getTrimester(), enrollment.getCredit(), enrollment.getCourseCode());
                        pastEnrollList.add(pastSubject); // Add to past subjects list
                        iterator.remove(); // Remove from enrollment.csv
                    }
                }
                // Save to pastSubject.csv (includes the newly added past subjects)
                pastSubjectUtil.savePastSubjectListToFile(pastEnrollList);

                // Update enrollment.csv (past subjects have been removed)
                enrollmentUtil.saveEnrollmentListToFile(EnrollmentList);

                showPastEnrollCourse();
            }
        }
        EnrollmentTableRefresh();
        courseList();
        ClearField();
        creditInfo();
    }

    /**
     * Drops a selected course for the currently logged-in student.
     *
     * @throws IOException if an I/O error occurs while reading or writing files
     */
    public void dropBtn() throws IOException {
        alertMessage alert = new alertMessage();
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);
        String selectedDropCourse = displayDropCourseList.getSelectionModel().getSelectedItem();
        if (selectedDropCourse == null) {
            alert.errorMessage("Please select a course to drop. ");
        }
        
        Course selectedCourse = courseUtil.findCourseByCode(selectedDropCourse);
        // Calculate current and total credits after dropping the selected course
        int currentCredits = selectedStudent.getCredit().getCurrentTrimesterCredits();
        int afterDropCurrentCredits = currentCredits - selectedCourse.getCredit();
        int totalCredits = selectedStudent.getCredit().getTotalCredits();
        int afterDropTotalCredits = totalCredits - selectedCourse.getCredit();

        // Find the enrollment to remove
        Enrollment enrollmentToRemove = null;
        for (Enrollment e : allEnrollments) {
            if (e.getStudentId() == selectedStudent.getId() && e.getCourseCode().equals(selectedDropCourse)) {
                enrollmentToRemove = e;
                break;
            }
        }

        // Remove the enrollment
        allEnrollments.remove(enrollmentToRemove);
        enrollmentUtil.saveEnrollmentListToFile(allEnrollments);
        alert.successMessage("Successfully drop course " + selectedDropCourse);

        // update student credits
        selectedStudent.getCredit().setCurrentTrimesterCredits(afterDropCurrentCredits);
        selectedStudent.getCredit().setTotalCredits(afterDropTotalCredits);

        List<Student> studentList = studentUtil.readStudentFromFile();
        int index = studentUtil.indexOf(selectedStudent.getId(), studentList);
        if (index != -1) {
            // Update the course object in the list
            studentList.set(index, selectedStudent);

            // Save the updated student list back to the file
            try {
                studentUtil.saveStudentListToFile(studentList);
            } catch (IOException ex) {
                System.err.println("Error saving to " + ex.getMessage());
            }
        }

        EnrollmentTableRefresh();
        ClearField();
        creditInfo();
        courseList();
    }

    /**
     * Refreshes the enrollment table to display the enrollments of the currently
     * logged-in student.
     */
    public void EnrollmentTableRefresh() {
        int studentID = Data.studentID;

        // Retrieve all enrollments from the file
        Set<Enrollment> allEnrollments = new HashSet<>(enrollmentUtil.readEnrollmentFromFile());

        // Filter enrollments to get only those related to the selected student
        List<Enrollment> studentEnrollments = new ArrayList<>();
        for (Enrollment enrollment : allEnrollments) {
            if (enrollment.getStudentId() == studentID) {
                studentEnrollments.add(enrollment);
            }
        }

        ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList(studentEnrollments);

        enrollmentTable.getItems().clear(); // empty the table
        enrollmentTable.getItems().addAll(enrollmentList);
    }

    /**
     * Displays a congratulatory message if the selected student has a total credit of 30 and no enrollments.
     * Hides enrollment and drop course sections and shows the congratulatory message accordingly.
     */
    public void showCongratMessage(){
        int studentID = Data.studentID;
        Student selectedStudent = studentUtil.findStudentById(studentID);
        List<Enrollment> enrollments = enrollmentUtil.readEnrollmentFromFile();
        List<Enrollment> studentEnrollments = new ArrayList<>();
        // Filter out enrollments belonging to the selected student
        for (Enrollment e : enrollments) {
            if (e.getStudentId() == studentID) {
                studentEnrollments.add(e);
            }
        }
        // Check if the selected student's total credits are 30 and they have no enrollments in current trimester
        if(selectedStudent.getCredit().getTotalCredits() == 30 && studentEnrollments.isEmpty()){
            // hide enroll and drop course section
            enrollBtn.setVisible(false);
            dropBtn.setVisible(false);
            displayEnrollCourseList.setVisible(false);
            displayDropCourseList.setVisible(false);
            selectEnroll.setVisible(false);
            selectDrop.setVisible(false);
            // show congrat messages
            congratMessage.setVisible(true);
        }
    }

    /**
     * Clears the data to prepare for new entry
     */
    public void ClearField() {
        totalCreditHours.setText("");
        currentTrimesterCreditHours.setText("");
        displayEnrollCourseList.getSelectionModel().clearSelection();
        displayDropCourseList.getSelectionModel().clearSelection();
    }

    /**
     * Displays student current credits, total credits and trimester
     */
    public void creditInfo() {
        List<Student> studentList = studentUtil.readStudentFromFile();
        for (Student s : studentList) {
            if (Data.studentID == s.getId()) {
                totalCreditHours.setText(String.valueOf(s.getCredit().getTotalCredits()) + "/30");
                currentTrimesterCreditHours.setText(String.valueOf(s.getCredit().getCurrentTrimesterCredits()));
                currentTrimester.setText(String.valueOf(s.getTrimester()));
            }
        }
    }

    /**
     * Set available courses for enrollment(for enroll) and enrolled courses(for
     * drop) in combo boxes.
     */
    public void courseList() {
        // enroll combo box
        List<Course> courseAvailable = courseUtil.readCourseFromFile();

        // Get course code of all course available
        List<String> courseCodes = new ArrayList<>();
        for (Course course : courseAvailable) {
            courseCodes.add(course.getCode());
        }

        ObservableList<String> enrollList = FXCollections.observableList(courseCodes);
        displayEnrollCourseList.setItems(enrollList);

        // drop combo box
        List<Enrollment> allEnrollments = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        // Filter enrollments to get only those related to the selected student
        List<String> courseEnoll = new ArrayList<>();
        for (Enrollment e : allEnrollments) {
            if (e.getStudentId() == selectedStudent.getId()) {
                courseEnoll.add(e.getCourseCode());
            }
        }
        ObservableList<String> dropList = FXCollections.observableList(courseEnoll);
        displayDropCourseList.setItems(dropList);
    }

    /**
     * Displays past enrollment courses for the selected student by trimester.
     */
    public void showPastEnrollCourse() {
        Set<PastSubject> pastSubjects = new HashSet<>(pastSubjectUtil.readPastSubjectFromFile());
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        String tri1 = "";
        String tri2 = "";
        String tri3 = "";

        for (PastSubject p : pastSubjects) {
            if (p.getStudentId() == selectedStudent.getId()) {
                if (selectedStudent.getTrimester() == 2 && p.getTrimester() == 1) {
                    // Trimester 1 courses for trimester 2 students
                    tri1 += p.getCourseCode() + ", ";
                } else if (selectedStudent.getTrimester() == 3) {
                    // Trimester 1 and 2 courses for trimester 3 students
                    if (p.getTrimester() == 1) {
                        tri1 += p.getCourseCode() + ", ";
                    }
                    if (p.getTrimester() == 2) {
                        tri2 += p.getCourseCode() + ", ";
                    }
                    if (p.getTrimester() == 3) {
                        tri3 += p.getCourseCode() + ", ";
                    }
                }
            }
        }
        // Set the text for trimester 1 courses
        if (!tri1.isEmpty()) {
            trimester1_course.setVisible(true);
            trimester1_course.setText("Trimester 1: " + tri1.substring(0, tri1.length() - 2));
            // Remove the last comma and space ", "

        }
        // Set the text for trimester 2 courses
        if (!tri2.isEmpty()) {
            trimester2_course.setVisible(true);
            trimester2_course.setText("Trimester 2: " + tri2.substring(0, tri2.length() - 2));
        }
        // Set the text for trimester 3 courses
        if (!tri3.isEmpty()) {
            trimester3_course.setVisible(true);
            trimester3_course.setText("Trimester 3: " + tri3.substring(0, tri3.length() - 2));
        }
    }

    // ---------------------------------ViewPast,CurrentFutureSubject----------------------
    /**
     * Displays past enrollment table with data of past subject for the selected
     * student.
     */
    public void pastSubjectTable() {

        List<PastSubject> allPastEnroll = pastSubjectUtil.readPastSubjectFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        // Filter past enrollments to include only those belonging to the selected
        // student
        List<PastSubject> pastEnroll = new ArrayList<>();
        for (PastSubject enroll : allPastEnroll) {
            if (enroll.getStudentId() == selectedStudent.getId()) {
                pastEnroll.add(enroll);
            }
        }

        ObservableList<PastSubject> pastSubjectList = FXCollections.observableArrayList(pastEnroll);

        past_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        past_col_name.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
        past_col_credit.setCellValueFactory(new PropertyValueFactory<>("credits"));

        pastSubjectTable.setItems(pastSubjectList);
    }

    /**
     * Displays current enrollment table with data of current subject for the
     * selected student.
     */
    public void currentSubjectTable() {

        List<Enrollment> allCurrentEnroll = enrollmentUtil.readEnrollmentFromFile();
        Student selectedStudent = studentUtil.findStudentById(Data.studentID);

        // Filter enrollments to include only those belonging to the selected student
        List<Enrollment> currentEnroll = new ArrayList<>();
        for (Enrollment enroll : allCurrentEnroll) {
            if (enroll.getStudentId() == selectedStudent.getId()) {
                currentEnroll.add(enroll);
            }
        }

        ObservableList<Enrollment> currentSubjectList = FXCollections.observableArrayList(currentEnroll);

        current_col_code.setCellValueFactory(new PropertyValueFactory<>("CourseCode"));
        current_col_name.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
        current_col_credit.setCellValueFactory(new PropertyValueFactory<>("Credit"));

        currentSubjectTable.setItems(currentSubjectList);
    }

    /**
     * Displays future enrollment table with data of future subject for the selected
     * student.
     */
    public void futureSubjectTable() {

        // Retrieve the list of all courses from courseTable
        ObservableList<Course> allCourses = courseTable.getItems();

        // Retrieve the list of courses from pastSubjectTable and currentSubjectTable
        List<String> pastEnrolledCourses = new ArrayList<>();
        for (PastSubject pastSubject : pastSubjectTable.getItems()) {
            pastEnrolledCourses.add(pastSubject.getCourseCode());
        }

        List<String> currentEnrolledCourses = new ArrayList<>();
        for (Enrollment enrollment : currentSubjectTable.getItems()) {
            currentEnrolledCourses.add(enrollment.getCourseCode());
        }

        // Combine past and current enrolled courses
        Set<String> enrolledCourses = new HashSet<>(pastEnrolledCourses);
        enrolledCourses.addAll(currentEnrolledCourses);

        // Filter out the courses that are not present in the past or current
        // enrollments
        List<Course> futureCourses = new ArrayList<>();
        for (Course course : allCourses) {
            if (!enrolledCourses.contains(course.getCode())) {
                futureCourses.add(course);
            }
        }

        // Displays the table with the filtered list of courses
        ObservableList<Course> futureSubjectList = FXCollections.observableArrayList(futureCourses);

        future_col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
        future_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        future_col_credit.setCellValueFactory(new PropertyValueFactory<>("credit"));

        futureSubjectTable.setItems(futureSubjectList);
    }

    /**
     * Switches between different dashboards based on the ActionEvent.
     * 
     * @param event Event triggered by the user interaction.
     */
    public void switchForm(ActionEvent event) {
        // switch to different dashboard
        if (event.getSource() == addDropCourseBtn) {
            AddDropCourse.setVisible(true);
            viewSubjectList.setVisible(false);
        } else if (event.getSource() == viewSubjectListBtn) {
            AddDropCourse.setVisible(false);
            viewSubjectList.setVisible(true);
            pastSubjectTable();
            currentSubjectTable();
            futureSubjectTable();
        }

        // switch to another trimester
        if (event.getSource() == changeTrimesterBtn) {
            try {
                Parent dialogLayout = FXMLLoader.load(getClass().getResource("studentChangeTrimester.fxml"));
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setTitle("Select Trimester");
                dialogStage.setScene(new Scene(dialogLayout));
                dialogStage.showAndWait(); // Show the dialog and wait for it to be closed

                showPastEnrollCourse();
                EnrollmentTableRefresh();
                creditInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // sign out
        if (event.getSource() == signOutBtn) {
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.close();
            // sign in page appear
            try {
                Parent root = FXMLLoader.load(getClass().getResource("studentSignIn.fxml"));
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
     * Initializes the student screen interface.
     * 
     * @param location  The URL location of the FXML file.
     * @param resources The resources bundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayStudentInfo();
        // register course dashboard
        courseTable();
        enrollmentTable();
        showPastEnrollCourse();
        courseList();
        creditInfo();

        // view subject list dashboard
        pastSubjectTable();
        currentSubjectTable();
        futureSubjectTable();

        // not allow to add and drop after complete course
        showCongratMessage();
    }
}
