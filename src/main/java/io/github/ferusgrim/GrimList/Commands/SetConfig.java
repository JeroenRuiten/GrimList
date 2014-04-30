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
        String s1 = WordUtils.capitalize(args[1].toLowerCase());
        String s2 = args[2].toLowerCase();
        if (s1.equals("Whitelist") || s1.equals("Metrics") || s1.equals("Updater") || s1.equals("Debugmessages") || s1.equals("Savequeries")) {
            if (s1.equals("Debugmessages")) {
                s1 = "DebugMessages";
            }
            if (s1.equals("Savequeries")) {
                s1 = "SaveQueries";
            }
            if (!s2.equals("true") && !s2.equals("false")) {
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + "Please select between true or false!");
                } else {
                    plugin.log("INFO", "Please select between true of false!");
                }
                return true;
            }
            if (s2.equals("true")) {
                if (plugin.getConfig().getBoolean(s1)) {
                    if (sender instanceof Player) {
                        sender.sendMessage(plugin.mStart + s1 + " is already enabled!");
                    } else {
                        plugin.log("INFO", s1 + " is already enabled!");
                    }
                    return true;
                }
                plugin.getConfig().set(s1, true);
                plugin.saveConfig();
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + s1 + " has been enabled!");
                } else {
                    plugin.log("INFO", s1 + " has been enabled!");
                }
                return true;
            }
            if (s2.equals("false")) {
                if (!plugin.getConfig().getBoolean(s1)) {
                    if (sender instanceof Player) {
                        sender.sendMessage(plugin.mStart + s1 + " is already disabled!");
                    } else {
                        plugin.log("INFO", s1 + " is already disabled!");
                    }
                    return true;
                }
                plugin.getConfig().set(s1, false);
                plugin.saveConfig();
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + s1 + " has been disabled!");
                } else {
                    plugin.log("INFO", s1 + " has been disabled!");
                }
                return true;
            }

        }
        if (s1.equals("Focus")) {
            if (!s2.equals("file") && !s2.equals("mysql") && !s2.equals("sqlite") && !s2.equals("url")) {
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + "The focus you selected, \"" + s2 + "\", isn't a valid focus.");
                } else {
                    plugin.log("INFO", "The focus you selected, '\"" + s2 + "\"', isn't a valid focus.");
                }
                return true;
            }
            //TODO - Remove the below if statements as more methods are added.
            if (s2.equals("mysql") || s2.equals("sqlite") || s2.equals("url")) {
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + "Currently, GrimList only supports \"file\" as a whitelist focus.");
                    sender.sendMessage(plugin.mStart + "GrimList v3.0 was released early to address the upcoming UUID necessity.");
                    sender.sendMessage(plugin.mStart + "I, personally, apologize for any inconvenience. GrimList 3.1 will be released soon, with full functionality.");
                } else {
                    plugin.log("INFO", "Currently, GrimList only supports \"file\" as a whitelist focus.");
                    plugin.log("INFO", "GrimList v3.0 was released early to address the upcoming UUID necessity.");
                    plugin.log("INFO", "I, personally, apologize for any inconvenience. GrimList 3.1 will be released soon, with full functionality.");
                }
                return true;
            }
            if (plugin.getConfig().getString("Focus").equals(s2)) {
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + "Whitelist focus is already set to \"" + s2 + "\"!");
                } else {
                    plugin.log("INFO", "Whitelist focus is already set to \"" + s2 + "\"!");
                }
                return true;
            }
            plugin.getConfig().set("Focus", s2);
            plugin.saveConfig();
            plugin.focusOn = s2;
            if (sender instanceof Player) {
                sender.sendMessage(plugin.mStart + "Whitelist focus altered to \"" + s2 + "\"!");
            } else {
                plugin.log("INFO", "Whitelist focus altered to \"" + s2 + "\"!");
            }
            return true;
        }
        return true;
    }
}
