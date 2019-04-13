package src.Model;
/**
 * model class for user object
 * contains getters and setters
 */
public class User {
    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * userName is of type string
     */
    private String userName;
    /**
     * password is of type string
     */
    private String password;
    /**
     * email is of type string
     */
    private String email;
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
     * must be either "m" or "f"
     */
    private String gender;
    /**
     * personID is of type string
     */
    private String personID;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonId() {
        return personID;
    }

    public String getUsername() {
        return userName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPersonId().equals(getPersonId()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getPassword().equals(getPassword());
        }
        return false;
    }
    
}
