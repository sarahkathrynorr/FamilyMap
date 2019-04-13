package src.RequestResponse;

import src.Model.User;
/**
 * response to User request
 * includes Strings for not found, and empty
 * and an User to return
 */
public class UserResponse {
    public UserResponse(String authToken, String userName, String personId) {
        this.authToken = authToken;
        this.userName = userName;
        this.personId = personId;
    }
    
    /**
     * notFoundMsg - String to display if the User is not found in the database
     */
    //private String notFoundMsg = "User not Found";
    
    private String authToken;
    private String userName;
    private String personId;

    private String message = null;

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPersonId() {
        return personId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
