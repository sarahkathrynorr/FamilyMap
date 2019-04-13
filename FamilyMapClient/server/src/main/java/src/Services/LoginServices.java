package src.Services;

import src.DAO.AuthTokenDatabaseAccess;
import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.UserDatabaseAccess;
import src.Model.AuthToken;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;

import java.util.UUID;

/**
 * services for logging in
 */
public class LoginServices {
    /**
     * login service - logs user in and creates new auth token
     * calls the and login DAO method and then createAuthToken DAO method
     *
     * @param requestLogin - login request object of user
     *
     * @throws src.DAO.DataAccessException
     *
     * @return Login Response object
     */
    public ResponseLogin login(RequestLogin requestLogin) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        AuthTokenDatabaseAccess authsDAO = new AuthTokenDatabaseAccess(myDb.getConn());

        String authTokenStr = generateAuthToken();


        try {
            UserDatabaseAccess usersDAO = new UserDatabaseAccess(myDb.getConn());
            String personId = usersDAO.findPersonId(requestLogin.getUsername(), requestLogin.getPassword());
            if (personId != null) {

                AuthToken authTokenObj = new AuthToken(requestLogin.getUsername(), personId, authTokenStr);

                authsDAO.addAuthToken(authTokenObj);

                myDb.closeConnection(true);
                return new ResponseLogin(authTokenStr, requestLogin.getUsername(), personId);
            }
            else {
                myDb.closeConnection(false);
                ResponseLogin errorResponse = new ResponseLogin(null, null, null);
                errorResponse.setMessage("Username or Password is incorrect");
                return errorResponse;
            }
        } catch (DataAccessException e) {
            myDb.closeConnection(false);

            ResponseLogin errorResponse = new ResponseLogin(null, null, null);
            errorResponse.setMessage(e.getMessage());
            return errorResponse;
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}