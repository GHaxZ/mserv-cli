package me.ghaxz.store;


import java.util.Objects;

/*
Stores information about a configured server instance, that gets serialized for storing server instance configuration details
 */
public class ServerConfig implements Cloneable {
    private String configName;
    private String storagePath;
    private String absoluteStoragePath;

    private String jarStoragePath;
    private JarType type;
    private JarVersion version;
    private long ramMB;

    // default values
    public ServerConfig() {
        this.configName = null;

        storagePath = null;

        absoluteStoragePath = null;

        type = null;

        version = null;

        ramMB = 0;
    }

    public ServerConfig(ServerConfig config) {
        this.configName = config.getConfigName();

        storagePath = config.getStoragePath();

        absoluteStoragePath = config.getAbsoluteStoragePath();

        type = config.getType();

        version = config.getVersion();

        ramMB = config.getRamMB();
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setAbsoluteStoragePath(String absoluteStoragePath) {
        this.absoluteStoragePath = absoluteStoragePath;
    }

    public void setJarStoragePath(String jarStoragePath) {
        this.jarStoragePath = jarStoragePath;
    }

    public void setType(JarType type) {
        this.type = type;
    }

    public void setRamMB(long ramMB) {
        this.ramMB = ramMB;
    }

    public void setVersion(JarVersion version) {
        this.version = version;
    }

    public String getConfigName() {
        return configName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getAbsoluteStoragePath() {
        return absoluteStoragePath;
    }

    public String getJarStoragePath() {
        return jarStoragePath;
    }

    public JarType getType() {
        return type;
    }

    public JarVersion getVersion() {
        return version;
    }

    public long getRamMB() {
        return ramMB;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "configName='" + configName + '\'' +
                ", storageDirectory='" + storagePath + '\'' +
                ", type=" + type +
                ", version=" + version +
                ", ram=" + ramMB +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerConfig config = (ServerConfig) o;
        return Objects.equals(configName, config.configName);
    }
}
