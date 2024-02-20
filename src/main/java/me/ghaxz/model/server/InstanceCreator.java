package me.ghaxz.model.server;

import me.ghaxz.model.notification.NotificationEvent;
import me.ghaxz.model.notification.NotificationPublisher;
import me.ghaxz.model.notification.NotificationSubscriber;
import me.ghaxz.model.notification.NotificationType;
import me.ghaxz.model.store.ServerConfig;
import me.ghaxz.model.store.ServerInstanceManager;
import me.ghaxz.model.supplier.JarDownloader;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InstanceCreator implements NotificationSubscriber, NotificationPublisher {
    private final ServerConfig config;

    public InstanceCreator(ServerConfig config) {
        this.config = config;
    }

    public void setUpInstance() throws IOException {

        saveConfiguration(config);
        createDirectory(config);
        downloadJar(config);
        createEula(config);

        notify("Finished server instance setup!", NotificationType.COMPLETED);
    }

    private void saveConfiguration(ServerConfig config) {
        notify("Saving server configuration ...", NotificationType.INFO);
        ServerInstanceManager.getInstance().addInstance(config);
        notify("Saved server configuration", NotificationType.COMPLETED);
    }

    private void createDirectory(ServerConfig config) throws IOException {
        notify("Creating directory ...", NotificationType.INFO);

        Files.createDirectory(Paths.get(config.getAbsoluteStoragePath()));

        notify("Created directory", NotificationType.COMPLETED);
    }

    private void downloadJar(ServerConfig config) throws IOException {
        JarDownloader downloader = new JarDownloader(config, config.getJarStoragePath());

        notify("Downloading jar ...\n", NotificationType.INFO);

        subscribe(downloader);
        downloader.download();
        unsubscribe(downloader);

        notify("Downloaded jar", NotificationType.COMPLETED);
    }

    private void createEula(ServerConfig config) throws IOException {
        notify("Creating eula.txt ...", NotificationType.INFO);
        Files.writeString(Paths.get(config.getAbsoluteStoragePath() + FileSystems.getDefault().getSeparator() + "eula.txt"), "eula=true");
        notify("Created eula.txt", NotificationType.COMPLETED);
    }

    @Override
    public void onNotification(NotificationEvent event) {
        notify(event.getMessage(), event.getType());
    }
}
