package me.ghaxz.interfaces;

import me.ghaxz.store.ConfigFile;
import me.ghaxz.store.ServerConfigBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/*
Parses command line arguments and executes corresponding code
 */

public class ArgParser {
    public static void parseArgs(ArrayList<String> args) {
        if (!ConfigFile.exists()) {
            System.out.println("First time startup, entering configuration mode.");

            Interface.getInterface().setupInterface();
        }

        if (args.contains("--setup")) {
            Interface.getInterface().setupInterface();

            return;
        }

        if (args.isEmpty()) {
            // todo implement help text
            return;
        }

        switch (args.get(1)) {
            case "new" -> {


                if (args.contains("--dir")) {
                    if (args.indexOf("--dir") + 1 < args.size()) {
                        String dir = args.get(args.indexOf("--dir") + 1);

                        if (Files.isDirectory(Paths.get(dir))) {
                            // todo add dir to config builder
                        } else {
                            exitWithErrorMessage("\nNot a valid directory: \"" + dir + "\"");
                        }
                    } else {
                        exitWithErrorMessage("\nMissing directory: \"--dir [DIRECTORY]\"");
                    }
                } else {
                    String defaultDir = ConfigFile.getConfig().getDefaultDirectory();

                    if (defaultDir == null) {
                        ArgParser.exitWithErrorMessage("Couldn't read default directory from config file.\n" +
                                "Use the --dir argument to specify one manually, or configure the default directory.");
                    }

                    if (defaultDir != null && defaultDir.isBlank()) {
                        ArgParser.exitWithErrorMessage("Couldn't read default directory from config file.\n" +
                                "Use the --dir argument to specify one manually, or configure the default directory.");
                    }

                    // todo add dir to config builder
                }


                // todo continue new instance command parsing
            }
        }
    }

    public static void exitWithErrorMessage(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
}
