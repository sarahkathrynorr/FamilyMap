package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.UserDatabaseAccess;
import src.Model.User;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;
import src.Services.LoginServices;
import org.junit.*;


import static org.junit.Assert.*;

public class LoginServicesTest {
    private LoginServices loginServices = new LoginServices();

    private String userName = "test";
    private String password = "test";

    @Before
    public void setUp() throws DataAccessException {
        User user = new User(userName, password, "test", "test", "Test", "f", "test");

        Database myDb = new Database();
        myDb.clearTables();
        myDb.createTables();
        myDb.openConnection();

        UserDatabaseAccess userDAO = new UserDatabaseAccess(myDb.getConn());
        userDAO.addUser(user);

        myDb.closeConnection(true);

    }

    //good test for logging in a correct user
    @Test
    public void testLogin() throws DataAccessException {
        RequestLogin requestLogin = new RequestLogin(userName, password);

        ResponseLogin responseLogin = loginServices.login(requestLogin);

        assertEquals(userName, responseLogin.getUserName());
    }

    //bad test for logging in with an incorrect username
    @Test
    public void badTestLoginUsername() throws DataAccessException {
        RequestLogin requestLogin = new RequestLogin("nope", password);

        ResponseLogin responseLogin = loginServices.login(requestLogin);

        assertNull(responseLogin.getUserName());
    }

    //bad test for logging in with an incorrect password
    @Test
    public void badTestLoginPassword() throws DataAccessException {
        RequestLogin requestLogin = new RequestLogin(userName, "wutwut");

        ResponseLogin responseLogin = loginServices.login(requestLogin);

        assertNull(responseLogin.getUserName());
    }

    @After
    public void cleanUp() throws DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
    }
}
