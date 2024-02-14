package me.ghaxz.server;

import me.ghaxz.cli.Input;
import me.ghaxz.notification.NotificationPublisher;
import me.ghaxz.notification.NotificationType;
import me.ghaxz.store.ServerConfig;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class InstanceRunner implements NotificationPublisher {
    public static void runInstance(ServerConfig instance) throws IOException {
        // Defines command and arguments
        String[] command = new String[] {"java", "-Xmx4G", "-Xms4G", "-jar", "\"" + instance.getVersion().getJarFilename() + "\"", "--nogui"};
        // Creates and runs the command turning it into a process
        Process process = new ProcessBuilder().command(command).directory(new File(instance.getAbsoluteStoragePath())).start();

        // Starts new Thread that reads and prints the server output
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed reading server output: " + e);
            }
        }).start();

        // todo add server command input functionality
    }
}
