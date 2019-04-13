package src.Services;

import src.DAO.*;
import src.Model.Person;
import src.RequestResponse.PersonResponse;
import src.RequestResponse.PersonsResponse;

import java.util.ArrayList;

/**
 * Services for adding a single person, deleting a single person, updating a single person
 * I am a single person lol
 */
public class PersonServices {

    /**
     * returns the person based on the personId sent in
     * @param personId - type: String, the Id of the person to be returned
     * @return the object response body of the person to be returned
     * @throws src.DAO.DataAccessException
     */
    public PersonResponse getPerson(String personId, String authToken) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        PersonDatabaseAccess personsDAO = new PersonDatabaseAccess(myDb.getConn());

        try {
            AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());

            String currentUserName = authTokenDAO.getUserFromAuthToken(authToken);

            Person person = personsDAO.getPerson(personId, currentUserName);
            myDb.closeConnection(true);

            return new PersonResponse(person);

        } catch (DataAccessException e) {
            myDb.closeConnection(false);

            PersonResponse errorResponse = new PersonResponse(null);
            errorResponse.setMessage(e.getMessage());
            return errorResponse;
        }
    }

    /**
     * returns all the persons in the person table associated with a user's personId
     * @return array of all the objects of persons to be returned
     * @throws src.DAO.DataAccessException
     */
    public PersonsResponse getAllPersons(String authToken) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        PersonDatabaseAccess personsDAO = new PersonDatabaseAccess(myDb.getConn());

        try {
            AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());

            String currentUserName = authTokenDAO.getUserFromAuthToken(authToken);

            ArrayList<Person> personsArray = personsDAO.getAllPersons(currentUserName);

            myDb.closeConnection(true);

            return new PersonsResponse(personsArray);

        } catch (DataAccessException e) {
            myDb.closeConnection(false);
            e.printStackTrace();

            PersonsResponse errorResponse = new PersonsResponse(null);
            errorResponse.setMessage(e.getMessage());

            return errorResponse;
        }
    }
}