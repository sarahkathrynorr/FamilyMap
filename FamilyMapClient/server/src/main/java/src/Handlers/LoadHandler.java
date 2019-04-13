package src.Handlers;

import src.EncodeDecode;
import src.RequestResponse.*;
import src.Services.LoadServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = encodeDecode.readString(reqBody);

                System.out.println(reqData);

                LoadRequest loadRequest = EncodeDecode.deserialize(reqData, LoadRequest.class);

                LoadServices loadServices = new LoadServices();

                try {
                    LoadResponse loadResponse = loadServices.load(loadRequest);

                    String respData = encodeDecode.serialize(loadResponse);

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
                //method is not a POST or something else goes wrong

                String respData = encodeDecode.serialize(new ErrorResponse("Encountered Error while loading database"));
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
