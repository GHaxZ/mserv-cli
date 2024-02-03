package me.ghaxz.store;

import me.ghaxz.interfaces.ArgParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFile {
    private static ConfigFile instance = null;

    private static final Path configPath = Paths.get("./config.properties");
    private final Properties properties = new Properties();

    public static ConfigFile getConfig() {
        if(instance == null) {
            try {
                instance = new ConfigFile();
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("Failed loading config file: " + e);
            }
        }

        return instance;
    }

    private ConfigFile() throws IOException {
        loadProperties();
    }

    private void loadProperties() throws IOException {
        if(!Files.exists(configPath)) {
            Files.createFile(configPath);
        }

        properties.load(Files.newInputStream(configPath));
    }

    private void saveProperties() throws IOException {
        properties.store(Files.newOutputStream(configPath), "This is the configuration file for mserv");
    }

    public String getDefaultDirectory() {
        return properties.getProperty("defaultDirectory");
    }

    public static boolean exists() {
        return Files.exists(configPath);
    }

    public void setDefaultDirectory(String dir) throws IOException {
        properties.put("defaultDirectory", dir);

        saveProperties();
    }
}
