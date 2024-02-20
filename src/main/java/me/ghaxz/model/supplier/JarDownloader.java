package me.ghaxz.model.supplier;

import me.ghaxz.model.notification.NotificationPublisher;
import me.ghaxz.model.notification.NotificationType;
import me.ghaxz.model.store.ServerConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/*
Responsible for downloading jars from the API (https://serverjars.com/)
 */

public class JarDownloader implements NotificationPublisher {
    private final ServerConfig config;
    private final String dir;

    private long bytesRead;

    public JarDownloader(ServerConfig config, String dir) {
        this.config = config;
        this.dir = dir;
    }

    public void download() throws IOException {
        URL url = new URL("https://serverjars.com/api/fetchJar/" + config.getType().getApiURL() + "/" + config.getVersion().getVersion() + "/");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);


        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = new FileOutputStream(dir)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                this.bytesRead += bytesRead;
                notify("%.2fMB / %.2fMB - %.0f%%".formatted(getDownloadProgress(), getDownloadSize(), getDownloadPercent()), NotificationType.PROGRESS);
            }
        }
    }


    private float getDownloadPercent() {
        return (float) bytesRead / (float) config.getVersion().getByteSize() * 100f;
    }

    private float getDownloadProgress() {
        return (float) bytesRead / 1_000_000;
    }

    private float getDownloadSize() {
        return (float) config.getVersion().getByteSize() / 1_000_000;
    }
}
