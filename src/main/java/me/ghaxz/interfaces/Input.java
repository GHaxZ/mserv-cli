package me.ghaxz.interfaces;

import java.util.Optional;
import java.util.Scanner;

/*
Helper class for user input
 */
public class Input {
    private static final Scanner sc = new Scanner(System.in);

    public static String readString() {
        return sc.nextLine();
    }

    public static String sanatizeString(String str) {
        return str.trim();
    }

    public static String capitalizeString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
