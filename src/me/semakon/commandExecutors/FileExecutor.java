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
                //TODO: add different reload options: 'reloadAllData userdata', 'reloadAllData settings', 'reloadAllData titles'
                dataContainer.reloadAllData();
                Utils.sendMsg(sender, "Reloaded data from config file.");
                return true;

            case "save":
                dataContainer.saveStorage();
                return true;

            case "autosave":
                Settings.setAutoSave(!Settings.isAutoSaveOn());
                return true;

            case "backup":

                return true;
        }
        return false;
    }

}
