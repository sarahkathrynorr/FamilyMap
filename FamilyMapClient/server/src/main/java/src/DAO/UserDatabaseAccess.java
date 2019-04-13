package src.DAO;

import src.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Users Database Access Object -
 * Here we will be able to update table rows,
 * delete rows,
 * or create new rows for various users
 */

public class UserDatabaseAccess {
    private Connection conn;

    public UserDatabaseAccess(Connection conn) {
        this.conn = conn;
    }

    /**
     * Add a new row in the user table
     *
     * @param userDetails - object body that contains:
     *  username - the username has to a unique non-null string
     *  password - the password is also non-null
     *  firstName - the first name of the new user, also non-null
     *  lastName - the last name of the new user, non-null
     *  gender - the gender ("m" or "f") non-null
     *
     * - if the user does not already exist as a person in the person table they are added using this information
     *               and a personId is assigned using the addPerson function from the PersonDatabaseAccess class
     * calls AuthTokenDatabaseAccess addAuthToken(username)
     * @throws DataAccessException
     *
     * @return object body of new user that was just added
     */
    public boolean addUser(User userDetails) throws DataAccessException {
        if (!userDetails.getGender().toUpperCase().equals("M") && !userDetails.getGender().toUpperCase().equals("F")) {
            throw new DataAccessException("User gender must be M or F");
        }
        boolean commit = true;

        String sql = "INSERT INTO User (Username, Password, FirstName, LastName, Email, " +
                "Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, userDetails.getUsername());
            stmt.setString(2, userDetails.getPassword());
            stmt.setString(3, userDetails.getFirstName());
            stmt.setString(4, userDetails.getLastName());
            stmt.setString(5, userDetails.getEmail());
            stmt.setString(6, userDetails.getGender().toUpperCase());
            stmt.setString(7, userDetails.getPersonId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        return commit;
    }

    /**
     * Login User
     * @param username - string
     *
     * @throws DataAccessException
     *
     * @return user object with specific username and password
     */
    public User getUser(String username) throws DataAccessException {
        User user = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            }
            else {
                throw new DataAccessException("User not found in database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while finding user");
        }
    }

    public String findPersonId(String userName, String password) throws DataAccessException {
        String personId = null;
        ResultSet rs = null;
        java.lang.String sql = "SELECT PersonID FROM User WHERE Username = ? AND Password = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                personId = rs.getString("PersonID");
                return personId;
            }
            else return null;
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error username or Password is incorrect");
        }
    }
}
