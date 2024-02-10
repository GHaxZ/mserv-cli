package me.ghaxz;

import me.ghaxz.cli.Interface;

public class Main {
    public static void main(String[] args) {
        Interface.getInterface().configureInterface();

        // new ArgParser().parseArgs(new ArrayList<>(List.of(args)));
    }
}