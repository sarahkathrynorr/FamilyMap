package com.example.familymapclient;

import java.io.Serializable;
import java.util.ArrayList;

import src.Model.Person;

public class AllPersons implements Serializable, Cloneable {
    private ArrayList<Person> allPersons;

    public ArrayList<Person> getAllPersonsArray() {
        return allPersons;
    }

    public void setAllPersons(ArrayList<Person> allPersons) {
        this.allPersons = allPersons;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
