package me.semakon;

import me.semakon.Handlers.GetCommands;
import me.semakon.Handlers.RequestCommands;
import me.semakon.Handlers.SetCommands;
import me.semakon.localStorage.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
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

    public InventoryListener(TitlesPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public static Inventory constructInventory(TitlesPlugin plugin, Player player, String cat, boolean owned) {
        DataContainer dataContainer = plugin.getDataContainer();
        Inventory inv = null;
        // if there are titles, construct category inventory
        if (!dataContainer.getTitles().isEmpty()) {
            List<String> categories = GetCommands.getCategories(dataContainer);
            List<ItemStack> clickables = new ArrayList<>();

            // get inventory with categories.
            if (cat == null) {

                if (owned) {
                    List<Title> unlocked = dataContainer.getOwnedTitles(player);
                    for (Title title : unlocked) {
                        ItemStack is;
                        String name = title.getName();
                        String description = title.getDescription();
                        String status;

                        if (dataContainer.getCurrentTitle(player) != null && dataContainer.getCurrentTitle(player).equals(title)) {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
                            status = "Current";
                        } else {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
                            status = "Unlocked";
                        }
                        ItemMeta meta = is.getItemMeta();
                        meta.setDisplayName(name);
                        meta.setLore(Arrays.asList("Description: " + description, "Category: " + title.getCategory().getName(), "Status: " + status));
                        is.setItemMeta(meta);
                        clickables.add(is);
                    }

                    int size = ((clickables.size() / 9) + 1) * 9;
                    inv = Bukkit.createInventory(null, size, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Unlocked Titles");

                    for (int i = 0; i < clickables.size(); i++) inv.setItem(i, clickables.get(i));

                    // back button
                    ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData());
                    ItemMeta meta = is.getItemMeta();
                    meta.setDisplayName("Back");
                    is.setItemMeta(meta);
                    inv.setItem(inv.getSize() - 1, is);
                    return inv;
                }

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
                Category category = dataContainer.getCategory(cat);
                if (category == null) return null;
                List<Title> titles = dataContainer.getTitlesFromCategory(category);
                List<Title> ownedTitles = dataContainer.getOwnedTitles(player);

                for (Title title : titles) {
                    ItemStack is;
                    String name = title.getName();
                    String description = title.getDescription();
                    String status;

                    if (ownedTitles.contains(title)) {
                        if (dataContainer.getCurrentTitle(player) != null && dataContainer.getCurrentTitle(player).equals(title)) {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData());
                            status = "Current";
                        } else {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
                            status = "Unlocked";
                        }
                    } else {
                        Request request = dataContainer.getRequest(player);
                        if (request != null && request.getTitle().equals(title) && request.getStatus() == RequestStatus.pending) {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.ORANGE.getData());
                            status = "Pending";
                        } else {
                            is = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
                            status = "Locked";
                        }
                    }

                    ItemMeta meta = is.getItemMeta();
                    meta.setDisplayName(name);
                    meta.setLore(Arrays.asList("Description: " + description, "Category: " + category.getName(), "Status: " + status));
                    is.setItemMeta(meta);
                    clickables.add(is);
                }
                int size = ((clickables.size() / 9) + 1) * 9;
                inv = Bukkit.createInventory(null, size, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Titles in category");
            }

            for (int i = 0; i < clickables.size(); i++) inv.setItem(i, clickables.get(i));

            // back button
            if (cat != null) {
                ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData());
                ItemMeta meta = is.getItemMeta();
                meta.setDisplayName("Back");
                is.setItemMeta(meta);
                inv.setItem(inv.getSize() - 1, is);

            // player's unlocked titles
            } else {
                ItemStack is = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
                ItemMeta meta = is.getItemMeta();
                meta.setDisplayName("Unlocked");
                is.setItemMeta(meta);
                inv.setItem(inv.getSize() - 1, is);
            }

        } else Utils.sendError(player, "There are no titles yet.");
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        DataContainer dataContainer = plugin.getDataContainer();
        Player player = (Player) e.getWhoClicked();

        String name = ChatColor.stripColor(e.getInventory().getName());

        if (name.equalsIgnoreCase("Title categories")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                return;
            }

            if (e.getCurrentItem().getData() instanceof Wool) {

                // All unlocked titles of player
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Unlocked")) {
                    player.openInventory(constructInventory(plugin, player, null, true));
                    return;
                }

                // open Titles in category
                player.openInventory(constructInventory(plugin, player, Utils.strip(Utils.setColors(e.getCurrentItem().getItemMeta().getDisplayName())), false));

            }
        } else if (name.equalsIgnoreCase("Titles in category") || name.equalsIgnoreCase("Unlocked Titles")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
                player.closeInventory();
                return;
            }

            // back button
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Back")) {
                player.openInventory(constructInventory(plugin, player, null, false));
                return;
            }

            String category = null;
            String[] split = e.getCurrentItem().getItemMeta().getLore().get(1).split(" ");
            if (split.length == 2) category = split[1];

            if (e.getCurrentItem().getData() instanceof Wool) {
                DyeColor color = ((Wool)e.getCurrentItem().getData()).getColor();
                Title title = dataContainer.getTitle(Utils.strip(Utils.setColors(e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase())));
                Inventory inv = e.getInventory();
                ItemMeta im = e.getCurrentItem().getItemMeta();
                int slot = e.getSlot();
                if (name.equalsIgnoreCase("Titles in category")) {
                    switch (color) {
                        case RED:
                            // Request title.
                            if (player.hasPermission(plugin.makeRequestsPerm)) {
                                if (RequestCommands.submitRequest(dataContainer, player, title.getId())) {
                                    Utils.sendMsg(player, "Your request for " + ChatColor.ITALIC + title.getName() + ChatColor.RESET + " has been submitted.");
                                    handleBlock(inv, im, DyeColor.ORANGE, "Pending", slot);
                                } else
                                    Utils.sendError(player, "You already have a pending request or that title doesn't exist.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                        case LIME:
                            // Disable current title.
                            if (player.hasPermission(plugin.setTitlePerm)) {
                                if (SetCommands.disableTitle(dataContainer, player)) {
                                    Utils.sendMsg(player, "Title disabled.");
                                    handleBlock(inv, im, DyeColor.GREEN, "Unlocked", slot);
                                } else Utils.sendError(player, "Player doesn't have that title active.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                        case GREEN:
                            // Set title as current.
                            if (player.hasPermission(plugin.setTitlePerm)) {
                                if (SetCommands.setTitle(dataContainer, player, title.getId())) {
                                    Utils.sendMsg(player, String.format("Title set to %s" + title.getName() + "%s.", ChatColor.ITALIC, ChatColor.RESET));
                                    handleBlock(inv, im, DyeColor.LIME, "Current", slot);
                                    if (category == null) player.closeInventory();
                                    else player.openInventory(constructInventory(plugin, player, category, false));

                                } else Utils.sendError(player, "Player doesn't own that title or it doesn't exist.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                        case ORANGE:
                            // Retract request.
                            if (player.hasPermission(plugin.makeRequestsPerm)) {
                                if (RequestCommands.retractRequest(dataContainer, player)) {
                                    Utils.sendMsg(player, "Your request has successfully been retracted.");
                                    handleBlock(inv, im, DyeColor.RED, "Locked", slot);
                                } else Utils.sendError(player, "You don't have a pending request.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                    }

                } else if (name.equalsIgnoreCase("Unlocked Titles")) {
                    switch (color) { //TODO: fix disabling
                        case LIME:
                            // Disable current title.
                            if (player.hasPermission(plugin.setTitlePerm)) {
                                if (SetCommands.disableTitle(dataContainer, player)) {
                                    Utils.sendMsg(player, "Title disabled.");
                                    handleBlock(inv, im, DyeColor.GREEN, "Unlocked", slot);
                                } else Utils.sendError(player, "Player doesn't have that title active.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                        case GREEN:
                            // Set title as current.
                            if (player.hasPermission(plugin.setTitlePerm)) {
                                if (SetCommands.setTitle(dataContainer, player, title.getId())) {
                                    Utils.sendMsg(player, String.format("Title set to %s" + title.getName() + "%s.", ChatColor.ITALIC, ChatColor.RESET));
                                    handleBlock(inv, im, DyeColor.LIME, "Current", slot);
                                    if (category == null) player.closeInventory();
                                    else player.openInventory(constructInventory(plugin, player, category, false));

                                } else Utils.sendError(player, "Player doesn't own that title or it doesn't exist.");
                            } else Utils.sendError(player, "You don't have permission to do that.");
                            break;
                    }
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
