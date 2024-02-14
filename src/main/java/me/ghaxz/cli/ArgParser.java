package me.ghaxz.cli;

import com.sun.management.OperatingSystemMXBean;
import me.ghaxz.notification.NotificationEvent;
import me.ghaxz.notification.NotificationSubscriber;
import me.ghaxz.server.InstanceRunner;
import me.ghaxz.store.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
Parses command line arguments and executes corresponding code
 */

public class ArgParser implements NotificationSubscriber {
    public void parseArgs(ArrayList<String> args) {
        if (!ConfigFile.exists()) {
            System.out.println("First time startup, entering configuration mode.");

            Interface.getInterface().setupInterface();
        }

        if (args.isEmpty()) {
            // todo display help text
            return;
        }

        if (args.contains("--setup")) {
            Interface.getInterface().setupInterface();

            return;
        }

        switch (args.get(0)) {
            case "new" -> parseNewArgument(args);

            case "delete" -> parseDeleteArgument(args);

            case "list" -> parseListArgument(args);


            case "start" -> parseStartArgument(args);

            case "edit" -> {
                // todo edit server instance
            }
        }
    }

    private void parseNewArgument(ArrayList<String> args) {
        ServerConfigBuilder builder = new ServerConfigBuilder();

        if (args.size() > 1) {
            String name = args.get(1);

            if (ServerInstanceManager.getInstance().instanceNameExists(name)) {
                exitWithErrorMessage("The instance name \"" + name + "\" is already used.");
            }

            builder.setConfigName(name);

            if (args.contains("--dir")) {
                if (args.indexOf("--dir") + 1 < args.size()) {
                    String dir = args.get(args.indexOf("--dir") + 1);

                    if (dir.contains("--")) {
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
                } else {
                    builder.setStorageDirectory(defaultDir);
                }
            }

            if (args.contains("--software")) {
                if (args.indexOf("--software") + 1 < args.size()) {
                    String software = args.get(args.indexOf("--software") + 1);

                    if (software.contains("--")) {
                        exitWithErrorMessage("Missing server software: \"--software [SOFTWARE]\"");
                    }

                    JarType jarType = null;

                    try {
                        jarType = JarTypeManager.getInstance().getJarTypeByName(software);
                    } catch (IOException e) {
                        exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
                    }

                    if (jarType != null) {
                        builder.setType(jarType);
                    } else {
                        exitWithErrorMessage("This software is not available, run \"mserv new\" to enter the guided configuration mode.");
                    }
                } else {
                    exitWithErrorMessage("Missing server software: \"--software [SOFTWARE]\"");
                }
            } else {
                try {
                    builder.setType(JarTypeManager.getInstance().getJarTypeByName("vanilla"));
                } catch (IOException e) {
                    exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
                }
            }

            if (args.contains("--version")) {
                if (args.indexOf("--version") + 1 < args.size()) {
                    String version = args.get(args.indexOf("--version") + 1);

                    if (version.contains("--")) {
                        exitWithErrorMessage("Missing version number: \"--version [VERSION]\"");
                    }

                    JarVersionManager versionManager = null;

                    try {
                        versionManager = JarVersionManager.getManager(builder.getConfig().getType());
                    } catch (IOException e) {
                        ArgParser.exitWithErrorMessage("\nFailed fetching available versions from API: " + e);
                    }

                    if (versionManager.getAllVersions().stream().anyMatch(jarVersion -> jarVersion.getVersion().equals(version.toLowerCase()))) {
                        builder.setVersion(versionManager.getJarVersionByVersionName(version));
                    } else {
                        exitWithErrorMessage("This version is not available, run \"mserv new\" to enter the guided configuration mode.");
                    }
                } else {
                    exitWithErrorMessage("Missing version number: \"--version [VERSION]\"");
                }
            } else {
                try {
                    builder.setVersion(JarVersionManager.getManager(builder.getConfig().getType()).getNewestVersion());
                } catch (IOException e) {
                    exitWithErrorMessage("\nFailed fetching available versions from API: " + e);
                }
            }

            if (args.contains("--ram")) {
                if (args.indexOf("--ram") + 1 < args.size()) {
                    String ram = args.get(args.indexOf("--ram") + 1);

                    if (ram.contains("--")) {
                        exitWithErrorMessage("Missing RAM amount in MB: \"--ram [RAM_AMOUNT]\"");
                    }

                    try {
                        long ramLong = Long.parseLong(ram);

                        long ramSize = ((OperatingSystemMXBean) ManagementFactory
                                .getOperatingSystemMXBean()).getTotalMemorySize();

                        if (ramLong < 1) {
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
            } else {
                builder.setRAM(2048);
            }

            Interface.getInterface().runConfiguration(builder.build());
        } else {
            Interface.getInterface().configureInterface();
        }
    }

    private void parseDeleteArgument(ArrayList<String> args) {
        if (args.size() > 1) {
            String instanceName = args.get(1);

            if (ServerInstanceManager.getInstance().instanceNameExists(instanceName)) {
                ServerConfig instance = ServerInstanceManager.getInstance().getInstanceByName(instanceName);
                if (args.contains("-y") && !instanceName.equalsIgnoreCase("-y")) {
                    try {
                        ServerInstanceManager.getInstance().deleteInstance(instance);

                        System.out.println("\nSuccessfully deleted \"" + instance.getConfigName() + "\".\n");
                    } catch (IOException e) {
                        ArgParser.exitWithErrorMessage("Failed to delete instance: " + e);
                    }
                } else {
                    Interface.getInterface().deleteInterface(instance);
                }


            } else {
                exitWithErrorMessage("No server instance found with name \"" + instanceName + "\".\nRun \"mserv list\" to see all instances.");
            }

        } else {
            exitWithErrorMessage("Missing server instance name: delete [INSTANCE_NAME]");
        }
    }

    private void parseListArgument(ArrayList<String> args) {
        Interface.getInterface().runList(args.contains("-d"));
    }

    private void parseStartArgument(ArrayList<String> args) {
        if (args.size() > 1) {
            String instanceName = args.get(1);

            if (ServerInstanceManager.getInstance().instanceNameExists(instanceName)) {
                ServerConfig instance = ServerInstanceManager.getInstance().getInstanceByName(instanceName);

                try {
                    InstanceRunner.runInstance(instance);
                } catch (IOException e) {
                    exitWithErrorMessage("Failed to start instance: " + e);
                }

            } else {
                exitWithErrorMessage("No server instance found with name \"" + instanceName + "\".\nRun \"mserv list\" to see all instances.");
            }

        } else {
            exitWithErrorMessage("Missing server instance name: start [INSTANCE_NAME]");
        }
    }

    public static void exitWithErrorMessage(String msg) {
        System.err.println();
        System.err.println(msg);
        System.err.println();
        System.exit(1);
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
