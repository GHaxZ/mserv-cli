package me.ghaxz.interfaces;

import java.util.Scanner;

public class Input {
    private static final Scanner sc = new Scanner(System.in);

    public static String readString() {
        return sc.nextLine();
    }

    public static String sanatizeString(String str) {
        return str.trim();
    }
}
