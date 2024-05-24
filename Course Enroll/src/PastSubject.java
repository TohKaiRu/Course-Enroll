/**
 * The PastSubject class represents course that enrolled by student in previous trimester
 */
public class PastSubject {
    private int studentId;
    private int trimester;
    private int credits;
    private String courseCode;

    /**
     * Create a past subject with specified student id, trimester enrolled, course credits and course code
     * 
     * @param studentID id of student
     * @param trimester trimester that student enrolled the course
     * @param credits credit of the enrolled course 
     * @param courseCode code of the enrolled course
     */
    public PastSubject(int studentID, int trimester, int credits, String courseCode) {
        this.studentId = studentID;
        this.trimester = trimester;
        this.credits = credits;
        this.courseCode = courseCode;
    }

    /**
     * Return id of student
     * 
     * @return id of student
     */
    public int getStudentId() {
        return studentId;
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
     * Return trimester that student enrolled the course
     * 
     * @return trimester that student enrolled the course
     */
    public int getTrimester() {
        return trimester;
    }

    /**
     * Return credit of enrolled course
     * 
     * @return credit of enrolled course
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Return code of enrolled course
     * 
     * @return code of enrolled course
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Returns a CSV string representation of the past subject object.
     * The string contains student id, trimester, course credit and course code of past subejct.
     * 
     * @return a CSV string representation of the past subject object
     */
    public String toCSVString() {
        return studentId + ",\t" + trimester + ",\t" + credits + ",\t" + courseCode;
    }

}
