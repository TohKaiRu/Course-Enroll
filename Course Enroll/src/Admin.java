/**
 * The Admin class represents an administrator user, extending the User class.
 * It inherits fields and methods from the User class.
 */
public class Admin extends User {
    /**
     * Create an admin with the specified id, name and password.
     * 
     * @param id id of the admin
     * @param name name of the admin
     * @param password password of the admin
     */
    public Admin(int id, String name, String password) {
        super(id, name, password);
    }
}