package me.ghaxz.model.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.ghaxz.cli.ArgParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

/*
Manages and stores all server configurations and gets serialized,
to later access configured server instances
 */
public class ServerInstanceManager {
    private static ServerInstanceManager instance = null;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private final Path directory = Paths.get("./instances.json");
    private ArrayList<ServerConfig> instances;


    private ServerInstanceManager() {
        instances = new ArrayList<>();
        readFromSerialized();
    }

    public static ServerInstanceManager getInstance() {
        if(instance == null) {
            instance = new ServerInstanceManager();
        }

        return instance;
    }

    public boolean instanceNameExists(String name) {
        return instances.stream().anyMatch(instance -> instance.getConfigName().equalsIgnoreCase(name));
    }

    public void addInstance(ServerConfig config) {
        instances.add(config);

        writeToSerialized();
    }

    public void deleteInstance(ServerConfig instance) throws IOException {
        instances.remove(instance);

        writeToSerialized();

        Files.walk(Paths.get(instance.getAbsoluteStoragePath()))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }

    public ServerConfig getInstanceByName(String name) {
        for(ServerConfig config : instances) {
            if(config.getConfigName().equalsIgnoreCase(name)) {
                return config;
            }
        }

        return null;
    }

    private void writeToSerialized() {
        try {
            Files.writeString(directory, gson.toJson(instances));
        } catch (IOException e) {
            ArgParser.exitWithErrorMessage("Failed serializing server instances to file: " + e);
        }
    }

    private void readFromSerialized() {
        if(!Files.exists(directory)) {
            return;
        }

        try {
            Type listType = new TypeToken<ArrayList<ServerConfig>>() {}.getType();
            instances = gson.fromJson(Files.readString(directory), listType);
        } catch (IOException e) {
            ArgParser.exitWithErrorMessage("Failed deserializing server instances from file: " + e);
        }
    }

    public ArrayList<ServerConfig> getInstances() {
        return instances;
    }
}
