/**
 * The Enrollment class represents an enrollemnt of a student into a course.
 */
public class Enrollment {
    private int studentId;
    private String courseCode;
    private String lecturer;
    private int credit;
    /**
     * Create an enrollment with specifiec student id, course code, lecturer and course credits
     * 
     * @param studentId id of the student
     * @param courseCode code of the course to enroll
     * @param lecturer lecturer of the course
     * @param credit credits of the course
     */
    public Enrollment(int studentId, String courseCode, String lecturer,int credit) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.lecturer = lecturer;
        this.credit = credit;
    }
    /**
     * Return id of the student
     * 
     * @return id of the student
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * Return name of the student
     * 
     * @return name of the student
     */
    public String getStudentName() {
        Student s = studentUtil.findStudentById(studentId);
        return s.getName();
    }

    /**
     * Return code of the course
     * 
     * @return code of the course
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Return name of enrolled course
     * 
     * @return name of enrolled course
     */
    public String getCourseName() {
        Course c = courseUtil.findCourseByCode(courseCode);
        return c.getName();
    }

    /**
     * Return lecturer of the course
     * 
     * @return lecturer of the course
     */
    public String getLecturer() {
        return lecturer;
    }
    
    /**
     * Return credits of the course
     * 
     * @return credits of the course
     */
    public int getCredit(){
        return credit;
    }

    /**
     * Set lecturer(s) of the course
     * @param lecturer lecturer(s) of the course
     */
    public void setLecturer(String lecturer){
        this. lecturer = lecturer;
    }

    /**
     * Returns a CSV string representation of the enrollment object.
     * The string contains enrollment information: student id, course code, lecturer and course credit.
     * 
     * @return a CSV string representation of the enrollment object
     */
    public String toCSVString() {
        return studentId + ",\t" + courseCode + ",\t" + lecturer+",\t" + credit;
    }
}
