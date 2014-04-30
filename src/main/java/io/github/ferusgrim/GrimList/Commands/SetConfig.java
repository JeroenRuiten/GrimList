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
        if (s1.equals("Whitelist") || s1.equals("Metrics") || s1.equals("Updater") || s1.equals("Debugmessages") || s1.equals("Savequeries") || s1.equals("Kickremove")) {
            if (s1.equals("Debugmessages")) {
                s1 = "DebugMessages";
            }
            if (s1.equals("Savequeries")) {
                s1 = "SaveQueries";
            }
            if (s1.equals("Kickremove")) {
                s1 = "KickRemove";
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
        if (s1.equals("Notify")) {
            String s2 = WordUtils.capitalize(args[2].toLowerCase());
            if(s2.equals("Console") || s2.equals("Player")) {
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
            if (s2.equals("mysql") || s2.equals("sqlite") || s2.equals("url")) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Currently, GrimList only supports \"file\" as a whitelist focus.");
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
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Whitelist focus altered to \"" + s2 + "\"!");
            return true;
        }
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Config setting doesn't exist, or can't be modified in-game.");
        return true;
    }
}
