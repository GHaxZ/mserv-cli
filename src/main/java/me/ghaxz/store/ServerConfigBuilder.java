package me.ghaxz.store;

public class ServerConfigBuilder {
    private final ServerConfig config;

    public ServerConfigBuilder() {
        config = new ServerConfig();
    }

    public ServerConfigBuilder setConfigName(String configName) {
        config.setConfigName(configName);
        return this;
    }

    public ServerConfigBuilder setStorageDirectory(String storageDirectory) {
        config.setStorageDirectory(storageDirectory);
        return this;
    }

    public ServerConfigBuilder setType(JarType type) {
        config.setType(type);
        return this;
    }

    public ServerConfigBuilder setVersion(String version) {
        config.setVersion(version);
        return this;
    }

    public ServerConfig getConfig() {
        return config;
    }
}
