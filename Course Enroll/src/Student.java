/**
 * The Student class represents an student user, extending the User class.
 * It inherits fields and methods from the User class.
 */
public class Student extends User {

  private StudentCredit credit;
  private int trimester;

  /**
   * Create an student with the specified id, name, password, current trimester, current triemster credit and total credits.
   * 
   * @param id id of student 
   * @param name name of student 
   * @param password password of student 
   * @param trimester current trimester of student
   * @param currentTrimesterCredits current trimester credits of student
   * @param totalCredits total credits of student
   */
  public Student(int id, String name, String password, int trimester, int currentTrimesterCredits, int totalCredits) {
    super(id, name, password);
    this.trimester = trimester;
    this.credit = new StudentCredit(currentTrimesterCredits, totalCredits);
    credit.setCurrentTrimesterCredits(currentTrimesterCredits);
    credit.setTotalCredits(totalCredits);
  }

  /**
   * Set new trimester of student
   * 
   * @param trimester new trimester of student
   */
  public void setTriemster(int trimester) {
    this.trimester = trimester;
  }

  /**
   * Return current trimester of student
   * 
   * @return current trimester of student
   */
  public int getTrimester() {
    return trimester;
  }

  /**
     * Get the StudentCredit object.
     *
     * @return the StudentCredit object
     */
    public StudentCredit getCredit() {
      return credit;
  }

  /**
     * Returns a CSV string representation of the student object.
     * The string contains the student information inherited from the user class along with trimester, current trimester credits and total credits.
     * 
     * @return a CSV string representation of the student object
     */
  public String toCSVString() {
    return super.toCSVString() + ",\t" + trimester + ",\t" + credit.getCurrentTrimesterCredits() + ",\t" + credit.getTotalCredits();
  }
}