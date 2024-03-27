package me.ghaxz.cli;

import me.ghaxz.cli.commands.NewCommand;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(name = "mserv",
        mixinStandardHelpOptions = true,
        version = "mserv 1.0",
        description = "A simple Minecraft server instance management tool.",
        subcommands = {NewCommand.class})

public class CommandLineParser implements Runnable {
    @Option(names = {"-s", "--setup"}, description = "Run the setup process to configure the tool.")
    private boolean setup;

    //TODO: add missing commands

    @Override
    public void run() {
        if(setup) {
            Interface.getInterface().setupInterface();
        }
    }

    public static void exitWithErrorMessage(String msg) {
        System.err.println();
        System.err.println(msg);
        System.err.println();
        System.exit(1);
    }


}
