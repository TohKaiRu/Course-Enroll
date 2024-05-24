/**
 * The User class represent user in the course management system.
 */
public class User {
  private int id;
  private String name;
  private String password;

  /**
   * Create a user with specified id, name and password
   * 
   * @param id       id of the user
   * @param name     name of the user
   * @param password password of the user
   */
  public User(int id, String name, String password) {
    this.id = id;
    this.name = name.trim();
    this.password = password.trim();
  }

  /**
   * Return id of user
   * 
   * @return id of user
   */
  public int getId() {
    return id;
  }

  /**
   * Return name of user
   * 
   * @return name of user
   */
  public String getName() {
    return name;
  }

  /**
   * Return password of user
   * 
   * @return password of user
   */
  public String getPassword() {
    return password;
  }

  /**
     * Set the id of the student
     * 
     * @param id new id of the student
     */
    public void setId(int id) {
      this.id = id;
  }

  /**
     * Set the name of the student
     * 
     * @param name new name of the student
     */
    public void setName(String name) {
      this.name = name;
  }

  /**
     * Set the password of the student
     * 
     * @param password new password of the student
     */
    public void setPassword(String password) {
      this.password = password;
  }

  /**
   * Returns a CSV string representation of the user object.
   * The string contains the id, name and password of the user separated by commas.
   * 
   * @return a CSV string representation of the user object
   */
  public String toCSVString() {
    return id + ",\t" + name + ",\t" + password;
  }
}
