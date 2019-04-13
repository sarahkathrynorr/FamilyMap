package src.Tests;

import src.DAO.AuthTokenDatabaseAccess;
import src.DAO.DataAccessException;
import src.DAO.Database;
import src.Model.AuthToken;
import org.junit.*;

import static org.junit.Assert.*;

public class AuthTokenDatabaseAccessTest {
    private AuthTokenDatabaseAccess authTokenDAO;
    private Database myDb;

    private String authToken = "test";
    private String userName = "test";
    private String personID = "test";

    @Before
    public void setUp() throws DataAccessException {
        myDb = new Database();
        myDb.openConnection();
        authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());
    }

    @Test
    public void testAddAuthToken() throws DataAccessException {
        AuthToken inputAuthToken = new AuthToken(userName, personID, authToken);
        boolean result = authTokenDAO.addAuthToken(inputAuthToken);
        assertTrue(result);
    }

    @Test
    public void badTestAddAuthToken() throws DataAccessException {
        AuthToken inputAuthToken = new AuthToken(null, personID, authToken);
        try {
            boolean result = authTokenDAO.addAuthToken(inputAuthToken);
            assertTrue(result);
        } catch (DataAccessException e) {
            String expectedMsg = "Error encountered while inserting new AuthToken into the database";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    @Test
    public void testGetCurrentUser() throws DataAccessException {
        AuthToken inputAuthToken = new AuthToken(userName, personID, authToken);
        authTokenDAO.addAuthToken(inputAuthToken);
        String outputUserName = authTokenDAO.getUserFromAuthToken(authToken);
        assertEquals(userName, outputUserName);
    }

    @Test
    public void badTestGetCurrentUser() throws DataAccessException {
        try {
            String outputUserName = authTokenDAO.getUserFromAuthToken(null);
        } catch (DataAccessException e) {
            String expectedMessage = "Authorization Token not found";
            assertEquals(expectedMessage, e.getMessage());
        }

    }


    @After
    public void cleanUp() throws DataAccessException {
        myDb.closeConnection(false);
        myDb.clearTables();
    }

}
