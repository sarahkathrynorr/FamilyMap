package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.UserDatabaseAccess;
import src.Model.User;
import src.RequestResponse.FillResponse;
import src.Services.FillServices;
import org.junit.*;

import static org.junit.Assert.*;

public class FillServicesTest {
    private FillServices fillServices = new FillServices();
    private String userName;
    Database myDb = new Database();

    @Before
    public void setUp() throws DataAccessException {
        try {
            userName = "test";

            String password = "test";
            String email = "email";
            String firstName = "firstName";
            String lastName = "lastName";
            String gender = "m";
            String personId = "personId";

            User user = new User(userName, password, email, firstName, lastName, gender, personId);

            myDb.openConnection();

            UserDatabaseAccess userDAO = new UserDatabaseAccess(myDb.getConn());
            userDAO.addUser(user);

            myDb.closeConnection(true);
        } catch (DataAccessException e) {
            myDb.closeConnection(false);
        }
    }

    //testing 5 generations
    @Test
    public void testFill5() throws DataAccessException {
        String expectedMessage = "Successfully added 63 persons and 187 events to the database.";
        FillResponse fillResponse = fillServices.fill(userName, 5);

        assertEquals(expectedMessage, fillResponse.getMessage());
    }

    //testing 10 generations because why not
    @Test
    public void testFill10() throws DataAccessException {
        String expectedMessage = "Successfully added 2047 persons and 6139 events to the database.";
        FillResponse fillResponse = fillServices.fill(userName, 10);

        assertEquals(expectedMessage, fillResponse.getMessage());
    }

    //bad test trying to fill for a user that isn't there
    @Test
    public void badTestFill() throws DataAccessException {
        String expectedMessage = "User not found in database";
        try {
            fillServices.fill("nope", 4);
        } catch (DataAccessException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException {
        myDb.clearTables();
    }
}
