package src.DAO;

import src.Model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Events Database Access Object -
 * Here we will be able to update table rows,
 * delete rows,
 * or create new rows for various events that occur in the dead/alive person's life
 * mazel tov!
 */

public class EventsDatabaseAccess {
    private Connection conn;

    public EventsDatabaseAccess(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Add a new event (row) to the events table
     * updateEvent is then called with the rest of the object body
     *
     * - congratulations! It's a bouncing baby girl. You should probably not put your newborn on a trampoline tho
     *
     * @param eventDetails - object body that contains:
     *  eventId - numeric unique id associated with the event
     *               * @param latitude - latitude of the event
     *  latitude - the latitude of the event
     *  longitude - the longitude of the event
     *  country - the country where the event took place
     *  city - the city where the event took place
     *  eventType - event type
     *  year - type int, the year this event happened
     *  username - the descendant the event is connected to
     *  personId - the person that this event is about
     *                 (if the person does not exist the event will not be added)
     *
     *
     *@throws DataAccessException
     *
     * @return boolean success value
     */
    public boolean addEvent(Event eventDetails) throws DataAccessException{
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, eventDetails.getEventID());
            stmt.setString(2, eventDetails.getDescendant());
            stmt.setString(3, eventDetails.getPersonId());
            stmt.setDouble(4, eventDetails.getLatitude());
            stmt.setDouble(5, eventDetails.getLongitude());
            stmt.setString(6, eventDetails.getCountry());
            stmt.setString(7, eventDetails.getCity());
            stmt.setString(8, eventDetails.getEventType());
            stmt.setInt(9, eventDetails.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
        return commit;
    }

    /**
     * Retrieves all the events associated with a specific username
     *
     * @param descendant - an String that contains:
     *  personId - can be null as long as username is not null
     *
     * @return events - Arraylist of objects that contain all the data on events relating to user
     *
     * @throws DataAccessException
     */
    public ArrayList<Event> getAllEvents(String descendant) throws DataAccessException {
        Event event;
        ArrayList<Event> events = new ArrayList<Event>();
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descendant);
            rs = stmt.executeQuery();
            if (rs.next()) {
                do{
                    event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                            rs.getString("PersonID"), rs.getDouble/*used to be getFloat*/("Latitude"),
                            rs.getDouble/*used to be getFloat*/("Longitude"),
                            rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                            rs.getInt("Year"));
                    events.add(event);
                } while (rs.next());
                return events;
            } else if (!rs.next()) {
                throw new DataAccessException("No events found in database to return for " + descendant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    /**
     * retrieves a specific event based on an entered eventId
     *
     * @param eventId - numeric value associated with a specific event
     *
     * @return event - the single event associated with eventId
     *
     * @throws DataAccessException
     */
    public Event getEvent(String eventId, String userName) throws DataAccessException {
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?" +
                "And Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            stmt.setString(2, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                        rs.getString("PersonID"), rs.getDouble/*used to be getFloat*/("Latitude"),
                        rs.getDouble/*used to be getFloat*/("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            } else {
                throw new DataAccessException("Event " + eventId + " not found in database for " + userName);
                }
            } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * clears all the events from the table
     *
     * @throws DataAccessException
     *
     * @return boolean success value
     */
    public boolean removeAllEvents(String descendant) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE Descendant = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, descendant);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }
}

