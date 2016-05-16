package me.semakon.commandExecutors;

import me.semakon.Utils;
import me.semakon.localStorage.DataContainer;
import me.semakon.localStorage.Settings;
import org.bukkit.command.CommandSender;

/**
 * Author:  Martijn
 * Date:    12-3-2016
 */
public class FileExecutor {

    private DataContainer dataContainer;

    public FileExecutor(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    /**
     * Called when a command is executed.
     * @param sender Sender of the command.
     * @param args Arguments given with the command.
     * @return True if the command has been executed successfully.
     */
    public boolean execute(CommandSender sender, String[] args) {
        switch (args[0].toLowerCase()) {
            case "reload":
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("all")) {
                        dataContainer.reloadAllData();
                        Utils.sendMsg(sender, "Reloaded all data from config file, " +
                                "current data overwritten.");
                    }
                    else if (args[1].equalsIgnoreCase("userdata")) {
                        dataContainer.reloadUserData();
                        Utils.sendMsg(sender, "Reloaded userdata from config file, " +
                                "current userdata overwritten.");
                    }
                    else if (args[1].equalsIgnoreCase("titledata")) {
                        dataContainer.reloadTitleData();
                        Utils.sendMsg(sender, "Reloaded title data from config file, " +
                                "current title data overwritten.");
                    }
                    else if (args[1].equalsIgnoreCase("settings")) {
                        dataContainer.loadSettings();
                        Utils.sendMsg(sender, "Reloaded settings from config file, " +
                                "current settings overwritten.");
                    }
                }
                return true;

            case "save":
                dataContainer.saveStorage();
                Utils.sendMsg(sender, "Saved all current data to config file(s). " +
                        "Config file(s) have been overwritten.");
                return true;

            case "autosave":
                Settings.setAutoSave(!Settings.isAutoSaveOn());
                Utils.sendMsg(sender, "Autosave is now turned " + (Settings.isAutoSaveOn() ? "on." : "off."));
                return true;

            case "backup":
                //TODO: implement
                return true;
        }
        return false;
    }

}
