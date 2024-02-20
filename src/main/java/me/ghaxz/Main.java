package me.ghaxz;

import me.ghaxz.cli.ArgParser;
import me.ghaxz.cli.Interface;
import me.ghaxz.model.store.ConfigFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Interface.getInterface().configureInterface();

        new ArgParser().parseArgs(new ArrayList<>(List.of(args)));
    }
}