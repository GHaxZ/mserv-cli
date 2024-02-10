package me.ghaxz.cli;

import com.sun.management.OperatingSystemMXBean;
import me.ghaxz.notification.NotificationEvent;
import me.ghaxz.notification.NotificationSubscriber;
import me.ghaxz.notification.NotificationType;
import me.ghaxz.server.InstanceCreator;
import me.ghaxz.server.InstanceRunner;
import me.ghaxz.store.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Handles all the user interaction
 */

public class Interface implements NotificationSubscriber {
    private static Interface instance = null;

    private Interface() {

    }

    public static Interface getInterface() {
        if(instance == null) {
            instance = new Interface();
        }

        return instance;
    }

    public void setupInterface() {
        while(true) {
            System.out.print("\nSpecify default save directory for server instances: ");

            String dir = Input.readString();

            if(!Files.isDirectory(Paths.get(dir))) {
                System.out.println("\nThis is not a valid directory.");
                continue;
            }

            try {
                ConfigFile.getConfig().setDefaultDirectory(Input.sanatizeString(dir));

                break;
            } catch (IOException e) {
                System.err.println("Failed writing to config file: " + e);
            }
        }
    }

    // Guided server configuration process (fetching all data, display it, read selection, if no default dir require dir specification, RAM size)
    // No script for startup required, all managed in program (run "java -XmS[RAM_SIZE] -Xmx[RAM_SIZE] -jar [JAR_NAME]" from tool)
    public void configureInterface() {
        System.out.println("Initializing ...");
        ServerConfigBuilder builder = new ServerConfigBuilder();
        ConfigFile configFile = ConfigFile.getConfig();

        System.out.println("\n-- Configure the server --");


        while(true) {
            System.out.print("\nEnter server instance name: ");

            String name = Input.sanatizeString(Input.readString());

            // todo Later check if name is already taken in ServerConfigManager
            if(!name.isBlank()) {
                builder.setConfigName(name);

                break;
            }
        }

        while(true) {
            System.out.println("\n-- Configure save directory --");

            boolean dirRequired = configFile.getDefaultDirectory() == null;

            if(dirRequired) {
                System.out.println("\nNo valid default directory configured in config file, directory is required.");

                System.out.print("\nSpecify save directory for server instance: ");

                String dir = Input.sanatizeString(Input.readString());

                if(!dir.isBlank()) {
                    if(Files.isDirectory(Paths.get(dir))) {
                        builder.setStorageDirectory(dir);
                        break;
                    } else {
                        System.err.println("\nThis is not a valid directory.");
                    }
                }
            } else {
                System.out.print("\nSpecify save directory for server instance" +
                        ", leave blank for default (" + configFile.getDefaultDirectory() + "): ");

                String dir = Input.sanatizeString(Input.readString());

                if(dir.isBlank()) {
                    builder.setStorageDirectory(ConfigFile.getConfig().getDefaultDirectory());
                    break;
                } else {
                    if(Files.isDirectory(Paths.get(dir))) {
                        builder.setStorageDirectory(dir);
                        break;
                    } else {
                        System.err.println("\nThis is not a valid directory.");
                    }
                }
            }
        }

        while(true) {
            System.out.println("\n-- Configure Server Software --");

            String currectCategory = "";

            System.out.print("\nAvailable software:");

            JarTypeManager jarTypeManager = null;

            try {
                jarTypeManager = JarTypeManager.getInstance();
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
            }

            for(JarType jar : jarTypeManager.getJarTypes()) {
                if(currectCategory.equals(jar.getCategory())) {
                    System.out.print(", " + Input.capitalizeString(jar.getName()));
                } else {
                    currectCategory = jar.getCategory();
                    System.out.println("\n\n[" + Input.capitalizeString(currectCategory)  + "]");


                    System.out.print(Input.capitalizeString(jar.getName()));
                }
            }

            System.out.print("\n\nSpecify server software, leave blank for default (vanilla): ");

            String software = Input.sanatizeString(Input.readString());

            if(software.isBlank()) {
                builder.setType(jarTypeManager.getJarTypeByName("vanilla"));
                break;
            } else {
                JarType type = jarTypeManager.getJarTypeByName(software.toLowerCase());

                if(type != null) {
                    builder.setType(type);
                    break;
                } else {
                    System.out.println("\nThis is not an available software.");
                }
            }
        }

        while(true) {
            System.out.println("\n-- Configure server version --");

            System.out.println("\nAvailable versions for " + Input.capitalizeString(builder.getConfig().getType().getName()) + ": \n");

            JarVersionManager versionManager = null;

            try {
                versionManager = JarVersionManager.getManager(builder.getConfig().getType());
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("\nFailed fetching available versions from API: " + e);
            }


            for(JarVersion version : versionManager.getAllVersions()) {
                System.out.println(version.getVersion() + " - " + version.getByteSize() / 1_000_000);
            }

            System.out.print("\nSpecify server version, leave blank for default (" +
                    versionManager.getNewestVersion().getVersion() + " - newest): "
            );

            String version = Input.sanatizeString(Input.readString());

            if(version.isBlank()) {
                builder.setVersion(versionManager.getNewestVersion());
                break;
            } else {
                JarVersion jarVersion = versionManager.getJarVersionByVersionName(version);

                if(jarVersion != null) {
                    builder.setVersion(versionManager.getJarVersionByVersionName(version));
                    break;
                } else {
                    System.out.println("\nThis is not an available version.");
                }
            }
        }

        while(true) {
            System.out.println("\n-- Configure server RAM --");

            System.out.println("\nMake sure to choose a sensible amount of RAM.\n" +
                    "Generally, you shouldn't use more than half of your RAM, if you're also playing on this system.");

            long ramSize = ((OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean()).getTotalMemorySize();

            System.out.println("\nSystem RAM size: " + ramSize / 1_000_000 + "MB");

            System.out.print("Specify RAM amount in MB, leave blank for default (2048MB): ");

            String ram = Input.sanatizeString(Input.readString());

            if(ram.isBlank()) {
                builder.setRAM(2048);
                break;
            } else {
                try {
                    long ramLong = Long.parseLong(ram);

                    if(ramLong < 1) {
                        System.out.println("\nThe RAM amount needs to be more than 0MB.");
                    } else if (ramLong > ramSize / 1_000_000) {
                        System.out.println("\n" + ramLong + "MB is larger than the systems RAM size.");
                    } else {
                        builder.setRAM(ramLong);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\nThis is not a valid RAM value.");
                }
            }
        }

        System.out.println("-- Running server instance setup --");
        try {
            InstanceCreator creator = new InstanceCreator(builder.getConfig());

            subscribe(creator);

            creator.setUpInstance();

            unsubscribe(creator);
        } catch (IOException e) {
            ArgParser.exitWithErrorMessage("Failed creating server instance: " + e);
        }
    }

    @Override
    public void onNotification(NotificationEvent event) {
        switch (event.getType()) {
            case INFO -> {
                System.out.print("\n" + event.getMessage());
            }

            case COMPLETED -> {
                System.out.println("\n" + event.getMessage());
            }

            case PROGRESS -> {
                System.out.print("\r" + event.getMessage());
            }
        }
    }
}
