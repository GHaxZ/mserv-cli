package me.ghaxz.supplier;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.ghaxz.store.JarType;
import me.ghaxz.store.JarVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
Fetches all sorts of information about jars from the API (https://serverjars.com/)
 */
public class JarInfoFetcher {
    private static final Gson gson = new Gson();

    public static JsonObject fetchAllJarTypes() throws IOException {
        return fetchFromLink("https://serverjars.com/api/fetchTypes/");
    }

    public static ArrayList<JarVersion> fetchAllJarTypeVersions(JarType type) throws IOException {
        ArrayList<JarVersion> jarVersions = new ArrayList<>();

        JsonArray jsonResponse = fetchFromLink("https://serverjars.com/api/fetchAll/" + type.getApiURL()).getAsJsonArray("response");

        for (JsonElement element : jsonResponse) {
            JsonObject object = element.getAsJsonObject();

            jarVersions.add(new JarVersion(
                    object.get("version").getAsString(),
                    object.getAsJsonObject("size").get("bytes").getAsLong(),
                    object.get("file").getAsString()
                    ));

        }

        return jarVersions;
    }

    private static JsonObject fetchFromLink(String apiURL) throws IOException {
        URL url = new URL(apiURL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder responseBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line);
        }

        return stringToJSON(responseBuilder.toString());
    }

    private static JsonObject stringToJSON(String str) {
        return gson.fromJson(str, JsonObject.class);
    }
}
