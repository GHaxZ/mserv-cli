package me.ghaxz.store;

// zb type = bedrock, modded, proxies, servers, vanilla --- name = vanilla, snapshot, paper, fabric, etc.
public final class JarType {
    private final String category;
    private final String name;
    private final String apiURL;

    public JarType(String category, String name) {
        this.category = category;
        this.name = name;
        this.apiURL = category + "/" + name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "JarType{" +
                "type='" + category + '\'' +
                ", name='" + name + '\'' +
                ", apiURL='" + apiURL + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getApiURL() {
        return apiURL;
    }
}
