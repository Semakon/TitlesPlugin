package me.semakon;

import me.semakon.commandExecutors.EditTitleCommands;
import me.semakon.commandExecutors.RequestCommands;
import me.semakon.commandExecutors.UserCommands;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author:  Martijn
 * Date:    8-2-2016
 */
public class TitlesPlugin extends JavaPlugin {

    public Permission pm = new Permission("editTitles.allowed");
    public Permission pm2 = new Permission("handleRequests.allowed");

    /**
     * Called when the plugin is enabled. Creates new instances of CommandExecutors for the corresponding commands.
     */
    @Override
    public void onEnable() {
        UserCommands uc = new UserCommands(this);
        EditTitleCommands etc = new EditTitleCommands(this);
        RequestCommands rc = new RequestCommands(this);

        this.getCommand(Utils.GET_TITLES).setExecutor(uc);
        this.getCommand(Utils.GET_DESCRIPTION).setExecutor(uc);
        this.getCommand(Utils.UNLOCK_TITLE).setExecutor(uc);
        this.getCommand(Utils.CHANGE_TITLE).setExecutor(uc);
        this.getCommand(Utils.RETRACT_TITLE_REQUEST).setExecutor(uc);

        this.getCommand(Utils.CREATE_NEW_TITLE).setExecutor(etc);
        this.getCommand(Utils.REMOVE_TITLE).setExecutor(etc);
        this.getCommand(Utils.EDIT_DESCRIPTION).setExecutor(etc);

        this.getCommand(Utils.VIEW_REQUESTS).setExecutor(rc);
    }

    /**
     * Called when the plugin is disabled. Saves the config.
     */
    @Override
    public void onDisable() {
        saveConfig();
    }

}
