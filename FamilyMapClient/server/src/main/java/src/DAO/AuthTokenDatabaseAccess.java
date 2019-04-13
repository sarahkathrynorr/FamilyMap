package src.DAO;

import src.Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A user can retrieve an authorization token through their username
 * used for updating, deleting, and adding to other tables
 */
public class AuthTokenDatabaseAccess {
    private Connection conn;

    public AuthTokenDatabaseAccess(Connection conn)
    {
        this.conn = conn;
    }


    /**
     * adds a new auth token with a new user
     *
     * @param tokenDetails - object body that contains:
     *  username - this is created when a new user is created
     *  personId - numeric id for person
     *  authToken - String auth Token
     *
     * @throws DataAccessException
     *
     * @return boolean success value
     */
    public boolean addAuthToken(AuthToken tokenDetails) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthToken (PersonID, Username, Auth_token) VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, tokenDetails.getPersonId());
            stmt.setString(2, tokenDetails.getUsername());
            stmt.setString(3, tokenDetails.getAuthToken());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while inserting new AuthToken into the database");
        }

        return commit;
    }

    /**
     * Retrieves the auth token according to either the username or personId
     *
     * @param authToken - String
     *
     * @return userAuthToken - Object string of the auth token if it exists
     * otherwise return null
     *
     * @throws DataAccessException
     */
    public String getUserFromAuthToken(String authToken) throws DataAccessException {
        String userName;
        ResultSet rs = null;
        String sql = "SELECT Username FROM AuthToken WHERE Auth_token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("Username");
                return userName;
            }
            else {
                throw new DataAccessException("Authorization Token not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new DataAccessException("Error encountered while finding username");
        }
    }
}
