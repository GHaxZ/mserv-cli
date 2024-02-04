package me.ghaxz.store;


import me.ghaxz.interfaces.ArgParser;

/*
Stores information about a configured server instance, that gets serialized for storing server instance configuration details
 */
public class ServerConfig {
    private String configName;
    private String storageDirectory;
    private JarType type;
    private JarVersion version;
    private long ram;

    // default values
    public ServerConfig() {
        this.configName = null;

        storageDirectory = null;

        type = null;

        version = null;

        // 2GB ram is default
        ram = 2048;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setStorageDirectory(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public void setType(JarType type) {
        this.type = type;
    }

    public void setRam(long ram) {
        this.ram = ram;
    }

    public void setVersion(JarVersion version) {
        this.version = version;
    }

    public String getConfigName() {
        return configName;
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public JarType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "configName='" + configName + '\'' +
                ", storageDirectory='" + storageDirectory + '\'' +
                ", type=" + type +
                ", version=" + version +
                ", ram=" + ram +
                '}';
    }

    public JarVersion getVersion() {
        return version;
    }

    public long getRam() {
        return ram;
    }
}
