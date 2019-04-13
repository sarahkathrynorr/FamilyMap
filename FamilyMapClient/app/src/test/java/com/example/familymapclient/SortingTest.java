package com.example.familymapclient;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import src.Model.Event;

import static org.junit.Assert.*;

public class SortingTest {
        private AllEvents allEvents = new AllEvents();

        private Event event1;
        private Event event2;
        private Event event3;
        private Event event4;
        private Event event5;


        @Before
        public void doBefore() {
            event1 = new Event("event1", "test", "test", 0.0, 0.0, "test", "test", "marriage", 4);
            event2 = new Event("event2", "test", "test", 0.0, 0.0, "test", "test", "marriage", 2);
            event3 = new Event("event3", "test", "test", 0.0, 0.0, "test", "test", "birth", 1);
            event4 = new Event("event4", "test", "test", 0.0, 0.0, "test", "test", "cried a bit", 3);

            event5 = new Event("event5", "test", "test", 0.0, 0.0, "test", "test", "cried a bit", 3);

            ArrayList<Event> allEventsArray = new ArrayList<>();
            allEventsArray.add(event1);
            allEventsArray.add(event2);
            allEventsArray.add(event3);
            allEventsArray.add(event4);
            allEvents.setAllEvents(allEventsArray);
    }

    @Test
    public void orderEventsCorrectly() {
            PersonActivity personActivity = new PersonActivity();

            ArrayList<Event> expected = new ArrayList<>();
            expected.add(event3);
            expected.add(event2);
            expected.add(event4);
            expected.add(event1);

            ArrayList<Event> result = personActivity.sortOrderOfEvents(allEvents.getAllEvents());

            assertEquals(expected, result);
    }

    @Test
    public void orderEventsCorrectlyWithDuplicateYear() {
        allEvents.getAllEvents().add(event5);

        PersonActivity personActivity = new PersonActivity();

        ArrayList<Event> expected = new ArrayList<>();
        expected.add(event3);
        expected.add(event2);
        expected.add(event4);
        expected.add(event5);
        expected.add(event1);

        ArrayList<Event> result = personActivity.sortOrderOfEvents(allEvents.getAllEvents());

        assertEquals(expected, result);
    }

    @Test
    public void testWithNoAvailableEvent() {
        PersonActivity personActivity = new PersonActivity();

        ArrayList<Event> result = personActivity.sortOrderOfEvents(new ArrayList<Event>());

        assertTrue(result.isEmpty());
    }
}
