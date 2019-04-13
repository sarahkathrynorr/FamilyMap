package src.RequestResponse;
/**
 * cleared
 */
public class ClearResponse {

    /**
     * message to display that a deletion was successful, type String
     */
    private String message = "Clear succeeded";

    public String getResponse() {
        return message;
    }

    public void setResponse(String message) {
        this.message = message;
    }
}
