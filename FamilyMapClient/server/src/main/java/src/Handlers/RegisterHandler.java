package src.Handlers;

import src.RequestResponse.ErrorResponse;
import src.RequestResponse.UserRequest;
import src.RequestResponse.UserResponse;
import src.Services.RegisterServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
    //POST

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = encodeDecode.readString(reqBody);

                System.out.println(reqData);

                UserRequest newUser = EncodeDecode.deserialize(reqData, UserRequest.class);

                RegisterServices register = new RegisterServices();
                try {
                    UserResponse userResponse = register.registerUser(newUser); 

                    String respData = encodeDecode.serialize(userResponse);
                    
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
                String respData = encodeDecode.serialize(new ErrorResponse("encountered error while registering user"));
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
