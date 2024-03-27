package me.ghaxz.cli.commands;

import me.ghaxz.cli.CommandLineParser;
import me.ghaxz.cli.Interface;
import me.ghaxz.store.*;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Command(name = "new", description = "Create a new server instance. Run without any arguments to enter a guided setup.")
public class NewCommand implements Runnable {
    @Spec
    private CommandSpec spec;

    @Parameters(paramLabel = "NAME", description = "A name for the server instance.")
    private String name;

    @Option(names = {"--dir"}, description = "Specify where to save the server instance. Optional if a default directory is set.")
    private String dir;

    @Option(names = {"--software"}, description = "Specify which server software to use. Default is vanilla.")
    private String software;

    @Option(names = {"--version"}, description = "Specify the server version. Default is newest available.")
    private String version;

    @Option(names = {"--ram"}, description = "Specify how much RAM to allocate to the server.")
    private long ram;

    @Override
    public void run() {
        // If no name is specified, enter the guided configuration
        if(name == null) {
            Interface.getInterface().configureInterface();

            return;
        }

        // Declare all required objects
        ServerConfigBuilder builder = new ServerConfigBuilder();
        ServerInstanceManager serverInstanceManager = ServerInstanceManager.getInstance();
        JarTypeManager jarTypeManager = null;
        ConfigFile configFile = null;

        // Initialize all required objects, exit if exception
        try {
            jarTypeManager = JarTypeManager.getInstance();
        } catch (IOException e) {
            CommandLineParser.exitWithErrorMessage("\nFailed fetching available jar types from API: " + e);
        }

        try {
            configFile = ConfigFile.getConfig();
        } catch (IOException e) {
            CommandLineParser.exitWithErrorMessage("\nFailed opening config file: " + e);
        }

        // Check and set instance name
        if (serverInstanceManager.instanceNameExists(name)) {
            throw new ParameterException(spec.commandLine(),
                    "The instance name \"" + name + "\" is already in use.");
        } else {
            builder.setConfigName(name);
        }


        // Check and set default directory
        if (dir == null) {
            String defaultDir = configFile.getDefaultDirectory();

            if (defaultDir == null) {
                throw new ParameterException(spec.commandLine(),
                        "There is no default directory configured. Specify one using \"--dir\" or run \"--setup\" to configure a default.");
            } else {
                builder.setStorageDirectory(defaultDir);
            }
        } else {
            if (Files.isDirectory(Path.of(dir))) {
                builder.setStorageDirectory(dir);
            } else {
                throw new ParameterException(spec.commandLine(),
                        "The specified directory is not valid.");
            }
        }

        // Check and set server software
        if(software != null) {
            JarType jarType = jarTypeManager.getJarTypeByName(software);

            if (jarType == null){
                throw new ParameterException(spec.commandLine(),
                        "Couldn't find a server software called \"" + software + "\". Use the guided setup to see available software.");
            } else{
                builder.setType(jarType);
            }
        } else  {
            jarTypeManager.getJarTypeByName("vanilla");
        }

        // Check and set server version
        JarVersionManager jarVersionManager = null;

        try {
            jarVersionManager = JarVersionManager.getManager(builder.getConfig().getType());
        } catch (IOException e) {
            CommandLineParser.exitWithErrorMessage("\nFailed fetching available jar versions from API: " + e);
        }

        if(version != null) {
            JarVersion jarVersion = jarVersionManager.getJarVersionByVersionName(version);

            if (jarVersion == null) {
                throw new ParameterException(spec.commandLine(),
                        "Couldn't find version \"" + version + "\" for this software. Use the guided setup to see available versions.");
            } else {
                builder.setVersion(jarVersion);
            }
        } else {
            builder.setVersion(jarVersionManager.getNewestVersion());
        }

        // Check and set server RAM
        if(ram != 0) {
            if(ram > 0) {
                builder.setRAM(ram);
            } else {
                throw new ParameterException(spec.commandLine(),
                        "Specified RAM needs to be larger than 0MB.");
            }
        } else {
            builder.setRAM(2048);
        }

        Interface.getInterface().runConfiguration(builder.build());
    }
}
