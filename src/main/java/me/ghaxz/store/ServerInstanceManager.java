package me.ghaxz.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.ghaxz.cli.ArgParser;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

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

    public void updateInstance(ServerConfig originalInstance, ServerConfig newConfig) throws IOException {
        int index = instances.indexOf(originalInstance);

        if(index == -1) return;

        instances.set(index, newConfig);

        moveServerInstance(originalInstance, Path.of(newConfig.getAbsoluteStoragePath()));
    }

    private void moveServerInstance(ServerConfig instance, Path newDir) throws IOException {
        Path oldDir = Path.of(instance.getAbsoluteStoragePath());

        if(oldDir.equals(newDir)) {
            return;
        }

        // TODO: implement the moving of server instance files to new directory (name change or storage dir change)

        // Create new directory
        Files.createDirectory(newDir);

        // Walk old directory, move to new directory
        Files.walk(oldDir)
                .forEach(path -> {
                    try {
                        Path newPath = newDir.resolve(oldDir.relativize(path));

                        Files.move(path, newPath);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
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
