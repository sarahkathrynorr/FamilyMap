package com.example.familymapclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import src.Model.Event;
import src.Model.Person;

import static org.junit.Assert.*;

public class SearchTest {

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
        person1 = new Person("test", "test", "test3", "test4", "f", "father", "mother", "spouse");
        person2 = new Person("spouse", "test", "nope", "test4", "m", null, null, "test");
        person3 = new Person("father", "test", "test3", "test4", "m", null, null, "mother");
        person4 = new Person("mother", "test", "test3", "nope", "f", null, null, "father");

        ArrayList<Person> allPersonsArray = new ArrayList<>();
        allPersonsArray.add(person1);
        allPersonsArray.add(person2);
        allPersonsArray.add(person3);
        allPersonsArray.add(person4);
        allPersons.setAllPersons(allPersonsArray);

        event1 = new Event("event1", "test", "test", 0.0, 0.0, "test1", "test2", "test5", 000);
        event2 = new Event("event2", "test", "spouse", 0.0, 0.0, "nope", "test2", "test5", 000);
        event3 = new Event("event3", "test", "mother", 0.0, 0.0, "test1", "nope", "test5", 000);
        event4 = new Event("event4", "test", "father", 0.0, 0.0, "test1", "test2", "cried just a bit", 000);

        ArrayList<Event> allEventsArray = new ArrayList<>();
        allEventsArray.add(event1);
        allEventsArray.add(event2);
        allEventsArray.add(event3);
        allEventsArray.add(event4);
        allEvents.setAllEvents(allEventsArray);
    }

    @Test
    public void testJustOneLetter() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("c", allEvents, allPersons);

        assertEquals(1, results.toArray().length);
    }

    @Test
    public void testCitySearch() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("test2", allEvents, allPersons);

        assertEquals(3, results.toArray().length);
    }

    @Test
    public void testCountrySearch() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("test1", allEvents, allPersons);

        assertEquals(3, results.toArray().length);
    }

    @Test
    public void testFirstNameSearch() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("test3", allEvents, allPersons);

        assertEquals(3, results.toArray().length);
    }

    @Test
    public void testLastNameSearch() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("test4", allEvents, allPersons);

        assertEquals(3, results.toArray().length);
    }

    @Test
    public void searchSomethingThatIsNotThere() {
        SearchActivity searchActivity = new SearchActivity();
        ArrayList<Object> results = searchActivity.updateSearchFilter("yerMom", allEvents, allPersons);

        assertEquals(0, results.toArray().length);
    }

    @Test
    public void searchWithNoPersons() {
        SearchActivity searchActivity = new SearchActivity();
        AllPersons testAllPersons = new AllPersons();
        testAllPersons.setAllPersons(new ArrayList<Person>());

        ArrayList<Object> results = searchActivity.updateSearchFilter("test3", allEvents, testAllPersons);

        assertEquals(0, results.toArray().length);
    }

    @Test
    public void searchWithNoEvents() {
        SearchActivity searchActivity = new SearchActivity();
        AllEvents testAllEvents = new AllEvents();
        testAllEvents.setAllEvents(new ArrayList<Event>());

        ArrayList<Object> results = searchActivity.updateSearchFilter("test1", testAllEvents, allPersons);

        assertEquals(0, results.toArray().length);
    }
}

