package src.Handlers;

import src.EncodeDecode;
import src.RequestResponse.ErrorResponse;
import src.RequestResponse.FillResponse;
import src.Services.FillServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    //POST

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        FillServices fillServices = new FillServices();
        FillResponse fillResponse = null;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String requestUserName = exchange.getRequestURI().getPath();
                requestUserName = requestUserName.substring(6);
                int generations = 4;

                if (requestUserName.contains("/")) {
                    int slashIndex = requestUserName.indexOf("/");
                    generations = Integer.parseInt(requestUserName.substring(slashIndex+1));
                    requestUserName = requestUserName.substring(0,slashIndex);
                }

                System.out.println("filling database for " + requestUserName);

                try {
                    fillResponse = fillServices.fill(requestUserName, generations);

                    String respData = encodeDecode.serialize(fillResponse);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    encodeDecode.sendToOutput(exchange, respData);

                    exchange.getResponseBody().close();
                    success = true;

                } catch (src.DAO.DataAccessException e) {
                    //exception in the DAO

                    String respData = encodeDecode.serialize(new ErrorResponse(e.getMessage()));
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                    encodeDecode.sendToOutput(exchange, respData);
                    exchange.getResponseBody().close();
                }
            }
            if (!success) {

                String respData = encodeDecode.serialize(new ErrorResponse("Encountered error while filling database"));
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
