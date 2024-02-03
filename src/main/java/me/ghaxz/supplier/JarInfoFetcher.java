package me.ghaxz.supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.ghaxz.store.JarType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// Fetches jar types, versions, etc.
public class JarInfoFetcher {
    private static final Gson gson = new Gson();

    public static JsonObject fetchAllJarTypes() throws IOException {
        URL url;

        try {
            url = new URL("https://serverjars.com/api/fetchTypes/");
        } catch(MalformedURLException e) {
            return null;
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder responseBuilder = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        String response = responseBuilder.toString();

        return stringToJSON(response);
    }

    // todo fetch versions for specific JarType
    public ArrayList<String> fetchAllJarTypeVersions(JarType type) {
        return null;
    }

    private static JsonObject stringToJSON(String str) {
        return gson.fromJson(str, JsonObject.class);
    }
}
