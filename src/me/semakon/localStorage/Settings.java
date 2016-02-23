package me.semakon.localStorage;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Settings {

    private static boolean donatorSuffixes;

    public static boolean getDonatorSuffixes() {
        return donatorSuffixes;
    }

    public static void setDonatorSuffixes(boolean ds) {
        donatorSuffixes = ds;
    }

}
