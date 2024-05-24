/**
 * The Lecturer class represents an lecturer user, extending the User class.
 * It inherits fields and methods from the User class.
 */
public class Lecturer extends User {

  /**
   * Create an lecturer with the specified id, name and password.
     * 
     * @param id id of the lecturer
     * @param name name of the lecturer
     * @param password password of the lecturer
   */
  public Lecturer(int id, String name, String password) {
    super(id, name, password);
  }
}
