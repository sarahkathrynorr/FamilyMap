package src.RequestResponse;

import src.Model.Event;

import java.util.ArrayList;

public class EventsRequest {
    private ArrayList<Event> eventArrayList;

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }
}
