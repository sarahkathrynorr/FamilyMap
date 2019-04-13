package src.Tests;

import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.PersonDatabaseAccess;
import src.Model.Person;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PersonDatabaseAccessTest {
    private PersonDatabaseAccess persons;
    private Database myDb;


    private String personId = "test123";
    private String descendant = "Me";
    private String firstName = "Mr.";
    private String lastName = "Test";
    private String gender = "M"; //test for M or F and nothing else
    private String father = "testdad";
    private String mother = "testmom";
    private String spouse = "Richard";

    private Person expectedPerson = new Person(personId, descendant, firstName, lastName,
            gender, father, mother, spouse);

    @Before
    public void setUp() throws DataAccessException {
        //myDb.closeConnection(true);
        myDb = new Database();
        myDb.openConnection();
        persons = new PersonDatabaseAccess(myDb.getConn());
    }

    //Good test to add a single person should return true
    @Test
    public void testAddOnePerson() throws DataAccessException {
        Person inputPerson = new Person(personId, descendant, firstName, lastName,
                gender, father, mother, spouse);
        boolean result;
        result = persons.addPerson(inputPerson);
        assertTrue(result);
    }

    //Bad test to add a person with a gender that isn't M or F
    @Test
    public void badTestAddOnePersonGender() {
        Person inputPerson = new Person(personId, descendant, firstName, lastName,
                "nope", father, mother, spouse);
        boolean result;
        try {
            result = persons.addPerson(inputPerson);
            assertTrue(result);
        } catch (src.DAO.DataAccessException e) {
            String expectedMsg = "Person gender must be M or F";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //Bad test to add a person that already exists in the database
    @Test
    public void badTestAddOnePerson() throws DataAccessException {
        Person inputPerson = new Person(personId, descendant, firstName, lastName,
                gender, father, mother, spouse);
        boolean result;
        persons.addPerson(inputPerson);
        try {
            result = persons.addPerson(inputPerson);
            assertTrue(result);
        } catch (src.DAO.DataAccessException e) {
            String expectedMsg = "Error inserting new Person into the database";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to return a person put into the db, should return a person object
    @Test
    public void testGetOnePerson() throws DataAccessException {
        Person inputPerson = new Person(personId, descendant, firstName, lastName,
                gender, father, mother, spouse);
        persons.addPerson(inputPerson);

        Person result = persons.getPerson(personId, descendant);
        assertEquals(expectedPerson, result);
    }

    //bad test to get one person that has an id not found in db should throw an exception
    @Test
    public void badTestGetOnePerson() throws DataAccessException {
        Person inputPerson = new Person(personId, descendant, firstName, lastName,
                gender, father, mother, spouse);
        persons.addPerson(inputPerson);

        String badId = "notinDB";
        try {
            Person result = persons.getPerson(badId, descendant);
            assertEquals(inputPerson, result);
        } catch (DataAccessException e)
        {
            String expectedMsg = "Person not found in database";
            assertEquals(expectedMsg, e.getMessage());
        }
    }

    //good test to get all the people associate with a specific user
    @Test
    public void testGetAllPersons() throws DataAccessException {
        Person inputPerson1 = new Person("test0", descendant, "test0", "test0",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson1);

        Person inputPerson2 = new Person("test1", descendant, "test1", "test1",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson2);

        Person inputPerson3 = new Person("test2", "Julie", "test2", "test2",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson3);

        Person inputPerson4 = new Person("test3", descendant, "test3", "test3",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson4);

        ArrayList<Person> expected = new ArrayList<>();
        expected.add(inputPerson1);
        expected.add(inputPerson2);
        expected.add(inputPerson4);

        ArrayList<Person> results;
        results = persons.getAllPersons(descendant);

        assertEquals(expected, results);

    }

    //Bad test for getting all the people associate with a username the DNE - should return an exception
    @Test
    public void badTestGetAllPersons() throws DataAccessException {
        Person inputPerson1 = new Person("test0", descendant, "test0", "test0",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson1);

        Person inputPerson2 = new Person("test1", descendant, "test1", "test1",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson2);

        Person inputPerson3 = new Person("test2", "Julie", "test2", "test2",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson3);

        Person inputPerson4 = new Person("test3", descendant, "test3", "test3",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson4);

        ArrayList<Person> expected = new ArrayList<>();
        expected.add(inputPerson1);
        expected.add(inputPerson2);
        expected.add(inputPerson4);

        ArrayList<Person> results;
        try {
            results = persons.getAllPersons("NotInDb");
            assertEquals(expected, results);
        } catch (DataAccessException e) {
            String expectedMsg = "No people found in Database to return";
            assertEquals(expectedMsg, e.getMessage());
        }

    }

    //good test to see if all the persons associated with a descendant will be removed
    @Test
    public void testRemoveAllPersons() throws DataAccessException {
        Person inputPerson1 = new Person("test0", descendant, "test0", "test0",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson1);

        Person inputPerson2 = new Person("test1", descendant, "test1", "test1",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson2);

        Person inputPerson3 = new Person("test2", "Julie", "test2", "test2",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson3);

        Person inputPerson4 = new Person("test3", descendant, "test1", "test3",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson4);

        boolean result;
        result = persons.removeAllPersons(descendant);

        assertTrue(result);
    }

    //bad test to see if all the persons associated with a false descendant will not be removed
    @Test
    public void badTestRemoveAllPersons() throws DataAccessException {
        Person inputPerson1 = new Person("test0", descendant, "test0", "test0",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson1);

        Person inputPerson2 = new Person("test1", descendant, "test1", "test1",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson2);

        Person inputPerson3 = new Person("test2", "Julie", "test2", "test2",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson3);

        Person inputPerson4 = new Person("test3", descendant, "test1", "test3",
                gender, father, mother, spouse);
        persons.addPerson(inputPerson4);

        try {
            persons.removeAllPersons("not Julie");
        } catch (DataAccessException e) {
            String expectedMessage = "Error encountered while removing persons";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @After
    public void cleanUp() throws DataAccessException{
        myDb.closeConnection(false);
        myDb.clearTables();
    }

}

