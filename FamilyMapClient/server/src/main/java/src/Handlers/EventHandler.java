package src.Handlers;

import src.RequestResponse.ErrorResponse;
import src.RequestResponse.EventResponse;
import src.RequestResponse.EventsResponse;
import src.Services.EventsServices;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    //GET

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        EventsServices eventsServices = new EventsServices();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                try {
                    System.out.println("fetching events");

                    Headers reqHeaders = exchange.getRequestHeaders();
                    if (reqHeaders.containsKey("Authorization")) {

                        // Extract the auth token from the "Authorization" header
                        String authToken = reqHeaders.getFirst("Authorization");
                        EventsResponse eventResponse = eventsServices.getAllEvents(authToken);
                        String respData = encodeDecode.serialize(eventResponse);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        encodeDecode.sendToOutput(exchange, respData);
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
                String respData = encodeDecode.serialize(new ErrorResponse("Failed to fetch events from database"));
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                encodeDecode.sendToOutput(exchange, respData);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {
            //error in reading in stuff and sending it out
            String respData = encodeDecode.serialize(new ErrorResponse(e.getMessage()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            encodeDecode.sendToOutput(exchange, respData);
            exchange.getResponseBody().close();
        }
    }
}
