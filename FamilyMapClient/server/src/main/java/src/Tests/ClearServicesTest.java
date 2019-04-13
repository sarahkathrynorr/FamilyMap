package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.RequestResponse.ClearResponse;
import src.Services.ClearServices;
import org.junit.*;


import static org.junit.Assert.*;

public class ClearServicesTest {
    private ClearServices clearServices = new ClearServices();

    //good test to see if everything gets cleared
    @Test
    public void testClearEverything() throws DataAccessException {
        ClearResponse clearResponse = clearServices.clearEverything();
        ClearResponse expected = new ClearResponse();

        assertEquals(expected.getResponse(), clearResponse.getResponse());
    }

    //bad test to see if there is an error with the database being closed?
    @Test
    public void badTestClearEverything() throws DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();

        try {
            clearServices.clearEverything();
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
    }
}
