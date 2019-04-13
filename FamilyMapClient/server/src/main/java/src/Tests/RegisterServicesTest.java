package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.RequestResponse.UserRequest;
import src.RequestResponse.UserResponse;
import src.Services.RegisterServices;
import org.junit.*;

import static org.junit.Assert.*;

public class RegisterServicesTest {
    RegisterServices registerServices = new RegisterServices();

    private String userName = "test";
    private String password = "test";
    private String email = "test";
    private String firstName = "test";
    private String lastName = "test";
    private String gender = "m";

    //good test to see if you can register a user
    @Test
    public void testRegisterUser() throws DataAccessException {
        UserRequest userRequest = new UserRequest(userName, password, email, firstName,lastName, gender);
        UserResponse userResponse = registerServices.registerUser(userRequest);

        assertEquals(userName, userResponse.getUsername());
    }

    //bad test fails to register a user with null username
    @Test
    public void badTestRegisterUserName() throws DataAccessException {
        UserRequest userRequest = new UserRequest(null, password, email, firstName, lastName, gender);
        String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (User.Username may not be NULL)";

        UserResponse userResponse = registerServices.registerUser(userRequest);
        assertEquals(expectedMsg, userResponse.getMessage());
    }

    //bad test fails to register that is already in the database
    @Test
    public void badTestRegisterDuplicate() throws DataAccessException {
        UserRequest userRequest = new UserRequest(userName, password, email, firstName, lastName, gender);
        String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column Username is not unique)";

        registerServices.registerUser(userRequest);
        UserResponse userResponse = registerServices.registerUser(userRequest);

        assertEquals(expectedMsg, userResponse.getMessage());
    }

    @After
    public void cleanUp() throws DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
    }
}
