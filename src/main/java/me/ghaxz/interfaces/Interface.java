package me.ghaxz.interfaces;

import me.ghaxz.store.ConfigFile;
import me.ghaxz.store.JarType;
import me.ghaxz.store.JarTypeManager;
import me.ghaxz.store.ServerConfigBuilder;

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
        System.out.println("Initializing ...");
        ServerConfigBuilder builder = new ServerConfigBuilder();
        ConfigFile configFile = ConfigFile.getConfig();

        System.out.println("\n-- Configure the server --");


        while(true) {
            System.out.print("\nGive your server Instance a name: ");

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
                System.out.println("\nNo default directory configured in config file, directory is required.");

                System.out.print("\nPlease specify the path, where you want to save your server instance: ");

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
                System.out.print("\nPlease specify the path, where you want to save your server instance" +
                        ", leave blank for default (" + configFile.getDefaultDirectory() + "): ");

                String dir = Input.sanatizeString(Input.readString());

                if(dir.isBlank()) {
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

            for(JarType jar : JarTypeManager.getInstance().getJars()) {
                if(currectCategory.equals(jar.getCategory())) {
                    System.out.print(", " + Input.capitalizeString(jar.getName()));
                } else {
                    currectCategory = jar.getCategory();
                    System.out.println("\n\n[" + Input.capitalizeString(currectCategory)  + "]");


                    System.out.print(Input.capitalizeString(jar.getName()));
                }
            }

            System.out.print("\n\nPlease specify the server software, leave blank for default (vanilla): ");

            String software = Input.sanatizeString(Input.readString());

            if(software.isBlank()) {
                break;
            } else {
                JarType type = JarTypeManager.getInstance().getJarTypeByName(software.toLowerCase());

                if(type != null) {
                    builder.setType(type);
                    break;
                } else {
                    System.out.println("\nThis is not an available software.");
                }
            }
        }

        // todo version selection, ram specification
    }
}
