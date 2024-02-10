package me.ghaxz.server;

import me.ghaxz.notification.NotificationEvent;
import me.ghaxz.notification.NotificationPublisher;
import me.ghaxz.notification.NotificationSubscriber;
import me.ghaxz.notification.NotificationType;
import me.ghaxz.store.ServerConfig;
import me.ghaxz.supplier.JarDownloader;

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
        String dir = config.getStorageDirectory() +
                FileSystems.getDefault().getSeparator() +
                config.getConfigName();

        String fileOutputDir = dir +
                FileSystems.getDefault().getSeparator() +
                config.getVersion().getJarFilename();

        JarDownloader downloader = new JarDownloader(config, fileOutputDir);

        createDirectory(dir);
        downloadJar(downloader);
        createEula(dir);
    }

    private void createDirectory(String directory) throws IOException {
        notify("Creating directory ...", NotificationType.INFO);

        Files.createDirectory(Paths.get(directory));

        notify("Created directory", NotificationType.COMPLETED);
    }

    private void downloadJar(JarDownloader downloader) throws IOException {
        notify("Downloading jar ...\n", NotificationType.INFO);

        subscribe(downloader);
        downloader.download();
        unsubscribe(downloader);

        notify("Downloaded jar", NotificationType.COMPLETED);
    }

    private void createEula(String dir) throws IOException {
        notify("Creating eula.txt ...", NotificationType.INFO);
        Files.writeString(Paths.get(dir + FileSystems.getDefault().getSeparator() + "eula.txt"), "eula=true");
        notify("Created eula.txt", NotificationType.COMPLETED);
    }

    @Override
    public void onNotification(NotificationEvent event) {
        notify(event.getMessage(), event.getType());
    }
}
