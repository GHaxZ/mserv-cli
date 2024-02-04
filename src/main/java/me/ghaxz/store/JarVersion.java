package me.ghaxz.store;

/*
Stores a specific jar version by version (1.20.4, 1.8.9), size (46.57MB) and jar filename (paper-1.20.4.jar)
 */
public class JarVersion {
    private final String version;
    private final String size;
    private final String jarFilename;

    public JarVersion(String version, String size, String jarFilename) {
        this.version = version;
        this.size = size;
        this.jarFilename = jarFilename;
    }

    public String getSize() {
        return size;
    }

    public String getVersion() {
        return version;
    }

    public String getJarFilename() {
        return jarFilename;
    }

    @Override
    public String toString() {
        return "JarVersion{" +
                "version='" + version + '\'' +
                ", size='" + size + '\'' +
                ", jarFilename='" + jarFilename + '\'' +
                '}';
    }
}
