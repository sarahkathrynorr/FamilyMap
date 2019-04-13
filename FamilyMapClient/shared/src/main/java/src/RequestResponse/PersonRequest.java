package src.RequestResponse;

/**
 * person request
 * contains data members Person object
 */
public class PersonRequest {
    public PersonRequest(String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
        this.descendant = descendant;
        this.father = father;
        this.mother = mother;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.spouse = spouse;
    }

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
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }
}
