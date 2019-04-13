package src.Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.net.httpserver.HttpExchange;
import java.io.*;


public class EncodeDecode<T> {

    public static <T> T deserialize(String value, Class<T> returnType) {
        Gson gson = new Gson();

        T returnMe = gson.fromJson(value, returnType);

        return returnMe;
    }

    public String serialize(T input) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonStr = gson.toJson(input);

        return jsonStr;
    }

    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public void sendToOutput(HttpExchange exchange, String respData) throws IOException {
        OutputStream respBody = exchange.getResponseBody();

        writeString(respData, respBody);
        respBody.close();
        exchange.getResponseBody().close();
    }
}
