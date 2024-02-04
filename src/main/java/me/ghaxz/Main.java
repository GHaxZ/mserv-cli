package me.ghaxz;

import me.ghaxz.interfaces.ArgParser;
import me.ghaxz.interfaces.Interface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Interface.getInterface().configureInterface();

        // ArgParser.parseArgs(new ArrayList<>(List.of(args)));
    }
}