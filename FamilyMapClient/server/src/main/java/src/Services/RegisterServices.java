package src.Services;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.UserDatabaseAccess;
import src.Model.User;
import src.RequestResponse.*;

import java.util.UUID;


/**
 * for registering a new user and thus a new person
 */

public class RegisterServices {
    /**
     * registers a new user by calling the AddUser DAO method
     * also adds a new person if the person is not already found in the DB using the addPerson DAO method
     * Also creates new AuthToken and adds that to the table
     *
     * @param userRequest - object body that contains:
     *
     * @throws src.DAO.DataAccessException
     *
     * @return the objectBody of the user just added
     *
     */
    public UserResponse registerUser(UserRequest userRequest) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        UserDatabaseAccess users = new UserDatabaseAccess(myDb.getConn());

        String personId = createPersonId();
        User user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getEmail(),
                userRequest.getFirstName(), userRequest.getLastName(), userRequest.getGender(), personId);

        LoginServices loginServices = new LoginServices();
        RequestLogin requestLogin = new RequestLogin(user.getUsername(), user.getPassword());
        FillServices fillServices = new FillServices();

        try {
            if (users.addUser(user)) {
                myDb.closeConnection(true);
                ResponseLogin responseLogin = loginServices.login(requestLogin);
                fillServices.fill(userRequest.getUsername(), 4);

                return new UserResponse(responseLogin.getAuthToken(), responseLogin.getUserName(), personId);
            }
            else {
                myDb.closeConnection(false);
                UserResponse errorResponse = new UserResponse(null, null, null);
                errorResponse.setMessage("New user was unable to be added to database");
                return errorResponse;
            }
        } catch (DataAccessException e) {
            myDb.closeConnection(false);

            UserResponse errorResponse = new UserResponse(null, null, null);
            errorResponse.setMessage(e.getMessage());
            return errorResponse;
        }
    }

    private String createPersonId() {
        return UUID.randomUUID().toString();
    }
}
