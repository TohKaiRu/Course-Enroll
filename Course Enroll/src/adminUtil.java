import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The `adminUtil` class provides utility methods for handling admins.
 * It includes methods for finding the index of a admin in a list,
 * reading admins from a file, saving a list of admins to a file, and other related functionalities.
 */
public class adminUtil {
    /**
     * The path to the CSV file containing admin information.
     */
    public static final String adminFile = "src/admins.csv";

    /**
     * Finds the index of a admin in the given list based on its admin ID.
     *
     * @param adminID The admin ID to search for.
     * @param admins  The list of admins to search within.
     * @return The index of the admin in the list, or -1 if not found.
     */
    public static int indexOf(int adminID, List<Admin> admins) {
        for (int i = 0; i < admins.size(); i++)
            if (adminID == admins.get(i).getId())
                return i; // found at index i
        return -1; // not found
    }

    /**
     * Reads admin information from the CSV file and returns a list of admin objects.
     *
     * @return A list of Admin objects representing the admins read from the file.
     */
    public static List<Admin> readAdminFromFile() {
        List<Admin> admins = new ArrayList<>();

        try {
            // read admins.csv into a list of lines.
            List<String> lines = Files.readAllLines(Path.of(adminFile));
            for (int i = 0; i < lines.size(); i++) {
                // split a line by comma
                String[] items = lines.get(i).split(",");
                int id = Integer.parseInt(items[0]); // convert String to int
                // items[1] is name, items[2] is password
                admins.add(new Admin(id, items[1], items[2]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return admins;
    }

    /**
     * Saves a list of Admin objects to the CSV file.
     *
     * @param admins The list of Admin objects to be saved.
     * @throws IOException If an I/O error occurs during the file writing process.
     */
    public static void saveAdminListToFile(List<Admin> admins) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < admins.size(); i++)
            sb.append(admins.get(i).toCSVString() + "\n");
        Files.write(Path.of(adminFile), sb.toString().getBytes());
    }
}
