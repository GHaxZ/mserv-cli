package me.ghaxz.store;

import java.nio.file.FileSystems;

/*
Allows for a ServerConfig to be built step by step and overriding default values
 */
public class ServerConfigBuilder {
    private final ServerConfig config;

    public ServerConfigBuilder() {
        config = new ServerConfig();
    }

    public ServerConfigBuilder(ServerConfig config) {
        this.config = config;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public ServerConfigBuilder setConfigName(String configName) {
        config.setConfigName(configName);
        return this;
    }

    public ServerConfigBuilder setStorageDirectory(String storageDirectory) {
        config.setStoragePath(storageDirectory);
        return this;
    }

    public ServerConfigBuilder setType(JarType type) {
        config.setType(type);
        return this;
    }

    public ServerConfigBuilder setVersion(JarVersion version) {
        config.setVersion(version);
        return this;
    }

    public ServerConfigBuilder setRAM(long ram) {
        config.setRamMB(ram);
        return this;
    }

    public ServerConfig build() {
        String configName = config.getConfigName();
        String storageDirectory = config.getStoragePath();

        if(configName != null && storageDirectory != null) {
            config.setAbsoluteStoragePath(storageDirectory + FileSystems.getDefault().getSeparator() + configName);
        }

        if(config.getAbsoluteStoragePath() != null) {
            config.setJarStoragePath(config.getAbsoluteStoragePath() +
                    FileSystems.getDefault().getSeparator() +
                    config.getVersion().getJarFilename());
        }

        return config;
    }
}
