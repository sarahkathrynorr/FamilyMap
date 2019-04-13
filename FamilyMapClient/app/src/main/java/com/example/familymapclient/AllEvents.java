package com.example.familymapclient;

import java.io.Serializable;
import java.util.ArrayList;

import src.Model.Event;

public class AllEvents implements Serializable, Cloneable{
    private ArrayList<Event> allEvents;

    public ArrayList<Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(ArrayList<Event> allEvents) {
        this.allEvents = allEvents;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
