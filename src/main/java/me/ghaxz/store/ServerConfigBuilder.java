package me.ghaxz.store;

/*
Allows for a ServerConfig to be built step by step and overriding default values
 */
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

    public ServerConfigBuilder setVersion(JarVersion version) {
        config.setVersion(version);
        return this;
    }

    public ServerConfigBuilder setRAM(long ram) {
        config.setRam(ram);
        return this;
    }

    public ServerConfig getConfig() {
        return config;
    }
}
