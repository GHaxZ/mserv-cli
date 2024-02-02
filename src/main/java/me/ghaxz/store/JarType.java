package me.ghaxz.store;

// zb type = bedrock, modded, proxies, servers, vanilla --- name = vanilla, snapshot, paper, fabric, etc.
public final class JarType {
    private final String type;
    private final String name;
    private final String apiURL;

    public JarType(String type, String name) {
        this.type = type;
        this.name = name;
        this.apiURL = type + "/" + name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getApiURL() {
        return apiURL;
    }
}
