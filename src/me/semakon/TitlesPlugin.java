package me.semakon;

import me.semakon.commandExecutors.EditTitleExecutor;
import me.semakon.commandExecutors.GetExecutor;
import me.semakon.commandExecutors.RequestExecutor;
import me.semakon.commandExecutors.SetExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author:  Martijn
 * Date:    8-2-2016
 */
public class TitlesPlugin extends JavaPlugin {

    public Permission editTitlesPerm = new Permission("titles.editTitles");
    public Permission handleRequestsPerm = new Permission("titles.handleRequests");
    public Permission editUserTitlesPerm = new Permission("titles.editUserTitles");
    public Permission makeRequestsPerm = new Permission("titles.makeRequests");
    public Permission setTitlePerm = new Permission("titles.setTitle");

    public Permission donatorSuffix1 = new Permission("titles.suffix.donator1");
    public Permission donatorSuffix2 = new Permission("titles.suffix.donator2");
    public Permission donatorSuffix3 = new Permission("titles.suffix.donator3");

    private GetExecutor getExecutor;
    private SetExecutor setExecutor;
    private RequestExecutor requestExecutor;
    private EditTitleExecutor editTitleExecutor;

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new InventoryListener(this), this);
    }

    /**
     * Called when the plugin is enabled. Registers Listeners.
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        registerListeners();
        Utils.initColors();

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
        args = Utils.inQuotes(args);
        if (cmd.getName().equals(Utils.TITLES_COMMAND) && args.length >= 1) {
            String type = args[0];
            switch (type) {

                case "set":
                    if (sender.hasPermission(setTitlePerm)) {
                        if (args.length == 1 || !setExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles set title [<title>]"};
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "get":
                    if (args.length == 1 || !getExecutor.execute(sender, args)) {
                        Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                        String[] commands = {"/titles get titles", "/titles get titles category <category>",
                                "/titles get title <title> description", "/titles get title <title> category",
                                "/titles get request [user] [<user>]", "/titles get requests", "/titles get categories"};
                        Utils.sendArray(sender, commands);
                    }
                    return true;

                case "create":
                    if (sender.hasPermission(editTitlesPerm)) {
                        System.out.println(sender);
                        System.out.println(editTitleExecutor);
                        System.out.println(args);
                        if (args.length == 1 || !editTitleExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles create title <name> <description> <category>"}; // "/titles create category <name>"
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "remove":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (args.length == 1 || !editTitleExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles remove title <title>", "/titles remove category <category>"};
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "edit":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (args.length == 1 || !editTitleExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles edit title <title> description <description>", "/titles edit title <title> category <category>"};
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "rename":
                    if (sender.hasPermission(editTitlesPerm)) {
                        if (args.length == 1 || !editTitleExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles rename title <title> <newName>", "/titles rename category <category> <newName>"};
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "user":
                    if (sender.hasPermission(editUserTitlesPerm)) {
                        if (args.length == 1 || !setExecutor.execute(sender, args)) {
                            Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                            String[] commands = {"/titles user <user> [add:remove:set] title <title>"};
                            Utils.sendArray(sender, commands);
                        }
                    } else Utils.sendError(sender, "You don't have permission to do that.");
                    return true;

                case "request":
                    // permissions handled in execute(...) method.
                    if (args.length == 1 || !requestExecutor.execute(sender, args)) {
                        Utils.sendMsg(sender, "Did you mean:", ChatColor.GOLD);
                        String[] commands = {"/titles request approve user <user>", "/titles request deny user <user>", "/titles request tp user <user>",
                                "/titles request submit title <title>", "/titles request retract"};
                        Utils.sendArray(sender, commands);
                    }
                    return true;
            }
        } else if (cmd.getName().equals(Utils.TITLES_COMMAND) && args.length == 0) {
            if (sender instanceof Player) {
                Inventory inv = InventoryListener.constructInventory(this, (Player) sender, null);
                if (inv != null) ((Player) sender).openInventory(inv);
                return true;
            } else Utils.sendError(sender, "Only players can access this command.");
        }
        Utils.sendError(sender, "Unknown command.");
        return true;
    }

}
