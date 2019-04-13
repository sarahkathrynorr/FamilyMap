package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.EventsDatabaseAccess;
import src.Model.Event;
import org.junit.*;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class EventDatabaseAccessTest {
    private EventsDatabaseAccess events;
    private Database myDb;

        String eventID = "test";
        String personID = "test";
        String descendant = "test";
        String city = "test";
        String country = "test";
        String eventType = "test";
        double latitude = 0.00;
        double longitude = 0.00;
        int year = 2019;

    @Before
    public void setUp() throws DataAccessException {
        myDb = new Database();
        myDb.openConnection();
        events = new EventsDatabaseAccess(myDb.getConn());
    }

    //Good test to add a single event should return true
    @Test
    public void testAddOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        boolean result;
        result = events.addEvent(inputEvent);
        assertTrue(result);
    }

    //Bad test to add an event with a null value
    @Test
    public void badTestAddOneEventGender() {
        Event inputEvent = new Event(eventID, descendant, personID, null, longitude, country, city, eventType, year);
        boolean result;
        try {
            try {
                result = events.addEvent(inputEvent);
                assertNull(result);
            } catch (DataAccessException e) {
                //do nothing here because we are looking for a null ptr exception
            }
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    //good test to see if an event can be fetched with a username/descendant
    @Test
    public void testGetAllEvents() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        events.addEvent(inputEvent);

        ArrayList<Event> result;
        ArrayList<Event> expectedResult = new ArrayList<>();
        expectedResult.add(inputEvent);

        result = events.getAllEvents(descendant);
        assertEquals(expectedResult, result);
    }

    //bad test to see if an exception will be thrown if descendant isn't legit
    @Test
    public void badTestGetAllEvents1() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        events.addEvent(inputEvent);

        ArrayList<Event> result = new ArrayList<>();
        ArrayList<Event> expectedResult = new ArrayList<>();
        try {
            result = events.getAllEvents("nope");
        } catch (DataAccessException e) {
            assertEquals(expectedResult, result);
        }
    }

    //bad test to see if an exception will be thrown if no events exist for a descendant
    @Test
    public void badTestGetAllEvents2() {
        ArrayList<Event> result = new ArrayList<>();
        ArrayList<Event> expectedResult = new ArrayList<>();
        try {
            result = events.getAllEvents(descendant);
        } catch (DataAccessException e) {
            assertEquals(expectedResult, result);
        }
    }

    //Bad test to add an event that already exists in the database
    @Test
    public void badTestAddOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        boolean result;
        events.addEvent(inputEvent);
        try {
            result = events.addEvent(inputEvent);
            assertTrue(result);
        } catch (src.DAO.DataAccessException e) {
            String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column EventId is not unique)";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to return a event put into the db, should return a event object
    @Test
    public void testGetOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        events.addEvent(inputEvent);

        Event result = events.getEvent(eventID, descendant);
        assertEquals(inputEvent, result);
    }

    //bad test to get an event that has a event that is not in the database
    @Test
    public void badTestGetOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        events.addEvent(inputEvent);

        String badEventname = "notinDB";
        try {
            Event result = events.getEvent(badEventname, descendant);
            assertEquals(inputEvent, result);
        } catch (DataAccessException e)
        {
            String expectedMsg = "Event notinDB not found in database for test";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to remove a single event - should return true
    @Test
    public void testRemoveOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        events.addEvent(inputEvent);

        boolean result;

        result = events.removeAllEvents(descendant);
        assertTrue(result);
    }

    //bad test for removing a single event - should throw an exception
    @Test
    public void badTestRemoveOneEvent() throws DataAccessException {
        Event inputEvent = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);

        events.addEvent(inputEvent);

        boolean result;

        String badId = "notInDb";
        try {
            result = events.removeAllEvents(badId);
            assertTrue(result);
        } catch (DataAccessException e) {
            String expectedMsg = "Error encountered while removing events";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException{
        myDb.closeConnection(false);
        myDb.clearTables();
    }
}

