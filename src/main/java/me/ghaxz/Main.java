package me.ghaxz;

import me.ghaxz.cli.ArgParser;
import me.ghaxz.cli.CommandLineParser;
import me.ghaxz.cli.Interface;
import me.ghaxz.store.ConfigFile;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // TODO: figure out how to execute command parser
        new CommandLine(new CommandLineParser()).execute(args);
    }
}