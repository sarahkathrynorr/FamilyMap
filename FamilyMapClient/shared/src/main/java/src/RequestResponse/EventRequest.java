package src.RequestResponse;
/**
 * data members are the eventID and the Event event object
 */
public class EventRequest {
    public EventRequest(String eventID) {
        this.eventID = eventID;
    }
    /**
     * eventID is of type string
     */
    private String eventID;
    /**
     * event is of type Event
     */

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}
