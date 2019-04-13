package src.RequestResponse;

import src.Model.Person;

import java.util.ArrayList;

public class PersonsResponse {
    ArrayList<Person> data;
    String message;

    public PersonsResponse(ArrayList<Person> personsArray) {
        this.data = personsArray;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Person> getPersonsArray() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setPersonsArray(ArrayList<Person> personsArray) {
        this.data = personsArray;
    }
}
