package me.ghaxz;

import me.ghaxz.interfaces.ArgParser;
import me.ghaxz.interfaces.Interface;
import me.ghaxz.supplier.JarInfoFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Interface.getInterface().configureInterface();
    }
}