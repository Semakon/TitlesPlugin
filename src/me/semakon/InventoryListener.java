package me.semakon;

import me.semakon.Handlers.GetCommands;
import me.semakon.Handlers.RequestCommands;
import me.semakon.Handlers.SetCommands;
import me.semakon.localStorage.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:  Martijn
 * Date:    21-2-2016
 */
public class InventoryListener implements Listener {

    private TitlesPlugin plugin;
    private DataContainer dataContainer;

    public InventoryListener(TitlesPlugin plugin) {
        this.plugin = plugin;
        this.dataContainer = plugin.getDataContainer();
    }

    @SuppressWarnings("deprecation")
    public static Inventory constructInventory(TitlesPlugin plugin, Player player, String cat) {
        DataContainer dataContainer = plugin.getDataContainer();
        Inventory inv = null;
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("Titles");
        // if there are titles, construct category inventory
        if (config != null && !config.getKeys(false).isEmpty()) {
            List<String> categories = GetCommands.getCategories(dataContainer);
            List<ItemStack> clickables = new ArrayList<>();

            // get inventory with categories.
            if (cat == null) {
                for (String category : categories) {
                    ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData());
                    ItemMeta meta = is.getItemMeta();
                    meta.setDisplayName(category);
                    is.setItemMeta(meta);
                    clickables.add(is);
                }
                int size = ((clickables.size() / 9) + 1) * 9;
                inv = Bukkit.createInventory(null, size, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Title categories");

            // get inventory with titles from category.
            } else {
                List<String> titles = GetCommands.getTitlesFromCategory(dataContainer, cat);
                List<String> ownedTitles = GetCommands.getMapping(dataContainer, player);

                for (String title : titles) {
                    ItemStack is;
                    String name = config.getString(title + ".Name");
                    String description = config.getString(title + ".Description");
                    String status;
                    String uuid = player.getUniqueId().toString();

                    if (ownedTitles.contains(title.toLowerCase())) {
                        ConfigurationSection mapConfig = plugin.getConfig().getConfigurationSection(Utils.MAPPINGS + uuid);
                        if (mapConfig != null && mapConfig.getString("Current") != null
                                && mapConfig.getString("Current").equalsIgnoreCase(title)) {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
                            status = "Current";
                        } else {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
                            status = "Owned";
                        }
                    } else {
                        ConfigurationSection reqConfig = plugin.getConfig().getConfigurationSection(Utils.REQUESTS + uuid);
                        if (reqConfig != null && reqConfig.getString("Title") != null
                                && reqConfig.getString("Title").equalsIgnoreCase(title)) {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.ORANGE.getData());
                            status = "Pending";
                        } else {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
                            status = "Not Owned";
                        }
                    }

                    ItemMeta meta = is.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(Arrays.asList("Description: " + description, "Category: " + cat, "Status: " + status));
                    is.setItemMeta(meta);
                    clickables.add(is);
                }
                int size = ((clickables.size() / 9) + 1) * 9;
                inv = Bukkit.createInventory(null, size, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Titles in category");
            }

            for (int i = 0; i < clickables.size(); i++) inv.setItem(i, clickables.get(i));
            if (cat != null) {
                ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData());
                ItemMeta meta = is.getItemMeta();
                meta.setDisplayName("Back");
                is.setItemMeta(meta);
                inv.setItem(inv.getSize() - 1, is);
            }

        } else Utils.sendError(player, "There are no titles yet.");
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Title categories")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                return;
            }

            if (e.getCurrentItem().getData() instanceof Wool) {
                player.openInventory(constructInventory(plugin, player, e.getCurrentItem().getItemMeta().getDisplayName()));
            }
        } else if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Titles in category")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                return;
            }

            // back button
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Back")) {
                player.openInventory(constructInventory(plugin, player, null));
                return;
            }

            String category = null;
            String[] split = e.getCurrentItem().getItemMeta().getLore().get(1).split(" ");
            if (split.length == 2) category = split[1];

            if (e.getCurrentItem().getData() instanceof Wool) {
                DyeColor color = ((Wool)e.getCurrentItem().getData()).getColor();
                String title = e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase();
                Inventory inv = e.getInventory();
                ItemMeta im = e.getCurrentItem().getItemMeta();
                int slot = e.getSlot();

                switch(color) {
                    case RED:
                        // Request title.
                        if (player.hasPermission(plugin.makeRequestsPerm)) {
                            if (RequestCommands.submitRequest(plugin, player, title)) {
                                Utils.sendMsg(player, "Your request for " + ChatColor.ITALIC + title + ChatColor.RESET + " has been submitted.");
                                handleBlock(inv, im, DyeColor.ORANGE, "Pending", slot);
                            } else Utils.sendError(player, "You already have a pending request or that title doesn't exist.");
                        }
                        break;
                    case LIME:
                        // Disable current title.
                        if (player.hasPermission(plugin.setTitlePerm)) {
                            if (SetCommands.disableTitle(plugin, player)) {
                                Utils.sendMsg(player, "Title disabled.");
                                handleBlock(inv, im, DyeColor.GREEN, "Owned", slot);
                            }
                            else Utils.sendError(player, "Player doesn't have that title active.");
                        }
                        break;
                    case GREEN:
                        // Set title as current.
                        if (player.hasPermission(plugin.setTitlePerm)) {
                            if (SetCommands.setTitle(plugin, player, title)) {
                                Utils.sendMsg(player, String.format("Title set to %s" + title + "%s.", ChatColor.ITALIC, ChatColor.RESET));
                                handleBlock(inv, im, DyeColor.LIME, "Current", slot);
                                if (category == null) player.closeInventory();
                                else player.openInventory(constructInventory(plugin, player, category));

                            } else Utils.sendError(player, "Player doesn't own that title or it doesn't exist.");
                        }
                        break;
                    case ORANGE:
                        // Retract request.
                        if (player.hasPermission(plugin.makeRequestsPerm)) {
                            if (RequestCommands.retractRequest(plugin, player)) {
                                Utils.sendMsg(player, "Your request has successfully been retracted.");
                                handleBlock(inv, im, DyeColor.RED, "Not Owned", slot);
                            }
                            else Utils.sendError(player, "You don't have a pending request.");
                        }
                        break;
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void handleBlock(Inventory inv, ItemMeta im, DyeColor color, String status, int slot) {
        ItemStack is = new ItemStack(Material.WOOL, 1, color.getData());
        
        // change status
        List<String> lore = im.getLore();
        lore.set(2, "Status: " + status);
        im.setLore(lore);
        is.setItemMeta(im);
        
        // put in inventory
        inv.setItem(slot, is);
    }

}
