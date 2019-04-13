package src.Handlers;

import src.EncodeDecode;
import src.RequestResponse.ErrorResponse;
import src.RequestResponse.PersonsResponse;
import src.Services.PersonServices;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonsHandler implements HttpHandler {
    //GET

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                PersonServices personServices = new PersonServices();

                try {

                    System.out.println("fetching persons");

                    Headers reqHeaders = exchange.getRequestHeaders();
                    if (reqHeaders.containsKey("Authorization")) {

                        // Extract the auth token from the "Authorization" header
                        String authToken = reqHeaders.getFirst("Authorization");

                        PersonsResponse personsResponse = personServices.getAllPersons(authToken);

                        String respData = encodeDecode.serialize(personsResponse);

                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        encodeDecode.sendToOutput(exchange, respData);

                        exchange.getResponseBody().close();
                        success = true;
                    } else {
                        success = false;
                    }
                } catch (src.DAO.DataAccessException e) {
                    //exception in the DAO

                    String respData = encodeDecode.serialize(new ErrorResponse(e.getMessage()));
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    encodeDecode.sendToOutput(exchange, respData);
                    exchange.getResponseBody().close();
                }
            }
            if (!success) {
                //method is not a POST or something else goes wrong
                String respData = encodeDecode.serialize(new ErrorResponse("Error fetching persons from database"));
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                encodeDecode.sendToOutput(exchange, respData);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            //error in reading in stuff and sending it out
            String respData = encodeDecode.serialize(new ErrorResponse(e.getMessage()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            encodeDecode.sendToOutput(exchange, respData);
            exchange.getResponseBody().close();
        }
    }
}

