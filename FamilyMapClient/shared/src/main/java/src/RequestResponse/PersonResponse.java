package src.RequestResponse;

import src.Model.Person;
/**
 * response to Event request
 * includes Strings for not found, and empty
 * and an Person to return
 */
public class PersonResponse {
    public PersonResponse(Person person) {
        this.person = person;
    }

    /**
     * String notFoundMsg is to display that the Person is not in the database
     */
    private static String message;
    /**
     * person is of type Person
     */
    private Person person;

    public Person getPerson() {
        return person;
    }

    public static String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
