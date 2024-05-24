/**
 * The StudentCredit class represents the credits of a student.
 */
public class StudentCredit {
    private int currentTrimesterCredits;
    private int totalCredits;

    /**
     * Create a StudentCredit with specified current trimester credits and total credits.
     *
     * @param currentTrimesterCredits current trimester credits of student
     * @param totalCredits            total credits of student
     */
    public StudentCredit(int currentTrimesterCredits, int totalCredits) {
        this.currentTrimesterCredits = currentTrimesterCredits;
        this.totalCredits = totalCredits;
    }

    /**
     * Set new current trimester credits of student.
     *
     * @param currentTrimesterCredits new current trimester credits of student
     */
    public void setCurrentTrimesterCredits(int currentTrimesterCredits) {
        this.currentTrimesterCredits = currentTrimesterCredits;
    }

    /**
     * Set new total credits of student.
     *
     * @param totalCredits new total credits of student
     */
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * Return current trimester credits of student.
     *
     * @return current trimester credits of student
     */
    public int getCurrentTrimesterCredits() {
        return currentTrimesterCredits;
    }

    /**
     * Return total credits of student.
     *
     * @return total credits of student
     */
    public int getTotalCredits() {
        return totalCredits;
    }
}