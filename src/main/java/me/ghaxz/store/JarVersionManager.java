package me.ghaxz.store;

import me.ghaxz.interfaces.ArgParser;
import me.ghaxz.supplier.JarInfoFetcher;

import java.io.IOException;
import java.util.ArrayList;


/*
Manages the JarVersion instances for a JarType and also stores instances of itself,
so information for a jar type does not have to be fetched multiple times
 */
public class JarVersionManager {
    private static final ArrayList<JarVersionManager> managers = new ArrayList<>();

    private final JarType jarType;
    private final ArrayList<JarVersion> jarVersions;

    public JarVersionManager(JarType jarType) throws IOException {
        this.jarType = jarType;
        jarVersions = JarInfoFetcher.fetchAllJarTypeVersions(jarType);
    }

    public static JarVersionManager getManager(JarType type) {
        if(managers.stream().noneMatch(manager -> manager.getJarType().equals(type))) {
            try {
                managers.add(new JarVersionManager(type));
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("Failed fetching available jar versions from API: " + e);
            }
        }

        return managers.stream().filter(jarVersionManager -> jarVersionManager.getJarType().equals(type)).findFirst().get();
    }

    public JarVersion getNewestVersion() {
        if(!jarVersions.isEmpty()) {
            return jarVersions.get(0);
        }

        return null;
    }

    public JarVersion getJarVersionByVersionName(String versionName) {
        for(JarVersion version : jarVersions) {
            if(version.getVersion().equals(versionName)) {
                return version;
            }
        }

        return null;
    }

    public ArrayList<JarVersion> getAllVersions() {
        return jarVersions;
    }

    public JarType getJarType() {
        return jarType;
    }
}
