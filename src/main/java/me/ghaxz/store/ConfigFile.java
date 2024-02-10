package me.ghaxz.store;

import me.ghaxz.cli.ArgParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/*
Handles the config file data saving and loading logic. Also gets save config properties.
 */
public class ConfigFile {
    private static ConfigFile instance = null;

    private static final Path configPath = Paths.get("./mserv.conf");
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
        String dir = properties.getProperty("defaultDirectory");

        return Files.isDirectory(Paths.get(dir)) ? dir : null;
    }

    public static boolean exists() {
        return Files.exists(configPath);
    }

    public void setDefaultDirectory(String dir) throws IOException {
        properties.put("defaultDirectory", dir);

        saveProperties();
    }
}
