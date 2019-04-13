package src.RequestResponse;


import src.Model.Event;
import src.Model.Person;
import src.Model.User;

import java.util.ArrayList;

public class LoadRequest {
    private ArrayList<User> users;
    private ArrayList<Person> persons;
    private ArrayList<Event> events;

    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.events = events;
        this.persons = persons;
        this.users = users;
    }

    public ArrayList<Event> getEventsArrayList() {
        return events;
    }

    public ArrayList<Person> getPersonsArrayList() {
        return persons;
    }

    public ArrayList<User> getUsersArrayList() {
        return users;
    }

    public void setEventsArrayList(ArrayList<Event> events) {
        this.events = events;
    }

    public void setPersonsArrayList(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public void setUsersArrayList(ArrayList<User> users) {
        this.users = users;
    }
}
