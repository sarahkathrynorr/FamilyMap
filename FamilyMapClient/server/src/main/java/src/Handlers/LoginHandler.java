package src.Handlers;

import src.RequestResponse.ErrorResponse;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;
import src.Services.LoginServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    //GET

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        EncodeDecode encodeDecode = new EncodeDecode();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = encodeDecode.readString(reqBody);

                System.out.println(reqData);

                RequestLogin newLogin = EncodeDecode.deserialize(reqData, RequestLogin.class);

                LoginServices loginServices = new LoginServices();

                try {
                    ResponseLogin responseLogin = loginServices.login(newLogin);

                    String respData = encodeDecode.serialize(responseLogin);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    encodeDecode.sendToOutput(exchange, respData);

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
                String respData = encodeDecode.serialize(new ErrorResponse("Error Logging in"));
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
