package me.ghaxz.store;

import me.ghaxz.interfaces.ArgParser;

import java.io.IOException;

// Add RAM size

public class ServerConfig {
    private final String configName;
    private String storageDirectory;
    private String type; // vanilla (vanilla, snapshot, ...), servers (paper, spigot, ...), modded (fabric, forge, ...), proxies (Waterfall, Bungeecord, ...)
    private String version;

    // default values
    public ServerConfig(String configName) throws IOException {
        this.configName = configName;

        String defaultDir = ConfigFile.getConfig().getDefaultDirectory();

        if(defaultDir == null) {
            ArgParser.exitWithErrorMessage("Couldn't read default directory from config file.\n" +
                    "Use the --dir argument to specify one manually, or configure the default directory.");
        }

        if(defaultDir != null && defaultDir.isBlank()) {
            ArgParser.exitWithErrorMessage("Couldn't read default directory from config file.\n" +
                    "Use the --dir argument to specify one manually, or configure the default directory.");
        }

        storageDirectory = defaultDir;

        // todo (see JarTypeManager)
        type = null;

        // If empty, api returns latest
        version = "";
    }

    public void setStorageDirectory(String storageDirectory) {
        this.storageDirectory = storageDirectory;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfigName() {
        return configName;
    }

    public String getStorageDirectory() {
        return storageDirectory;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }
}
