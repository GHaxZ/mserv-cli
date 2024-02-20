package me.ghaxz.model.store;

/*
Stores a specific jar version by version (1.20.4, 1.8.9), size (46.57MB) and jar filename (paper-1.20.4.jar)
 */
public class JarVersion {
    private final String version;
    private transient final long byteSize;
    private final String jarFilename;

    public JarVersion(String version, long byteSize, String jarFilename) {
        this.version = version;
        this.jarFilename = jarFilename;
        this.byteSize = byteSize;
    }

    public String getVersion() {
        return version;
    }

    public long getByteSize() {
        return byteSize;
    }

    public String getJarFilename() {
        return jarFilename;
    }

    @Override
    public String toString() {
        return "JarVersion{" +
                "version='" + version + '\'' +
                ", size='" + byteSize + '\'' +
                ", jarFilename='" + jarFilename + '\'' +
                '}';
    }
}
