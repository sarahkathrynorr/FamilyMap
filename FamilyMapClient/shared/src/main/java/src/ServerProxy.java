package src;

import java.io.*;
import java.net.*;

import src.RequestResponse.EventResponse;
import src.RequestResponse.EventsResponse;
import src.RequestResponse.PersonResponse;
import src.RequestResponse.PersonsRequest;
import src.RequestResponse.PersonsResponse;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;
import src.RequestResponse.UserRequest;
import src.RequestResponse.UserResponse;

public class ServerProxy {
    private String serverHost;
    private String serverPort;
    private EncodeDecode encodeDecode = new EncodeDecode();

    public String getServerHost() {
        return serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {}//nothing to see here


    public ResponseLogin login(RequestLogin requestLogin) {

        EncodeDecode encodeDecode = new EncodeDecode();
        // This method shows how to send a GET request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqData = encodeDecode.serialize(requestLogin);

            OutputStream reqBody = http.getOutputStream();
            encodeDecode.writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("login successful");

                InputStream respBody = http.getInputStream();

                String respData = encodeDecode.readString(respBody);

                System.out.println(respData);

                respBody.close();

                return encodeDecode.deserialize(respData, ResponseLogin.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream respBody = http.getInputStream();

                String respData = encodeDecode.readString(respBody);

                System.out.println(respData);
                respBody.close();

                return encodeDecode.deserialize(respData, ResponseLogin.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            ResponseLogin errorResponse = new ResponseLogin(null, null, null);
            errorResponse.setMessage(e.getMessage());
            return errorResponse;
        }
    }

    public UserResponse register(UserRequest userRequest) {

        EncodeDecode encodeDecode = new EncodeDecode();
        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            String reqData = encodeDecode.serialize(userRequest);

            OutputStream reqBody = http.getOutputStream();
            encodeDecode.writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("Successfully registered new user");
                respBody.close();

                return encodeDecode.deserialize(respData, UserResponse.class);
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return encodeDecode.deserialize(respData, UserResponse.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PersonResponse getPerson(String personId, String authToken) {
        EncodeDecode encodeDecode = new EncodeDecode();
        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + personId);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is not a request body

            http.addRequestProperty("Authorization", authToken);

            http.connect();
            System.out.println(http.getResponseCode());

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("Successfully got person for user");
                respBody.close();

                return encodeDecode.deserialize(respData, PersonResponse.class);
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return encodeDecode.deserialize(respData, PersonResponse.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public EventsResponse getEvents(String authToken) {
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            URL url = new URL("http://" + serverHost +  ":" + serverPort + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is not a request body

            http.addRequestProperty("Authorization", authToken);

            http.connect();
            System.out.println(http.getResponseCode());
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("Successfully got events for user");
                respBody.close();

                return encodeDecode.deserialize(respData, EventsResponse.class);
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return encodeDecode.deserialize(respData, EventsResponse.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public PersonsResponse getPersons(String authToken) {
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            URL url = new URL("http://" + serverHost +  ":" + serverPort + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is not a request body

            http.addRequestProperty("Authorization", authToken);

            http.connect();
            System.out.println(http.getResponseCode());
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("Successfully got events for user");
                respBody.close();

                return encodeDecode.deserialize(respData, PersonsResponse.class);
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return encodeDecode.deserialize(respData, PersonsResponse.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public EventResponse getSingleEvent(String eventId, String authToken) {
        EncodeDecode encodeDecode = new EncodeDecode();
        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event/" + eventId);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is not a request body

            http.addRequestProperty("Authorization", authToken);

            http.connect();
            System.out.println(http.getResponseCode());

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("Successfully got event");
                respBody.close();

                return encodeDecode.deserialize(respData, EventResponse.class);
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return encodeDecode.deserialize(respData, EventResponse.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean clear() {
        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);	// There is not a request body

            http.connect();
            System.out.println(http.getResponseCode());

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("cleared database");
                respBody.close();

                return true;
            }
            else {
                InputStream respBody = http.getInputStream();
                String respData = encodeDecode.readString(respBody);
                System.out.println(respData);

                System.out.println("ERROR: " + http.getResponseMessage());
                respBody.close();

                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
