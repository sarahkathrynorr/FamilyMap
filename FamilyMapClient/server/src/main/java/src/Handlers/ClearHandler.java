package src.Handlers;

import src.RequestResponse.ClearResponse;
import src.RequestResponse.ErrorResponse;
import src.Services.ClearServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle (HttpExchange exchange) throws IOException {
        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {

            ClearServices clearServices = new ClearServices();
            System.out.println("Attempting to clear Database");

            try {
                ClearResponse clearResp = clearServices.clearEverything();
                String respData = encodeDecode.serialize(clearResp);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                System.out.println("Database cleared");
                encodeDecode.sendToOutput(exchange, respData);
                success = true;

            } catch (src.DAO.DataAccessException e) {
                //exception in the DAO
                String respData = encodeDecode.serialize(new ErrorResponse(e.getMessage()));
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                encodeDecode.sendToOutput(exchange, respData);
                exchange.getResponseBody().close();
                e.printStackTrace();
            }
            if (!success) {
                //method is not a POST or something else goes wrong
                String respData = encodeDecode.serialize(new ErrorResponse("Failed to clear database"));
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
            e.printStackTrace();
        }
    }
}
