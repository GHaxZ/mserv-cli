package me.ghaxz.interfaces;

import me.ghaxz.store.ConfigFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Interface {
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
        System.out.println("First time startup, entering configuration mode.");

        while(true) {
            System.out.print("\nPlease specify the path, where you want to save your server instances by default: ");

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
    // No script for startup required, all managed in program (run "java -jar NAME RAM_SIZE" from tool)
    public void configureInterface() {

    }
}
