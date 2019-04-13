package src.RequestResponse;

/**
 * user request
 * contains data members Person object
 */
public class UserRequest {
    public UserRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
}
