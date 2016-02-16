package me.semakon;

import me.semakon.commandExecutors.EditTitleExecutor;
import me.semakon.commandExecutors.GetExecutor;
import me.semakon.commandExecutors.RequestExecutor;
import me.semakon.commandExecutors.SetExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author:  Martijn
 * Date:    8-2-2016
 */
public class TitlesPlugin extends JavaPlugin {

    public Permission pm = new Permission("editTitles.allowed");
    public Permission pm2 = new Permission("handleRequests.allowed");
    public Permission pm3 = new Permission("editUserTitles.allowed");
    public Permission pm4 = new Permission("makeRequests.allowed");
    public Permission pm5 = new Permission("setTitle.allowed");

    private GetExecutor getExecutor;
    private SetExecutor setExecutor;
    private RequestExecutor requestExecutor;
    private EditTitleExecutor editTitleExecutor;

    public void registerListeners() {
        org.bukkit.plugin.PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    /**
     * Called when the plugin is enabled. Registers Listeners.
     */
    @Override
    public void onEnable() {
        registerListeners();
        getExecutor = new GetExecutor(this);
        setExecutor = new SetExecutor(this);
        requestExecutor = new RequestExecutor(this);
        editTitleExecutor = new EditTitleExecutor(this);
    }

    /**
     * Called when the plugin is disabled. Saves the config.
     */
    @Override
    public void onDisable() {
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals(Utils.TITLES_COMMAND) && args.length >= 2) {
            String type = args[0];
            switch (type) {
                case "set":
                    //TODO: implement
                    break;
                case "get":
                    sender.sendMessage(String.valueOf(getExecutor.execute(sender, args)));
                    return true;
                case "create":
                    //TODO: implement
                    break;
                case "remove":
                    //TODO: implement
                    break;
                case "edit":
                    //TODO: implement
                    break;
                case "rename":
                    //TODO: implement
                    break;
                case "user":
                    //TODO: implement
                    break;
                case "request":
                    //TODO: implement
                    break;
            }
        }
        return false;
    }

}
