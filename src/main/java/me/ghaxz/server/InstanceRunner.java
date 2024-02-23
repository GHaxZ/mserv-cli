package me.ghaxz.server;

import me.ghaxz.notification.NotificationPublisher;
import me.ghaxz.notification.NotificationType;
import me.ghaxz.store.ServerConfig;

import java.io.*;

public class InstanceRunner implements NotificationPublisher {
    public void runInstance(ServerConfig instance) throws IOException {
        String[] command = new String[]
                {
                        "java",
                        "-Xmx" + instance.getRamMB() + "M",
                        "-Xms" + instance.getRamMB() + "M", "-jar",
                        "\"" + instance.getVersion().getJarFilename() + "\"",
                        "--nogui"
                };

        notify("Starting server instance \"" + instance.getConfigName() + "\"", NotificationType.INFO);

        Process process = new ProcessBuilder().command(command).directory(new File(instance.getAbsoluteStoragePath())).inheritIO().start();

        try {
            process.waitFor();
            notify("Stopped server instance \"" + instance.getConfigName() + "\"", NotificationType.INFO);
        } catch (InterruptedException e) {
            notify("Server instance stopped unexpectedly with error: " + e, NotificationType.ERROR);
        }
    }
}
