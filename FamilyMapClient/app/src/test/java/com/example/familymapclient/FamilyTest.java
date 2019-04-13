package com.example.familymapclient;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import src.Model.Person;

import static org.junit.Assert.*;

public class FamilyTest {
    AllPersons allPersons;

    @Before
    public void doBefore() {
        Person grandma = new Person("grandma", "test", "test", "test", "f", null, null, "grandpa");
        Person grandpa = new Person("grandpa", "test", "test", "test", "m", null, null, "grandma");
        Person mom = new Person("mom", "mom", "test", "test", "f", "grandpa", "grandma", "dad");
        Person dad = new Person("dad", "mom", "test", "test", "m", null, null, "mom");
        Person daughter = new Person("daughter", "mom", "test", "test", "f", "dad", "mom", null);
        Person son = new Person("son", "mom", "test", "test", "m", "dad", "mom", "bertha");

        ArrayList<Person> allPersonsArray = new ArrayList<>();
        allPersonsArray.add(grandma);
        allPersonsArray.add(grandpa);
        allPersonsArray.add(mom);
        allPersonsArray.add(dad);
        allPersonsArray.add(son);
        allPersonsArray.add(daughter);

        allPersons = new AllPersons();
        allPersons.setAllPersons(allPersonsArray);
    }

    @Test
    public void testFindChildrenGood() {
        PersonActivity personActivity = new PersonActivity();
        ArrayList<String> children = personActivity.findChildren("mom", allPersons);

        assertTrue(children.contains("son"));
        assertTrue(children.contains("daughter"));
    }
    @Test
    public void testFindChildrenBad() {
        //testing for a person that has no children
        PersonActivity personActivity = new PersonActivity();
        ArrayList<String> children = personActivity.findChildren("son", allPersons);

        assertTrue(children.isEmpty());
    }

    @Test
    public void testFindParentsGood() {
        MapFragment mapFragment = new MapFragment();
        ArrayList<String> parents = mapFragment.findParents("mom", allPersons);

        assertTrue(parents.contains("grandma"));
        assertTrue(parents.contains("grandpa"));
    }
    @Test
    public void testFindParentsBad() {
        //looking for a person with no parents
        MapFragment mapFragment = new MapFragment();
        ArrayList<String> parents = mapFragment.findParents("grandpa", allPersons);

        assertTrue(parents.isEmpty());
    }

    @Test
    public void testFindSpouseGood() {
        MapFragment mapFragment = new MapFragment();
        String spouse = mapFragment.findSpouse("grandma", allPersons);

        assertEquals("grandpa", spouse);
    }
    @Test
    public void testFindSpouseBad() {
        //testing someone who doesn't have a spouse
        MapFragment mapFragment = new MapFragment();
        String spouse = mapFragment.findSpouse("daughter", allPersons);

        assertNull(spouse);

    }
}
