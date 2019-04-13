package src.Handlers;

import src.EncodeDecode;
import src.RequestResponse.ErrorResponse;
import src.RequestResponse.PersonResponse;
import src.Services.PersonServices;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SinglePersonHandler implements HttpHandler {
    //GET

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                String requestPersonID = exchange.getRequestURI().getPath();
                requestPersonID = requestPersonID.substring(8);

                PersonServices personServices = new PersonServices();

                try {

                    System.out.println("fetching data for personID: " + requestPersonID);

                    Headers reqHeaders = exchange.getRequestHeaders();

                    if (reqHeaders.containsKey("Authorization")) {

                        // Extract the auth token from the "Authorization" header
                        String authToken = reqHeaders.getFirst("Authorization");

                        PersonResponse personResponse = personServices.getPerson(requestPersonID, authToken);

                        String respData = encodeDecode.serialize(personResponse);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        encodeDecode.sendToOutput(exchange, respData);

                        exchange.getResponseBody().close();
                        success = true;
                    }
                    else {
                        success = false;
                    }
                } catch (src.DAO.DataAccessException e) {
                    //exception in the DAO
                    PersonResponse errorResponse = new PersonResponse(null);
                    errorResponse.setMessage(e.getMessage());

                    String respData = encodeDecode.serialize(errorResponse);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    encodeDecode.sendToOutput(exchange, respData);
                    exchange.getResponseBody().close();
                }
            }
            if (!success) {
                //method is not a GET or something else goes wrong
                PersonResponse errorResponse = new PersonResponse(null);
                errorResponse.setMessage("Error encountered while fetching person");

                String respData = encodeDecode.serialize(errorResponse);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                encodeDecode.sendToOutput(exchange, respData);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            //error in reading in stuff and sending it out
            PersonResponse errorResponse = new PersonResponse(null);
            errorResponse.setMessage(e.getMessage());

            String respData = encodeDecode.serialize(errorResponse);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            encodeDecode.sendToOutput(exchange, respData);
            exchange.getResponseBody().close();
        }
    }
}
