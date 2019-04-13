package src.Model;

import java.io.Serializable;

/**
 * model class for person
 * includes getters and setters
 */
public class Person implements Serializable {
    /**
     * constructor
     *
     * @param personID - String
     * @param descendant - String
     * @param firstName - String
     * @param lastName - String
     * @param gender - String
     * @param father - String
     * @param mother - String
     * @param spouse - String
     */
    public Person(String personID, String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.descendant = descendant;
        this.personID = personID;
        this.father = father;
        this.mother = mother;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.spouse = spouse;
    }

    /**
     * personID is of type string
     */
    private String personID;
    /**
     * descendant is of type string
     */
    private String descendant;
    /**
     * firstName is of type string
     */
    private String firstName;
    /**
     * lastName is of type string
     */
    private String lastName;
    /**
     * gender is of type string
     * can only be "m" or "f"
     */
    private String gender;
    /**
     * father is of type string
     * may be a null value
     */
    private String father;
    /**
     * mother is of type string
     * may be a null value
     */
    private String mother;
    /**
     * spouse is of type string
     * may be a null value
     */
    private String spouse;


    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getFather() {
        if (father == null) {
            return null;
        }
        return father;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMother() {
        if (mother == null) {
            return null;
        }
        return mother;
    }

    public String getPersonId() {
        return personID;
    }

    public String getSpouse() {
        if (spouse == null) {
            return null;
        }
        return spouse;
    }

    /**
     * equals method
     * @param o - object to compare
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonId().equals(getPersonId()) &&
                    oPerson.getDescendant().equals(getDescendant()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    (oPerson.getFather() == null || oPerson.getFather().equals(getFather())) &&
                    (oPerson.getMother() == null || oPerson.getMother().equals(getMother())) &&
                    (oPerson.getSpouse() == null || oPerson.getSpouse().equals(getSpouse()));
        }
        return false;
    }
}
