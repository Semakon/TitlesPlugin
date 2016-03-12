package me.semakon.localStorage;

/**
 * Author:  Martijn
 * Date:    23-2-2016
 */
public class Settings {

    //TODO: make loadSettings in DataContainer

    private static boolean donatorSuffixes;
    private static boolean autoSave;
    private static boolean debugging;

    public static boolean getDonatorSuffixes() {
        return donatorSuffixes;
    }

    public static boolean isAutoSaveOn() {
        return autoSave;
    }

    public static boolean isDebuggingOn() {
        return debugging;
    }

    public static void setDonatorSuffixes(boolean ds) {
        donatorSuffixes = ds;
    }

    public static void setAutoSave(boolean autoSave) {
        Settings.autoSave = autoSave;
    }

    public static void setDebugging(boolean debugging) {
        Settings.debugging = debugging;
    }
}
