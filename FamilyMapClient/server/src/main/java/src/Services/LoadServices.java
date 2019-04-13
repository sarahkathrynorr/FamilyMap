package src.Services;

import src.DAO.*;
import src.Model.Event;
import src.Model.Person;
import src.Model.User;
import src.RequestResponse.LoadRequest;
import src.RequestResponse.LoadResponse;

import java.util.ArrayList;

public class LoadServices {
    public LoadResponse load(LoadRequest loadRequest) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.clearTables();
        myDb.createTables();

        myDb.openConnection();
        UserDatabaseAccess userDAO = new UserDatabaseAccess(myDb.getConn());
        EventsDatabaseAccess eventDAO = new EventsDatabaseAccess(myDb.getConn());
        PersonDatabaseAccess personDAO = new PersonDatabaseAccess(myDb.getConn());

        ArrayList<User> newUsers = loadRequest.getUsersArrayList();
        ArrayList<Person> newPersons = loadRequest.getPersonsArrayList();
        ArrayList<Event> newEvents = loadRequest.getEventsArrayList();

        try {
            for (User user : newUsers) {
                userDAO.addUser(user);
            }

            for (Event event : newEvents) {
                eventDAO.addEvent(event);
            }

            for (Person person : newPersons) {
                personDAO.addPerson(person);
            }

            myDb.closeConnection(true);

            int amountUsers = newUsers.size();
            int amountPersons = newPersons.size();
            int amountEvents = newEvents.size();

            String returnMsg = "Successfully added " + amountUsers + " users, " + amountPersons + " persons, and " + amountEvents + " events to the database.";

            return new LoadResponse(returnMsg);

        } catch (DataAccessException e) {
            myDb.closeConnection(true);

            return new LoadResponse(e.getMessage());
        }
    }
}
