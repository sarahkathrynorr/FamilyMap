package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.Model.Event;
import src.Model.Person;
import src.Model.User;
import src.RequestResponse.LoadRequest;
import src.RequestResponse.LoadResponse;
import src.Services.LoadServices;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LoadServicesTest {
    private LoadServices loadServices = new LoadServices();
    private LoadRequest loadRequest;
    private ArrayList<User> users;
    private ArrayList<Event> events;
    private ArrayList<Person> persons;

    @Before
    public void setUp() {

        User user1 = new User("test1", "test1","test1", "test1", "test1", "f", "test1");
        User user2 = new User("test2", "test2","test2", "test2", "test2", "m", "test2");
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Event event1 = new Event("event1", "event1", "event1", 0.00, 0.00,"event1", "event1", "event1", 2000);
        Event event2 = new Event("event2", "event2", "event2", 0.00, 0.00,"event2", "event2", "event2", 2000);
        Event event3 = new Event("event3", "event3", "event3", 0.00, 0.00,"event3", "event3", "event3", 2000);
        events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);

        Person person1 = new Person("person1", "person1", "person1", "person1", "f", "person1", "person1", "person1");
        Person person2 = new Person("person2", "person2", "person2", "person2", "f", "person2", "person2", "person2");
        Person person3 = new Person("person3", "person3", "person3", "person3", "f", "person3", "person3", "person3");
        Person person4 = new Person("person4", "person4", "person4", "person4", "f", "person4", "person4", "person4");
        persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);

        loadRequest = new LoadRequest(users, persons, events);
    }

    //testing a good load with 2 users, 3 events, and 4 people
    @Test
    public void testLoad() throws DataAccessException {
        String expectedMsg = "Successfully added 2 users, 4 persons, and 3 events to the database.";
        LoadResponse loadResponse = loadServices.load(loadRequest);

        assertEquals(expectedMsg, loadResponse.getMessage());
    }

    //testing a bad load with duplicate event
    @Test
    public void badTestLoadEvent() {
        Event evilEvent3 = new Event("event3", "event3", "event3", 0.00, 0.00,"event3", "event3", "event3", 2000);
        events.add(evilEvent3);

        loadRequest = new LoadRequest(users, persons, events);

        try {
            loadServices.load(loadRequest);
        } catch (DataAccessException e) {
            String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column EventId is not unique)";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //testing a bad load with duplicate person
    @Test
    public void badTestLoadPerson() {
        Person evilPerson1 = new Person("person1", "person1", "person1", "person1", "f", "person1", "person1", "person1");
        persons.add(evilPerson1);

        loadRequest = new LoadRequest(users, persons, events);

        try {
            loadServices.load(loadRequest);
        } catch (DataAccessException e) {
            String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column PersonID is not unique)";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //testing a bad load with duplicate user
    @Test
    public void badTestLoadUser() {
        User evilUser2 = new User("test2", "test2","test2", "test2", "test2", "m", "test2");
        users.add(evilUser2);

        loadRequest = new LoadRequest(users, persons, events);

        try {
            loadServices.load(loadRequest);
        } catch (DataAccessException e) {
            String expectedMsg = "[SQLITE_CONSTRAINT]  Abort due to constraint violation (column Email is not unique)";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
    }
}
