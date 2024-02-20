package me.ghaxz.cli;

import com.sun.management.OperatingSystemMXBean;
import me.ghaxz.model.helper.StringHelper;
import me.ghaxz.model.notification.NotificationEvent;
import me.ghaxz.model.notification.NotificationSubscriber;
import me.ghaxz.model.server.InstanceCreator;
import me.ghaxz.model.store.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
Handles all the user interaction
 */

public class Interface implements NotificationSubscriber {
    private static Interface instance = null;

    private Interface() {

    }

    public static Interface getInterface() {
        if (instance == null) {
            instance = new Interface();
        }

        return instance;
    }

    public void setupInterface() {
        while (true) {
            System.out.print("\nSpecify default save directory for server instances: ");

            String dir = Input.readString();

            if (!Files.isDirectory(Paths.get(dir))) {
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

        System.out.println();
    }

    // Guided server configuration process (fetching all data, display it, read selection, if no default dir require dir specification, RAM size)
    // No script for startup required, all managed in program (run "java -XmS[RAM_SIZE] -Xmx[RAM_SIZE] -jar [JAR_NAME]" from tool)
    public void configureInterface() {
        System.out.println("Initializing ...");
        ServerConfigBuilder builder = new ServerConfigBuilder();
        ConfigFile configFile = ConfigFile.getConfig();

        System.out.println("\n-- Configure the server --");

        while (true) {
            System.out.print("\nEnter server instance name: ");

            String name = Input.sanatizeString(Input.readString());

            if (!name.isBlank()) {
                if (!ServerInstanceManager.getInstance().instanceNameExists(name)) {
                    builder.setConfigName(name);

                    break;
                } else {
                    System.out.println("\nThis instance name already exists.");
                }
            }
        }

        while (true) {
            System.out.println("\n-- Configure save directory --");

            String defaultDir = configFile.getDefaultDirectory();

            if (defaultDir == null) {
                System.out.println("\nNo valid default directory configured in config file, directory is required.");

                System.out.print("\nSpecify save directory for server instance: ");

                String dir = Input.sanatizeString(Input.readString());

                if (!dir.isBlank()) {
                    if (Files.isDirectory(Paths.get(dir))) {
                        builder.setStorageDirectory(dir);
                        break;
                    } else {
                        System.out.println("\nThis is not a valid directory.");
                    }
                }
            } else {
                System.out.print("\nSpecify save directory for server instance" +
                        ", leave blank for default (" + defaultDir + "): ");

                String dir = Input.sanatizeString(Input.readString());

                if (dir.isBlank()) {
                    builder.setStorageDirectory(defaultDir);
                    break;
                } else {
                    if (Files.isDirectory(Paths.get(dir))) {
                        builder.setStorageDirectory(dir);
                        break;
                    } else {
                        System.out.println("\nThis is not a valid directory.");
                    }
                }
            }
        }

        while (true) {
            System.out.println("\n-- Configure Server Software --");

            String currectCategory = "";

            System.out.print("\nAvailable software:");

            JarTypeManager jarTypeManager = null;

            try {
                jarTypeManager = JarTypeManager.getInstance();
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
            }

            for (JarType jar : jarTypeManager.getJarTypes()) {
                if (currectCategory.equals(jar.getCategory())) {
                    System.out.print(", " + StringHelper.capitalizeString(jar.getSoftware()));
                } else {
                    currectCategory = jar.getCategory();
                    System.out.println("\n\n[" + StringHelper.capitalizeString(currectCategory) + "]");


                    System.out.print(StringHelper.capitalizeString(jar.getSoftware()));
                }
            }

            System.out.print("\n\nSpecify server software, leave blank for default (vanilla): ");

            String software = Input.sanatizeString(Input.readString());

            if (software.isBlank()) {
                builder.setType(jarTypeManager.getJarTypeByName("vanilla"));
                break;
            } else {
                JarType type = jarTypeManager.getJarTypeByName(software.toLowerCase());

                if (type != null) {
                    builder.setType(type);
                    break;
                } else {
                    System.out.println("\nThis is not an available software.");
                }
            }
        }

        while (true) {
            System.out.println("\n-- Configure server version --");

            System.out.println("\nAvailable versions for " + StringHelper.capitalizeString(builder.getConfig().getType().getSoftware()) + ": \n");

            JarVersionManager versionManager = null;

            try {
                versionManager = JarVersionManager.getManager(builder.getConfig().getType());
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("\nFailed fetching available versions from API: " + e);
            }


            for (JarVersion version : versionManager.getAllVersions()) {
                System.out.printf("%s - %.2fMB%n", version.getVersion(), version.getByteSize() / 1_000_000f);
            }

            System.out.print("\nSpecify server version, leave blank for default (" +
                    versionManager.getNewestVersion().getVersion() + " - newest): "
            );

            String version = Input.sanatizeString(Input.readString());

            if (version.isBlank()) {
                builder.setVersion(versionManager.getNewestVersion());
                break;
            } else {
                JarVersion jarVersion = versionManager.getJarVersionByVersionName(version);

                if (jarVersion != null) {
                    builder.setVersion(versionManager.getJarVersionByVersionName(version));
                    break;
                } else {
                    System.out.println("\nThis is not an available version.");
                }
            }
        }

        while (true) {
            System.out.println("\n-- Configure server RAM --");

            System.out.println("\nMake sure to choose a sensible amount of RAM.\n" +
                    "Generally, you shouldn't use more than half of your RAM, if you're also playing on this system.");

            long ramSize = ((OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean()).getTotalMemorySize();

            System.out.println("\nSystem RAM size: " + ramSize / 1_000_000 + "MB");

            System.out.print("Specify RAM amount in MB, leave blank for default (2048MB): ");

            String ram = Input.sanatizeString(Input.readString());

            if (ram.isBlank()) {
                builder.setRAM(2048);
                break;
            } else {
                try {
                    long ramLong = Long.parseLong(ram);

                    if (ramLong < 1) {
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

        runConfiguration(builder.build());
    }

    public void deleteInterface(ServerConfig instance) {
        while (true) {
            System.out.println("\n-- Confirm deletion --");

            System.out.print("Are you sure you want to delete \"" + instance.getConfigName() + "\"? (y/n): ");

            String answer = Input.sanatizeString(Input.readString());

            if (!answer.isBlank()) {
                if (answer.equalsIgnoreCase("y")) {
                    try {
                        ServerInstanceManager.getInstance().deleteInstance(instance);

                        System.out.println("\nSuccessfully deleted \"" + instance.getConfigName() + "\".");
                        break;
                    } catch (IOException e) {
                        ArgParser.exitWithErrorMessage("Failed to delete instance: " + e);
                    }
                } else if (answer.equalsIgnoreCase("n")) {
                    System.out.println("Canceled deletion process.");
                    break;
                } else {
                    System.out.println("\nThis is not a valid answer.");
                }
            }
        }

        System.out.println();
    }

    public void runConfiguration(ServerConfig config) {
        System.out.println("\n-- Running server instance setup --");
        try {
            InstanceCreator creator = new InstanceCreator(config);

            subscribe(creator);

            creator.setUpInstance();

            unsubscribe(creator);
        } catch (IOException e) {
            ArgParser.exitWithErrorMessage("Failed creating server instance: " + e);
        }

        System.out.println();
    }

    public void runList(boolean detailed) {
        ArrayList<ServerConfig> instances = ServerInstanceManager.getInstance().getInstances();

        if(instances.isEmpty()) {
            System.out.println("\nThere are currently no server instances.\n");
            return;
        }

        System.out.println("\nAll server instances" + (detailed ? " in detail" : "") + ":");

        if (detailed) {
            for (ServerConfig config : instances) {
                System.out.println();
                System.out.println("┌ Name: " + config.getConfigName());
                System.out.println("├ Storage path: " + config.getAbsoluteStoragePath());
                System.out.println("├ Category: " + StringHelper.capitalizeString(config.getType().getCategory()));
                System.out.println("├ Software: " + StringHelper.capitalizeString(config.getType().getSoftware()));
                System.out.println("└ RAM allocated: " + config.getRamMB() + "MB");
            }
        } else {
            for (ServerConfig config : instances) {
                System.out.println();
                System.out.println("- " + config.getConfigName());
            }
        }

        System.out.println();
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
