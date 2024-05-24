/**
 * The Course class represents a course offer in the program.
 */
public class Course {
    private int courseCredit;
    private String courseCode;
    private String courseName;
    private String courseLecturer;
    private String coursePR;

    /**
     * Create a course with specified code, name, credit, lecturer and prerequisite
     * 
     * @param courseCode     code of the course
     * @param courseName     name of the course
     * @param courseCredit   credit of the course
     * @param courseLecturer lecturer of the course
     * @param coursePR       prerequisite of the course
     */
    public Course(String courseCode, String courseName, int courseCredit, String courseLecturer, String coursePR) {
        this.courseCredit = courseCredit;
        this.courseCode = courseCode.trim();
        this.courseName = courseName.trim();
        this.courseLecturer = courseLecturer;
        this.coursePR = coursePR.trim();
    }


    /**
     * Return code of the course
     * 
     * @return code of the course
     */
    public String getCode() {
        return courseCode;
    }

    /**
     * Return name of the course
     * 
     * @return name of the course
     */
    public String getName() {
        return courseName;
    }

    /**
     * Return credit of the course
     * 
     * @return credit of the course
     */
    public int getCredit() {
        return courseCredit;
    }

    /**
     * Return lecturer(s) of the course
     * 
     * @return lecturer(s) of the course
     */
    public String getLecturerName() {
        return courseLecturer.replaceAll("/", ", ");
    }


    /**
     * Return prerequisite of the course
     * 
     * @return prerequisite of the course
     */
    public String getPR() {
        return coursePR.replaceAll("/", ", ");
    }

    /**
     * Set the code of the course
     * 
     * @param courseCode new code of the course
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.trim();
    }

    /**
     * Set the name of the course
     * 
     * @param courseName new name of the course
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName.trim();
    }

    /**
     * Set the credit of the course
     * 
     * @param courseCredits new credit of the course
     */
    public void setCredits(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    /**
     * Set the lecturer(s) of the course
     * 
     * @param courseLecturer new lecturer(s) of the course
     */
    public void setLecturerName(String courseLecturer) {
        this.courseLecturer = courseLecturer.trim();
    }

    /**
     * Set the prerequisite of the course
     * 
     * @param coursePR new prerequisite of the course
     */
    public void setPR(String coursePR) {
        this.coursePR = coursePR.trim();
    }

    /**
     * Returns a CSV string representation of the course object.
     * The string contains the code, name, credit, lecturer and prerequisite of the course separated by commas.
     * 
     * @return a CSV string representation of the course object
     */
    public String toCSVString() {
        return courseCode + ",\t" + courseName + ",\t" + courseCredit + ",\t " + courseLecturer + ",\t" + coursePR;
    }
}