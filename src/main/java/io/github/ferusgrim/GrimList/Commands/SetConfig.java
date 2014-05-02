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
    private final GrimList plugin;

    public SetConfig(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String[] args) {
        String s1 = WordUtils.capitalize(args[1].toLowerCase());
        if (s1.equals("Whitelist") || s1.equals("Metrics") || s1.equals("Updater") || s1.equals("Debugmessages") || s1.equals("Savequeries") || s1.equals("Kickremove") || s1.equals("Alwayslookup")) {
            if (s1.equals("Debugmessages")) {
                s1 = "DebugMessages";
            }
            if (s1.equals("Savequeries")) {
                s1 = "SaveQueries";
            }
            if (s1.equals("Kickremove")) {
                s1 = "KickRemove";
            }
            if (s1.equals("Alwayslookup")) {
                s1 = "AlwaysLookup";
            }
            if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Please select between true or false!");
                return true;
            }
            boolean newBoolean = Boolean.parseBoolean(args[2]);
            if (plugin.getConfig().getBoolean(s1) == newBoolean) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + s1 + " is already " + (newBoolean ? "enabled" : "disabled") + "!");
                return true;
            }
            plugin.getConfig().set(s1, newBoolean);
            plugin.saveConfig();
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + s1 + " has been " + (newBoolean ? "enabled" : "disabled") + "!");
            return true;
        }
        if (s1.equals("Notify") || s1.equals("Logusage")) {
            if (s1.equals("Logusage")) {
                s1 = "LogUsage";
            }
            String s2 = WordUtils.capitalize(args[2].toLowerCase());
            if (s2.equals("Console") || s2.equals("Player") || s2.equals("Add") || s2.equals("Remove") || s2.equals("Delete") || s2.equals("View")) {
                if (args.length < 4 || (args[3].equalsIgnoreCase("true")) && (args[3].equalsIgnoreCase("false"))) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Please select between true or false!");
                    return true;
                }
                boolean newBoolean = Boolean.parseBoolean(args[3]);
                if (plugin.getConfig().getBoolean(s1 + "." + s2)) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + s1 + "." + s2 + " is already " + (newBoolean ? "enabled" : "disabled") + "!");
                    return true;
                }
                plugin.getConfig().set(s1 + "." + s2, newBoolean);
                plugin.saveConfig();
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + s1 + "." + s2 + " has been " + (newBoolean ? "enabled" : "disabled") + "!");
                return true;
            }
        }
        if (s1.equals("Focus")) {
            String s2 = args[2].toLowerCase();
            if (!s2.equals("file") && !s2.equals("mysql") && !s2.equals("sqlite") && !s2.equals("url")) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "The focus you selected, \"" + s2 + "\", isn't a valid focus.");
                return true;
            }
            //TODO - Remove the below if statements as more methods are added.
            if (s2.equals("sqlite") || s2.equals("url")) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Currently, GrimList only supports \"file\" and \"mysql\" as a whitelist focus.");
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "GrimList v3.0 was released early to address the upcoming UUID necessity.");
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "I, personally, apologize for any inconvenience. GrimList 3.1 will be released soon, with full functionality.");
                return true;
            }
            if (plugin.getConfig().getString("Focus").equals(s2)) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Whitelist focus is already set to \"" + s2 + "\"!");
                return true;
            }
            plugin.getConfig().set("Focus", s2);
            plugin.saveConfig();
            plugin.focusOn = s2;
            plugin.updateFocus(s2);
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Whitelist focus altered to \"" + s2 + "\"!");
            return true;
        }
        if (s1.equals("Mysql")) {
            String s2 = args[2].toLowerCase();
            if (args.length < 4) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "You forgot what to alter " + s2 + " to!");
                return true;
            }
            if (s2.equals("host") || s2.equals("database") || s2.equals("username") || s2.equals("username") || s2.equals("password")) {
                if (plugin.getConfig().getString("MySQL." + s2).equals(args[3])) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "MySQL " + WordUtils.capitalize(s2) + " is already set to : " + args[3]);
                    return true;
                } else {
                    plugin.getConfig().set("MySQL." + s2, args[3]);
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "MySQL " + WordUtils.capitalize(s2) + " was altered to : " + args[3]);
                }
            }
            if (s2.equals("port")) {
                int s3I;
                if (s2.equals("port")) {
                    if (!args[3].matches("[0-9]{1,5}") || args[3].length() > 5) {
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Invalid port!");
                        return true;
                    }
                    s3I = Integer.parseInt(args[3]);
                    if (plugin.getConfig().getInt("MySQL.port") == s3I) {
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "MySQL Port is already set to : " + s3I);
                    } else {
                        plugin.getConfig().set("MySQL.port", s3I);
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "MySQL Port altered to : " + s3I);
                    }
                }
            }
        }
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Config setting doesn't exist, or can't be modified in-game.");
        return true;
    }
}
