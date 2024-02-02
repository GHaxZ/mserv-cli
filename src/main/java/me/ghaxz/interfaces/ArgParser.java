package me.ghaxz.interfaces;

import me.ghaxz.store.ConfigFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ArgParser {
    public static void parseArgs(ArrayList<String> args) {
        if(!ConfigFile.exists()) {
            Interface.getInterface().setupInterface();
        }

        if(args.contains("--setup")) {
            Interface.getInterface().setupInterface();

            return;
        }

        if(args.contains("--dir")) {
            if(args.indexOf("--dir") + 1 < args.size()) {
                String dir = args.get(args.indexOf("--dir") + 1);

                if(Files.isDirectory(Paths.get(dir))) {

                } else {
                    exitWithErrorMessage("\nNot a valid directory: \"" + dir + "\"");
                }
            } else {
                exitWithErrorMessage("\nMissing directory: \"--dir [DIRECTORY]\"");
            }
        }
    }

    public static void exitWithErrorMessage(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
}
