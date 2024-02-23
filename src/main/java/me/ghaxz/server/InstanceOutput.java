package me.ghaxz.server;

import me.ghaxz.notification.NotificationPublisher;
import me.ghaxz.notification.NotificationType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InstanceOutput implements NotificationPublisher {
    public void notifyServerOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            notify(line, NotificationType.INFO);
        }
    }
}
