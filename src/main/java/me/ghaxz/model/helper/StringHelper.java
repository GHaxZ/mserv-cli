package me.ghaxz.model.helper;

public class StringHelper {
    public static String capitalizeString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}