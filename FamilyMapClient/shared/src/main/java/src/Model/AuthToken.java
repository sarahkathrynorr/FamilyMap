package src.Model;

/**
 * model for Auth Token
 */
public class AuthToken {
    public AuthToken(String username, String personId, String authToken) {
        this.username = username;
        this.personId = personId;
        this.authToken = authToken;
    }

    /**
     * username is a string
     */
    private String username;
    /**
     * personId is a string
     */
    private String personId;
    /**
     * authToken is a string
     */
    private String authToken;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
