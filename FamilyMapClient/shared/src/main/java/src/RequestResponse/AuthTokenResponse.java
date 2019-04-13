package src.RequestResponse;
/**
 * auth token response.
 * Data members include username, and auth Token
 * both are strings
 */
public class AuthTokenResponse {
    /**
     * username is of type string
     */
    private String username;
    /**
     * authToken is of type string
     */
    private String authToken;

    private String message;

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
