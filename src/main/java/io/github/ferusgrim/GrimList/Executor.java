/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import io.github.ferusgrim.GrimList.Commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Executor implements CommandExecutor {
    private final GrimList plugin;

    public Executor(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            args = new String[]{"help"};
        }
        String function = inferAlias(args[0].toLowerCase());
        if (containsError(sender, args)) {
            return true;
        }
        switch (function) {
            case "help":
                Helper hlp = new Helper();
                return hlp.run(sender, args);
            case "add":
                AddPlayer ap = new AddPlayer(plugin);
                return ap.run(sender, args[1]);
            case "remove":
                RemovePlayer rp = new RemovePlayer(plugin);
                return rp.run(sender, args[1]);
            case "delete":
                DeleteRecord dp = new DeleteRecord(plugin);
                return dp.run(sender, args[1]);
            case "view":
                ViewPlayer vp = new ViewPlayer(plugin);
                return vp.run(sender, args[1]);
            case "getid":
                GetUUID gid = new GetUUID(plugin);
                return gid.run(sender, args[1]);
            case "set":
                SetConfig sc = new SetConfig(plugin);
                return sc.run(sender, args);
            default:
                if (sender instanceof Player) {
                    sender.sendMessage(plugin.mStart + "Unknown or invalid command!");
                } else {
                    plugin.log("WARNING", "Unknown or invalid command!");
                }
                return true;
        }
    }

    private boolean containsError(CommandSender sender, String[] args) {
        String function = inferAlias(args[0].toLowerCase());
        if (!sender.hasPermission("grimlist." + args[0])) {
            sender.sendMessage(plugin.mStart + "Insufficient privileges!");
            return true;
        }
        if (function.equals("add") || function.equals("remove") || function.equals("delete") || function.equals("view") || function.equals("getid")) {
            if (args.length < 2) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Missing username!");
                return true;
            }
            if (!args[1].matches("[a-zA-Z0-9_]{3,16}")) {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Invalid username!");
                return true;
            }
        }
        if (function.equals("set") && args.length < 3) {
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Not enough arguments to use this command!");
            return true;
        }
        return false;
    }

    private String inferAlias(String function) {
        switch (function) {
            case "-h":
                function = "help";
                break;
            case "-a":
                function = "add";
                break;
            case "-r":
                function = "remove";
                break;
            case "-d":
                function = "delete";
                break;
            case "-v":
                function = "view";
                break;
            case "-s":
                function = "set";
                break;
            case "-g":
                function = "getid";
                break;
        }
        return function;
    }
}
