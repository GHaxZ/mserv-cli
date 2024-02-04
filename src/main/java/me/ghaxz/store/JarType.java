package me.ghaxz.store;

import java.util.Objects;

/*
Stores specific jar typ by category (modded, proxies, software, vanilla), name (fabric, paper, bungeecord, vanilla)
and their respective api endpoint and unique identifier url ex. modded/fabric
 */
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

    public String getName() {
        return name;
    }

    public String getApiURL() {
        return apiURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JarType jarType = (JarType) o;
        return Objects.equals(apiURL, jarType.apiURL);
    }

    @Override
    public String toString() {
        return "JarType{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", apiURL='" + apiURL + '\'' +
                '}';
    }
}
