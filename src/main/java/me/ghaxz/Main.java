package me.ghaxz;

import me.ghaxz.cli.ArgParser;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        new ArgParser().parseArgs(new ArrayList<>(List.of(args)));
    }
}