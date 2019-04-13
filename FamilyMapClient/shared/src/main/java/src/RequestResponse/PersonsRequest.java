package src.RequestResponse;

import src.Model.Person;

import java.util.ArrayList;

public class PersonsRequest {
    private ArrayList<Person> personArrayList;

    public ArrayList<Person> getPersonArrayList() {
        return personArrayList;
    }

    public void setPersonArrayList(ArrayList<Person> personArrayList) {
        this.personArrayList = personArrayList;
    }
}
