package src.RequestResponse;

import src.Model.Event;

import java.util.ArrayList;

public class EventsResponse {
    ArrayList<Event> data;
    String message;

    public EventsResponse(ArrayList<Event> eventsArray) {
        this.data = eventsArray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Event> getEventsArray() {
        return data;
    }

    public void setEventsArray(ArrayList<Event> eventsArray) {
        this.data = eventsArray;
    }

}
