package me.ghaxz.store;

import me.ghaxz.interfaces.ArgParser;

import java.io.IOException;
import java.io.Serializable;

public class ServerConfigBuilder implements Serializable{
    private ServerConfig config;

    public ServerConfigBuilder(String configName) {
        try {
            config = new ServerConfig(configName);
        } catch (IOException e) {
            ArgParser.exitWithErrorMessage("Failed to load config file.");
        }
    }

    public ServerConfigBuilder addStorageDirectory(String storageDirectory) {
        config.setStorageDirectory(storageDirectory);
        return this;
    }

    public ServerConfigBuilder addType(String type) {
        config.setType(type);
        return this;
    }

    public ServerConfigBuilder addVersion(String version) {
        config.setVersion(version);
        return this;
    }

    public ServerConfig getConfig() {
        return config;
    }
}
