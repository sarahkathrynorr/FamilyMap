package src.Tests;

import src.DAO.AuthTokenDatabaseAccess;
import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.EventsDatabaseAccess;
import src.Model.AuthToken;
import src.Model.Event;
import src.RequestResponse.EventResponse;
import src.RequestResponse.EventsResponse;
import src.Services.EventsServices;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EventsServicesTest {
    private EventsServices eventsServices = new EventsServices();
    private Event event;

    private String eventId = "test";
    private String descendant = "test";
    private String personID = "test";
    private double latitude = 0.00;
    private double longitude = 0.00;
    private String country = "USA";
    private String city = "Provo";
    private String eventType = "wrote these tests";
    private int year = 2019;
    private String authTokenInput = "test";

    @Before
    public void setUp() throws DataAccessException {
        eventId = "test";
        descendant = "test";
        personID = "test";
        latitude = 10;
        longitude = 100;
        country = "USA";
        city = "Provo";
        eventType = "wrote these tests";
        year = 2019;
        authTokenInput = "test";

        event = new Event(eventId, descendant, personID, latitude, longitude, country, city, eventType, year);

        AuthToken authToken = new AuthToken(descendant, personID, authTokenInput);

        Database myDb = new Database();
        myDb.clearTables();
        myDb.createTables();
        myDb.openConnection();
        EventsDatabaseAccess eventDAO = new EventsDatabaseAccess(myDb.getConn());
        eventDAO.addEvent(event);

        AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());
        authTokenDAO.addAuthToken(authToken);

        myDb.closeConnection(true);
    }

    //good test to see if an event can be fetched
    @Test
    public void testGetEvent() throws DataAccessException {

        EventResponse expected = new EventResponse(event);
        EventResponse actual = eventsServices.getEvent(eventId, authTokenInput);
        assertEquals(expected, actual);
    }

    //bad test to see if an event is fetched with a bad authToken
    @Test
    public void badTestGetEventAuthToken() throws DataAccessException {

        EventResponse actual = eventsServices.getEvent(eventId, "nope");
        assertNull(actual.getEvent());
    }

    //bad test to see if an event is fetched with a bad eventId
    @Test
    public void badTestGetEventEventId() throws DataAccessException {

        EventResponse actual = eventsServices.getEvent("nope", authTokenInput);
        assertNull(actual.getEvent());
    }

    //good test to get all the events in a database for a specific user
    @Test
    public void testGetAllEvents() throws DataAccessException {

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(event);

        EventsResponse actual = eventsServices.getAllEvents(authTokenInput);
        assertEquals(event, actual.getEventsArray().toArray()[0]);
    }

    //bad test: fails to get all the events in a database for a bad authToken
    @Test
    public void badTestGetAllEvents() {

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(event);

        try {
            eventsServices.getAllEvents("not a good authToken");
        } catch (DataAccessException e) {
            String expectedMsg = "Authorization Token not found";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
    }

}
