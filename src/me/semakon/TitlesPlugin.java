package me.semakon;

import me.semakon.commandExecutors.EditTitleExecutor;
import me.semakon.commandExecutors.GetExecutor;
import me.semakon.commandExecutors.RequestExecutor;
import me.semakon.commandExecutors.SetExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author:  Martijn
 * Date:    8-2-2016
 */
public class TitlesPlugin extends JavaPlugin {

    public Permission editTitlesPerm = new Permission("titles.editTitles");
    public Permission handelRequestsPerm = new Permission("titles.handleRequests");
    public Permission editUserTitlesPerm = new Permission("titles.editUserTitles");
    public Permission makeRequestsPerm = new Permission("titles.makeRequests");
    public Permission setTitlePerm = new Permission("titles.setTitle");

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
        if (cmd.getName().equals(Utils.TITLES_COMMAND) && args.length >= 1) {
            String type = args[0];
            switch (type) {

                case "set":
                    if (sender.hasPermission(setTitlePerm)) {
                        if (!setExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:");

                            sender.sendMessage("/titles set title [<title>]");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "get":
                    if (!getExecutor.execute(sender, args)) {
                        sender.sendMessage("/titles get titles\n" + "/titles get titles <category>\n" +
                                "/titles get title <title> description\n" + "/titles get title <title> category\n" +
                                "/titles get request [user] [<user>]\n" + "/titles get requests\n" + "/titles get categories");
                    }
                    return true;

                case "create":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (!editTitleExecutor.execute(sender, args)) {
                            sender.sendMessage("/titles create title <name> <description> <category>\n" + "/titles create category <name>");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "remove":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (!editTitleExecutor.execute(sender, args)) {
                            sender.sendMessage("/titles remove title <title>\n" + "/titles remove category <category>");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "edit":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (!editTitleExecutor.execute(sender, args)) {
                            sender.sendMessage("/titles edit <title> description <description>\n" + "/titles edit <title> category <category>");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "rename":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (!editTitleExecutor.execute(sender, args)) {
                            sender.sendMessage("/titles rename title <title> <newName>\n" + "/titles rename category <category> <newName>");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "user":
                    if (sender.hasPermission(editUserTitlesPerm)) {
                        if (!setExecutor.execute(sender, args)) {
                            sender.sendMessage("/titles user <user> [add:remove:set] title <title>\n");
                        }
                    } else sender.sendMessage(Utils.msgPrefix(sender, "You don't have permission to do that."));
                    return true;

                case "request":
                    // permissions handled in execute(...) method.
                    if (!requestExecutor.execute(sender, args)) {
                        sender.sendMessage("/titles request approve <user>\n" + "/titles request deny <user\n" +
                                "/titles request submit <title> [<comments>]\n" + "/titles request retract");
                    }
                    return true;
            }
        }
        sender.sendMessage(Utils.msgPrefix(sender, "Unknown command."));
        return true;
    }

}
