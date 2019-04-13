package src.RequestResponse;
/**
 * the result object body for the login
 * contains a User user data member
 */
public class ResponseLogin {
    public ResponseLogin(String authToken, String userName, String personId) {
        this.authToken = authToken;
        this.userName = userName;
        this.personId = personId;
    }
    /**
     * user - User object
     */
    private String authToken;
    private String userName;
    private String personId;

    private String message;

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() { return userName;  }

    public String getPersonId() { return personId; }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
