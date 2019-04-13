package src.Services;
import src.DAO.AuthTokenDatabaseAccess;
import src.DAO.DataAccessException;
import src.DAO.Database;
import src.DAO.EventsDatabaseAccess;
import src.Model.Event;
import src.RequestResponse.EventResponse;
import src.RequestResponse.EventsResponse;

import java.util.ArrayList;

/**
 * services to get, remove, update events table
 */

public class EventsServices {
    /**
     * returns a single event based on Event Id
     *
     * @param eventId - the eventId of event to be returned, string
     * @return RequestResponse.EventResponse
     */
    public EventResponse getEvent(String eventId, String authToken) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        EventsDatabaseAccess eventsDAO = new EventsDatabaseAccess(myDb.getConn());

        try {
            AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());

            String currentUserName = authTokenDAO.getUserFromAuthToken(authToken);

            Event event = eventsDAO.getEvent(eventId, currentUserName);
            if (event != null) {
                myDb.closeConnection(true);

                return new EventResponse(event);
            }
            else {
                myDb.closeConnection(false);

                EventResponse eventResponse = new EventResponse(null);
                eventResponse.setMessage("Event " + eventId + " not found in database for " + currentUserName);
                return eventResponse;
            }

        } catch (DataAccessException e) {

            e.printStackTrace();

            EventResponse errorResponse = new EventResponse(null);
            errorResponse.setMessage(e.getMessage());
            myDb.closeConnection(false);
            return errorResponse;
        }
    }

    /**
     * returns all the events related to a user
     *
     * @return the array of Objects that contain events
     */
    public EventsResponse getAllEvents(String authToken) throws src.DAO.DataAccessException {
        Database myDb = new Database();
        myDb.openConnection();
        EventsDatabaseAccess eventsDAO = new EventsDatabaseAccess(myDb.getConn());

        try {
            AuthTokenDatabaseAccess authTokenDAO = new AuthTokenDatabaseAccess(myDb.getConn());
            String currentUserName = authTokenDAO.getUserFromAuthToken(authToken);

            ArrayList<Event> eventsArray = eventsDAO.getAllEvents(currentUserName);
            myDb.closeConnection(true);

            return new EventsResponse(eventsArray);

        } catch (DataAccessException e) {
            myDb.closeConnection(false);

            EventsResponse eventResponse = new EventsResponse(null);
            eventResponse.setMessage(e.getMessage());

            e.printStackTrace();

            return eventResponse;
        }
    }
}
