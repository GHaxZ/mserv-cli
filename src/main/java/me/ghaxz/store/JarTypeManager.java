package me.ghaxz.store;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.ghaxz.interfaces.ArgParser;
import me.ghaxz.supplier.JarInfoFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/*
Manages and stores all JarTypes for easy access and information gathering
 */
public class JarTypeManager {
    private static JarTypeManager instance = null;
    private final ArrayList<JarType> jarTypes;

    private JarTypeManager() throws IOException {
        jarTypes = new ArrayList<>();
        getAllTypes();
    }

    private void getAllTypes() throws IOException {
        JsonObject jsonResponse = JarInfoFetcher.fetchAllJarTypes().getAsJsonObject("response");

        for(Map.Entry<String, JsonElement> category : jsonResponse.entrySet()) {
            for(JsonElement name : category.getValue().getAsJsonArray()) {
                jarTypes.add(new JarType(category.getKey(), name.getAsString()));
            }
        }
    }

    public static JarTypeManager getInstance() {
        if(instance == null) {
            try {
                instance = new JarTypeManager();
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("Failed fetching available jar types from api: " + e);
            }

        }

        return instance;
    }

    public ArrayList<JarType> getJarTypes() {
        return jarTypes;
    }

    public JarType getJarTypeByName(String name) {
        for(JarType jar : jarTypes) {
            if(jar.getName().equals(name)) {
                return jar;
            }
        }

        return null;
    }
}
