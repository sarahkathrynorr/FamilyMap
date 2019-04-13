package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.UserDatabaseAccess;
import src.Model.User;
import org.junit.*;

import static org.junit.Assert.*;

public class UserDatabaseAccessTest {
    private UserDatabaseAccess users;
    private Database myDb;

    private String username = "test";
    private String email = "test@test.com";
    private String password = "test";
    private String firstName = "Your";
    private String lastName = "Mom";
    private String personId = "test";
    private String gender = "F";

    @Before
    public void setUp() throws DataAccessException {
        myDb = new Database();
        myDb.openConnection();
        users = new UserDatabaseAccess(myDb.getConn());
    }

    //Good test to add a single user should return true
    @Test
    public void testAddOneUser() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        boolean result;
        result = users.addUser(inputUser);
        assertTrue(result);
    }

    //Bad test to add a user with a gender that isn't M or F
    @Test
    public void badTestAddOneUserGender() {
        User inputUser = new User(username, password, email, firstName, lastName, "nope", personId);
        boolean result;
        try {
            result = users.addUser(inputUser);
            assertTrue(result);
        } catch (src.DAO.DataAccessException e) {
            String expectedMsg = "User gender must be M or F";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //Bad test to add a user that already exists in the database
    @Test
    public void badTestAddOneUSer() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        boolean result;
        users.addUser(inputUser);
        try {
            result = users.addUser(inputUser);
            assertTrue(result);
        } catch (src.DAO.DataAccessException e) {
            String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column Username is not unique)";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to return a user put into the db, should return a user object
    @Test
    public void testGetOneUser() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        users.addUser(inputUser);

        User result = users.getUser(username);
        assertEquals(inputUser, result);
    }

    //bad test to get a user that has a username that is not in the database
    @Test
    public void badTestGetOneUser() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        users.addUser(inputUser);

        String badUsername = "notinDB";
        try {
            User result = users.getUser(badUsername);
            assertEquals(inputUser, result);
        } catch (DataAccessException e)
        {
            String expectedMsg = "User not found in database";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to see if a personId can be fetched from a correct password and username
    @Test
    public void testGetPersonId() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        users.addUser(inputUser);

        String result = users.findPersonId(username, password);
        assertEquals(personId, result);
    }

    //bad test to see if exception will be thrown if incorrect username is given
    @Test
    public void badTestGetPersonIdUsername() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        users.addUser(inputUser);

        try {
            users.findPersonId("not Julie", password);
        } catch (DataAccessException e) {
            String expectedMessage = "Error username or Password is incorrect";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    //bad test to see if exception will be thrown if incorrect password is given
    @Test
    public void badTestGetPersonIdPassword() throws DataAccessException {
        User inputUser = new User(username, password, email, firstName, lastName, gender, personId);
        users.addUser(inputUser);

        try {
            users.findPersonId(username, "not Julie's password");
        } catch (DataAccessException e) {
            String expectedMessage = "Error username or Password is incorrect";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException{
        myDb.closeConnection(true);
        myDb.clearTables();
    }
}

