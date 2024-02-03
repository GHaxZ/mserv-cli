package me.ghaxz.store;

// Add RAM size

public class ServerConfig {
    private String configName;



    private String storageDirectory;
    private JarType type; // vanilla (vanilla, snapshot, ...), servers (paper, spigot, ...), modded (fabric, forge, ...), proxies (Waterfall, Bungeecord, ...)
    private String version;
    private int ram;

    // default values
    public ServerConfig() {
        this.configName = null;

        storageDirectory = ConfigFile.getConfig().getDefaultDirectory();

        type = JarTypeManager.getInstance().getJarTypeByName("vanilla");

        // If empty, api returns latest
        version = "";

        // 2G ram is default
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

    public void setRam(int ram) {
        this.ram = ram;
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

    public JarType getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public int getRam() {
        return ram;
    }
}
