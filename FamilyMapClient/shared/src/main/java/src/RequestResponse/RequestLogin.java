package src.RequestResponse;
/**
 * request login object
 * includes a userName and a password string
 */
public class RequestLogin {
    public RequestLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    /**
     * userName - type String
     */
    private String userName;
    /**
     * password - type String
     */
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }
}

