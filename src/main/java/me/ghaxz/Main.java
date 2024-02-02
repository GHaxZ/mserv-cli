package me.ghaxz;

import me.ghaxz.interfaces.ArgParser;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArgParser.parseArgs(new ArrayList<>(List.of(args)));
    }
}