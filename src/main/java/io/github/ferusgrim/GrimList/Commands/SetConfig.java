/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;

import org.apache.commons.lang.WordUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetConfig {
    private GrimList plugin;

    public SetConfig(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String[] args) {
        switch(args[1].toLowerCase()){
            case "whitelist":
                switch(args[2].toLowerCase()){
                    case "true":
                        if(plugin.getConfig().getBoolean("Whitelist")){
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Whitelist is already enabled!");
                            }else{
                                plugin.log("INFO", "Whitelist is already enabled!");
                            }
                        }else{
                            plugin.getConfig().set("Whitelist", true);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Whitelist has been enabled!");
                            }else{
                                plugin.log("INFO", "Whitelist has been enabled!");
                            }
                        }
                        return true;
                    case "false":
                        if(plugin.getConfig().getBoolean("Whitelist")){
                            plugin.getConfig().set("Whitelist", false);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Whitelist has been disabled!");
                            }else{
                                plugin.log("INFO", "Whitelist has been disabled!");
                            }
                        }
                        return true;
                    default:
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Please select between true or false!");
                        }else{
                            plugin.log("INFO", "Please select between true of false!");
                        }
                        return true;
                }
            case "metrics":
                switch(args[2].toLowerCase()){
                    case "true":
                        if(plugin.getConfig().getBoolean("Metrics")){
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Metrics is already enabled!");
                            }else{
                                plugin.log("INFO", "Metrics is already enabled!");
                            }
                        }else{
                            plugin.getConfig().set("Metrics", true);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Metrics has been enabled!");
                            }else{
                                plugin.log("INFO", "Metrics has been enabled!");
                            }
                        }
                        return true;
                    case "false":
                        if(plugin.getConfig().getBoolean("Metrics")){
                            plugin.getConfig().set("Metrics", false);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Metrics has been disabled!");
                            }else{
                                plugin.log("INFO", "Metrics has been disabled!");
                            }
                        }
                        return true;
                    default:
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Please select between true or false!");
                        }else{
                            plugin.log("INFO", "Please select between true of false!");
                        }
                        return true;
                }
            case "updater":
                switch(args[2].toLowerCase()){
                    case "true":
                        if(plugin.getConfig().getBoolean("Updater")){
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Updater is already enabled!");
                            }else{
                                plugin.log("INFO", "Updater is already enabled!");
                            }
                        }else{
                            plugin.getConfig().set("Updater", true);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Updater has been enabled!");
                            }else{
                                plugin.log("INFO", "Updater has been enabled!");
                            }
                        }
                        return true;
                    case "false":
                        if(plugin.getConfig().getBoolean("Updater")){
                            plugin.getConfig().set("Updater", false);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Updater has been disabled!");
                            }else{
                                plugin.log("INFO", "Updater has been disabled!");
                            }
                        }else{
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Updater is already disabled!");
                            }else{
                                plugin.log("INFO", "Updater is already disabled!");
                            }
                        }
                        return true;
                    default:
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Please select between true or false!");
                        }else{
                            plugin.log("INFO", "Please select between true of false!");
                        }
                        return true;
                }
            case "debug":
                switch(args[2].toLowerCase()){
                    case "true":
                        if(plugin.getConfig().getBoolean("DebugMessages")){
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Debug messages are already enabled!");
                            }else{
                                plugin.log("INFO", "Debug messages are already enabled!");
                            }
                        }else{
                            plugin.getConfig().set("DebugMessages", true);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Debug messages have been enabled!");
                            }else{
                                plugin.log("INFO", "Debug messages have been enabled!");
                            }
                        }
                        return true;
                    case "false":
                        if(plugin.getConfig().getBoolean("DebugMessages")){
                            plugin.getConfig().set("DebugMessages", false);
                            plugin.saveConfig();
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Debug messages have been disabled!");
                            }else{
                                plugin.log("INFO", "Debug messages have been disabled!");
                            }
                        }else{
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Debug messages are already disabled!");
                            }else{
                                plugin.log("INFO", "Debug messages are already disabled!");
                            }
                        }
                        return true;
                    default:
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Please select between true or false!");
                        }else{
                            plugin.log("INFO", "Please select between true of false!");
                        }
                        return true;
                }
            case "focus":
                switch(args[2].toLowerCase()) {
                    case "file":
                        if (plugin.getConfig().getString("Focus").equals("file")) {
                            if (sender instanceof Player) {
                                sender.sendMessage(plugin.mStart + "Whitelist focus is already set to \"file\"!");
                            } else {
                                plugin.log("INFO", "Whitelist focus is already set to \"file\"!");
                            }
                            return true;
                        }
                        plugin.getConfig().set("Focus", "file");
                        plugin.saveConfig();
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Whitelist focus altered to \"file\"!");
                        } else {
                            plugin.log("INFO", "Whitelist focus altered to \"file\"!");
                        }
                        return true;
                    case "mysql":
                        if (plugin.getConfig().getString("Focus").equals("mysql")) {
                            if (sender instanceof Player) {
                                sender.sendMessage(plugin.mStart + "Whitelist focus is already set to \"mysql\"!");
                            } else {
                                plugin.log("INFO", "Whitelist focus is already set to \"mysql\"!");
                            }
                            return true;
                        }
                        plugin.getConfig().set("Focus", "mysql");
                        plugin.saveConfig();
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Whitelist focus altered to \"mysql\"!");
                        } else {
                            plugin.log("INFO", "Whitelist focus altered to \"mysql\"!");
                        }
                        return true;
                    case "sqlite":
                        if (plugin.getConfig().getString("Focus").equals("sqlite")) {
                            if (sender instanceof Player) {
                                sender.sendMessage(plugin.mStart + "Whitelist focus is already set to \"sqlite\"!");
                            } else {
                                plugin.log("INFO", "Whitelist focus is already set to \"sqlite\"!");
                            }
                            return true;
                        }
                        plugin.getConfig().set("Focus", "sqlite");
                        plugin.saveConfig();
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Whitelist focus altered to \"sqlite\"!");
                        } else {
                            plugin.log("INFO", "Whitelist focus altered to \"sqlite\"!");
                        }
                        return true;
                    case "url":
                        if (plugin.getConfig().getString("Focus").equals("url")) {
                            if (sender instanceof Player) {
                                sender.sendMessage(plugin.mStart + "Whitelist focus is already set to \"url\"!");
                            } else {
                                plugin.log("INFO", "Whitelist focus is already set to \"url\"!");
                            }
                            return true;
                        }
                        plugin.getConfig().set("Focus", "url");
                        plugin.saveConfig();
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Whitelist focus altered to \"url\"!");
                        } else {
                            plugin.log("INFO", "Whitelist focus altered to \"url\"!");
                        }
                        return true;
                    default:
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "The focus you selected, '" + args[2] + "', isn't a valid focus.");
                        } else {
                            plugin.log("INFO", "The focus you selected, '\" + args[2] + \"', isn't a valid focus.");
                        }
                        return true;
                }
            default:
                if(sender instanceof Player){
                    sender.sendMessage(plugin.mStart + "Currently, only a few settings may be modified from within the game:");
                    sender.sendMessage(plugin.mStart + "For a current list of these settings, as well as how to change them, please visit the BukkitDev page.");
                }else{
                    plugin.log("WARNING", "Currently, only a few settings may be modified from within the game:");
                    plugin.log("WARNING", "For a current list of these settings, as well as how to change them, please visit the BukkitDev page.");
                }
        }
        return true;
    }

}
