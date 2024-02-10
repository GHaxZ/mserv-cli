package me.ghaxz.cli;

import com.sun.management.OperatingSystemMXBean;
import me.ghaxz.notification.NotificationEvent;
import me.ghaxz.notification.NotificationSubscriber;
import me.ghaxz.server.InstanceCreator;
import me.ghaxz.store.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
Parses command line arguments and executes corresponding code
 */

// todo implement default values

public class ArgParser implements NotificationSubscriber {
    public void parseArgs(ArrayList<String> args) {
        if (!ConfigFile.exists()) {
            System.out.println("First time startup, entering configuration mode.");

            Interface.getInterface().setupInterface();
        }

        if (args.isEmpty()) {
            // todo implement help text
            return;
        }

        if (args.contains("--setup")) {
            Interface.getInterface().setupInterface();

            return;
        }

        switch (args.get(0)) {
            case "new" -> parseNewArgument(args);

            case "delete" -> {
                // todo implement deletion of server instances once storing them is implemented
            }

            case "list" -> {
                // todo implement listing server instances once storing them is implemented
            }

            case "start" -> {
                // todo load server instance and start the server
            }

            case "edit" -> {
                // todo edit server instance
            }
        }
    }

    private void parseNewArgument(ArrayList<String> args) {
        ServerConfigBuilder builder = new ServerConfigBuilder();

        if (args.size() > 1) {
            // todo check if instance name already exists

            String name = args.get(1);

            builder.setConfigName(name);

            if (args.contains("--dir")) {
                if (args.indexOf("--dir") + 1 < args.size()) {
                    String dir = args.get(args.indexOf("--dir") + 1);

                    if(dir.contains("--")) {
                        exitWithErrorMessage("Missing save directory: \"--dir [DIRECTORY]\"");
                    }

                    if (Files.isDirectory(Paths.get(dir))) {
                        builder.setStorageDirectory(dir);
                    } else {
                        exitWithErrorMessage("Not a valid directory: \"" + dir + "\"");
                    }
                } else {
                    exitWithErrorMessage("Missing save directory: \"--dir [DIRECTORY]\"");
                }
            } else {
                String defaultDir = ConfigFile.getConfig().getDefaultDirectory();

                if (defaultDir == null) {
                    exitWithErrorMessage("Couldn't read a valid default directory from config file.\n" +
                            "Use the --dir argument to specify one manually, or configure the default directory.");
                }
            }

            if(args.contains("--software")) {
                if (args.indexOf("--software") + 1 < args.size()) {
                    String software = args.get(args.indexOf("--software") + 1);

                    if(software.contains("--")) {
                        exitWithErrorMessage("Missing server software: \"--software [SOFTWARE]\"");
                    }

                    JarType jarType = null;

                    try {
                        jarType = JarTypeManager.getInstance().getJarTypeByName(software);
                    } catch (IOException e) {
                        ArgParser.exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
                    }

                    if(jarType != null) {
                        builder.setType(jarType);
                    } else {
                        exitWithErrorMessage("This software is not available, run \"mserv new\" to enter the guided configuration mode.");
                    }
                } else {
                    exitWithErrorMessage("Missing server software: \"--software [SOFTWARE]\"");
                }
            }

            if(args.contains("--version")) {
                if (args.indexOf("--version") + 1 < args.size()) {
                    String version = args.get(args.indexOf("--version") + 1);

                    if(version.contains("--")) {
                        exitWithErrorMessage("Missing version number: \"--version [VERSION]\"");
                    }

                    JarVersionManager versionManager = null;

                    try {
                        versionManager = JarVersionManager.getManager(builder.getConfig().getType());
                    } catch (IOException e) {
                        ArgParser.exitWithErrorMessage("\nFailed fetching available versions from API: " + e);
                    }

                    if(versionManager.getAllVersions().stream().anyMatch(jarVersion -> jarVersion.getVersion().equals(version.toLowerCase()))) {
                        builder.setVersion(versionManager.getJarVersionByVersionName(version));
                    } else {
                        exitWithErrorMessage("This version is not available, run \"mserv new\" to enter the guided configuration mode.");
                    }
                } else {
                    exitWithErrorMessage("Missing version number: \"--version [VERSION]\"");
                }
            }

            if(args.contains("--ram")) {
                if (args.indexOf("--ram") + 1 < args.size()) {
                    String ram = args.get(args.indexOf("--ram") + 1);

                    if(ram.contains("--")) {
                        exitWithErrorMessage("Missing RAM amount in MB: \"--ram [RAM_AMOUNT]\"");
                    }

                    try {
                        long ramLong = Long.parseLong(ram);

                        long ramSize = ((OperatingSystemMXBean) ManagementFactory
                                .getOperatingSystemMXBean()).getTotalMemorySize();

                        if(ramLong < 1) {
                            exitWithErrorMessage("The RAM amount needs to be more than 0MB.");
                        } else if (ramLong > ramSize / 1_000_000) {
                            exitWithErrorMessage(ramLong + "MB is larger than the systems RAM size (" + ramSize / 1_000_000 + "MB).");
                        } else {
                            builder.setRAM(ramLong);
                        }
                    } catch (NumberFormatException e) {
                        exitWithErrorMessage("This is not a valid RAM value.");
                    }
                } else {
                    exitWithErrorMessage("Missing RAM amount in MB: \"--ram [RAM_AMOUNT]\"");
                }
            }

            System.out.print("Initializing instance setup");
            try {
                InstanceCreator creator = new InstanceCreator(builder.getConfig());

                subscribe(creator);

                creator.setUpInstance();

                subscribe(creator);
            } catch (IOException e) {
                ArgParser.exitWithErrorMessage("Failed creating server instance: " + e);
            }
        } else {
            Interface.getInterface().configureInterface();
        }
    }

    public static void exitWithErrorMessage(String msg) {
        System.err.println(msg);
        System.exit(1);
    }

    @Override
    public void onNotification(NotificationEvent event) {
        System.out.printf("\r%s", event.getMessage());
    }
}
