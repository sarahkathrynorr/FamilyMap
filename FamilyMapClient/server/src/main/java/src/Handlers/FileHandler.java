package src.Handlers;
//import MainServer; How to import from parent dir

import java.net.HttpURLConnection;
import java.nio.file.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class FileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestURL = exchange.getRequestURI().getPath();
        //Icon only shows up on lab computers. So don't worry about that.

        if (requestURL.equals("/")) {
            requestURL = "/index.html";
        }

        // FROM DR. R's CODE:
        String filePathStr = "/Users/orrsa/IdeaProjects/FamilySearch/Web" + requestURL;
        Path filePath = FileSystems.getDefault().getPath(filePathStr);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        Files.copy(filePath, exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
}
