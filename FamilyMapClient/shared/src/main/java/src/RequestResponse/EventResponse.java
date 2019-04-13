package src.RequestResponse;

import src.Model.Event;
/**
 * response to Event request
 * includes Strings for not found, and empty
 * and an Event to return
 */
public class EventResponse {
    public EventResponse(Event event) {
        this.event = event;
    }

    /**
     * String message to display if the event is not found in the Database
     */
    private String message = null;
    /**
     * event is of type Event
     */
    private Event event;

    public Event getEvent() {
        return event;
    }

   public String getMessage() {
        return message;
   }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof EventResponse) {
            EventResponse oEventResponse = (EventResponse) obj;
            return oEventResponse.getEvent().equals(getEvent());
        }
        return false;
    }
}
