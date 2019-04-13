package src.RequestResponse;
/**
 * authToken request object
 * data members are AuthToken and username
 */
public class AuthTokenRequest {
    /**
     * authToken is of type string
     */
    private String AuthToken;
    /**
     * username is of type string
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }
}
