package src.Tests;

import src.DAO.AuthTokenDatabaseAccess;
import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.PersonDatabaseAccess;
import src.Model.AuthToken;
import src.Model.Person;
import src.RequestResponse.ErrorResponse;
import src.RequestResponse.PersonResponse;
import src.RequestResponse.PersonsResponse;
import src.Services.PersonServices;
import org.junit.*;

import static org.junit.Assert.*;

public class PersonServicesTest {
    private PersonServices personServices = new PersonServices();
    private Person person;
    private AuthToken authToken;

    @Before
    public void SetUp() throws DataAccessException {
        person = new Person("test", "test", "test", "test", "f", "test", "test", "test");
        authToken = new AuthToken("test", "test", "test");

        Database myDb = new Database();
        myDb.openConnection();

        PersonDatabaseAccess personsDAO = new PersonDatabaseAccess(myDb.getConn());
        AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());

        personsDAO.addPerson(person);
        authTokenDAO.addAuthToken(authToken);

        myDb.closeConnection(true);

    }

    //good test to get a specific person based on personId and authToken
    @Test
    public void testGetPerson() throws DataAccessException {
        PersonResponse personResponse = personServices.getPerson(person.getPersonId(), authToken.getAuthToken());
        assertEquals(person, personResponse.getPerson());
    }

    //bad test checking for bad personId
    @Test
    public void badTestGetPersonPersonId() throws DataAccessException {
        PersonResponse personResponse = personServices.getPerson("wat", authToken.getAuthToken());
        String expectedMsg = "Person not found in database";
        assertEquals(expectedMsg, personResponse.getMessage());
    }

    //bad test checking for bad personId
    @Test
    public void badTestGetPersonAuthToken() throws DataAccessException {
        PersonResponse personResponse = personServices.getPerson(person.getPersonId(), "wutwut");
        String expectedMsg = "Authorization Token not found";
        assertEquals(expectedMsg, personResponse.getMessage());
    }

    //good test to get all persons with an auth token
    @Test
    public void testGetAllPersons() throws DataAccessException {
        PersonsResponse personsResponse = personServices.getAllPersons(authToken.getAuthToken());
        assertEquals(person, personsResponse.getPersonsArray().toArray()[0]);
    }

    //bad test fails to all persons with a lame auth token
    @Test
    public void badTestGetAllPersons() throws DataAccessException {
        try {
            personServices.getAllPersons("lame auth token");
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
