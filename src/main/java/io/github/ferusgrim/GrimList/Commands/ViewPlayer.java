/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;

import io.github.ferusgrim.GrimList.utils.AsyncThenSyncOperation;
import io.github.ferusgrim.GrimList.utils.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ViewPlayer {
    private GrimList plugin;

    public ViewPlayer(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name) {
        switch(plugin.focusOn){
            case "file":
                String uuid = plugin.filem.getUUID(name);
                if(uuid.isEmpty()){
                    runOperation(sender, name);
                }else{
                    outputText(sender, uuid);
                }
                break;
        }
        return true;
    }

    public void outputText(CommandSender sender, String uuid){
        List<String> viewIds = null;
        List<String> viewPUs = null;
        List<String> viewPIs = null;
        switch(plugin.focusOn){
            case "file":
                viewIds = new ArrayList<String>(plugin.filem.setupViewPlayers(uuid));
                viewPUs = new ArrayList<String>(plugin.filem.setupPreviousUsernames(uuid));
                viewPIs = new ArrayList<String>(plugin.filem.setupPreviousAddresses(uuid));
                break;
        }
        if(viewIds == null || viewIds.size() < 5 || viewPUs == null || viewPIs == null){
            plugin.log("SEVERE", "ViewPlayer arrays returned null unexpectedly.");
            if(sender instanceof Player){
                sender.sendMessage(plugin.mStart + "Command returned error. Check logs!");
            }
            return;
        }
        if(!(sender instanceof Player)){
            sender.sendMessage("*-- " + viewIds.get(0) + " --*");
            sender.sendMessage("UUID: " + uuid);
            sender.sendMessage("Whitelisted: " + viewIds.get(1));
            sender.sendMessage("Previous Usernames:");
            for (String viewPU : viewPUs) {
                sender.sendMessage("  - " + viewPU);
            }
            sender.sendMessage("Previous Addresses:");
            for (String viewPI : viewPIs) {
                sender.sendMessage("  - " + viewPI);
            }
            sender.sendMessage("First Login: " + (viewIds.get(2) == null? "Never" : viewIds.get(2)));
            sender.sendMessage("Last Login: " + (viewIds.get(3) == null? "Never" : viewIds.get(3)));
            sender.sendMessage("Logged in: " + viewIds.get(4) + " times");
            sender.sendMessage("*-- " + viewIds.get(0) + " --*");
        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + viewIds.get(0) + " &r&7--&6*"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lUUID: &r&9" + uuid));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lWhitelisted: &r&9" + viewIds.get(1)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Usernames:"));
            for (String viewPU : viewPUs) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&9" + viewPU));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Addresses:"));
            for (String viewPI : viewPIs) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&9" + viewPI));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lFirst Login: &r&9" + (viewIds.get(2) == null? "Never" : viewIds.get(2))));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLast Login: &r&9" + (viewIds.get(3) == null? "Never" : viewIds.get(3))));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLogged in: &r&9" + viewIds.get(4) + " times"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + viewIds.get(0) + " &r&7--&6*"));
        }
        viewPUs.clear();
        viewPIs.clear();
    }

    public void runOperation(CommandSender sender, String name){
        if (sender instanceof Player) {
            sender.sendMessage(plugin.mStart + "Looking up UUID. This can take a moment...");
        } else {
            plugin.log("INFO", "Looking up UUID. This can take a moment...");
        }
        new AsyncThenSyncOperation(plugin, true){
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try{
                    response = new UUIDFetcher(Arrays.asList(name.toLowerCase())).call();
                }catch(Exception e){
                    plugin.log("WARNING", "Exception while running UUIDFetcher!");
                    e.printStackTrace();
                }
            }

            @Override
            protected void execSyncThen() {
                if (response.get(name.toLowerCase()) == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage(plugin.mStart + "UUID Query returned null! Invalid username?");
                    } else {
                        plugin.log("WARNING", "UUID Query returned null! Username might not exist.?");
                    }
                    return;
                }
                String uuid = response.get(name.toLowerCase()).toString();
                switch(plugin.focusOn){
                    case "file":
                        if(plugin.filem.recordExists(uuid)){
                            outputText(sender, uuid);
                            return;
                        }else{
                            if(!plugin.filem.recordExists(uuid) && plugin.getConfig().getBoolean("SaveQueries")){
                                plugin.filem.newPlayerRecord(uuid, name);
                                outputText(sender, uuid);
                                return;
                            }
                        }
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Player record not found!");
                        }else{
                            plugin.log("WARNING", "Player record not found!");
                        }
                        break;
                }
            }
        };
    }

}
