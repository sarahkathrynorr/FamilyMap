package com.example.familymapclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import src.Model.Event;
import src.Model.Person;

import static org.junit.Assert.*;

public class FilterTest {

    private AllEvents allEvents = new AllEvents();
    private AllPersons allPersons = new AllPersons();
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;

    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;

    ArrayList<String> eventTypes = new ArrayList<>();


    @Before
    public void doBefore() {
        person1 = new Person("test", "test", "test", "test", "f", "father", "mother", "spouse");
        person2 = new Person("spouse", "test", "test", "test", "m", null, null, "test");
        person3 = new Person("father", "test", "test", "test", "m", null, null, "mother");
        person4 = new Person("mother", "test", "test", "test", "f", null, null, "father");

        ArrayList<Person> allPersonsArray = new ArrayList<>();
        allPersonsArray.add(person1);
        allPersonsArray.add(person2);
        allPersonsArray.add(person3);
        allPersonsArray.add(person4);
        allPersons.setAllPersons(allPersonsArray);

        event1 = new Event("event1", "test", "test", 0.0, 0.0, "test", "test", "marriage", 000);
        event2 = new Event("event2", "test", "spouse", 0.0, 0.0, "test", "test", "marriage", 000);
        event3 = new Event("event3", "test", "mother", 0.0, 0.0, "test", "test", "birth", 000);
        event4 = new Event("event4", "test", "father", 0.0, 0.0, "test", "test", "cried a bit", 000);

        ArrayList<Event> allEventsArray = new ArrayList<>();
        allEventsArray.add(event1);
        allEventsArray.add(event2);
        allEventsArray.add(event3);
        allEventsArray.add(event4);
        allEvents.setAllEvents(allEventsArray);

        Model.instance().setUser(person1);
    }

    @Test
    public void testFilterByEventName() {
        eventTypes.add(event4.getEventType());

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertEquals(event4, results.get(0));
    }

    @Test
    public void testByGender() {
        eventTypes.add("Female");

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.contains(event1));
        assertTrue(results.contains(event3));
    }

    @Test
    public void testByMothersLine() {
        eventTypes.add("Mother");

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.contains(event1));
        assertTrue(results.contains(event3));
    }

    @Test
    public void testByFathersLine() {
        eventTypes.add("Father");

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.contains(event1));
        assertTrue(results.contains(event4));
    }

    @Test
    public void testMultipleEventTypes() {
        eventTypes.add("Mother");
        eventTypes.add(event4.getEventType());

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.contains(event4));
        assertTrue(results.contains(event1));
        assertTrue(results.contains(event3));
        assertTrue(!results.contains(event2));
    }



    @Test
    public void testEventTypeThatIsNotThere() {
        eventTypes.add("nope");

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.isEmpty());
    }

    @Test
    public void testNoPersonsToGet() {
        eventTypes.add("Female");

        MapFragment mapFragment = new MapFragment();
        AllPersons testAllPersons = new AllPersons();
        testAllPersons.setAllPersons(new ArrayList<Person>());
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, testAllPersons);

        assertTrue(results.isEmpty());
    }

    @Test
    public void testNoEventsToGet() {
        eventTypes.add("Female");

        MapFragment mapFragment = new MapFragment();
        AllEvents testAllEvents = new AllEvents();
        testAllEvents.setAllEvents(new ArrayList<Event>());
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, testAllEvents, allPersons);

        assertTrue(results.isEmpty());
    }

    @Test
    public void filterBySideWithNoParent() {
        eventTypes.add("Mother");
        Model.instance().setUser(person4);

        MapFragment mapFragment = new MapFragment();
        ArrayList<Event> results = mapFragment.filterEvents(eventTypes, allEvents, allPersons);

        assertTrue(results.contains(event3));
        Model.instance().setUser(person1);
    }


        @After
    public void doAfter() {
        allEvents = new AllEvents();
        allPersons = new AllPersons();
        eventTypes.clear();
    }
}
