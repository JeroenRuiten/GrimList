/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Helper {

    public boolean run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return runPlayer(sender, args);
        }
        if (args.length < 2) {
            sender.sendMessage("*--------------------------------------------*");
            sender.sendMessage("|             GrimList Help Menu             |");
            sender.sendMessage("|                                            |");
            sender.sendMessage("|              /wl help <topic>              |");
            sender.sendMessage("|                                            |");
            sender.sendMessage("|                Valid Topic:                |");
            sender.sendMessage("|       add - Add player to whitelist.       |");
            sender.sendMessage("|   remove - Remove player from whitelist.   |");
            sender.sendMessage("|       delete - Remove player record.       |");
            sender.sendMessage("|     view - View recorded player data.      |");
            sender.sendMessage("|       getid - Lookup player's UUID.        |");
            sender.sendMessage("| set - Avoid configuration files, entirely. |");
            sender.sendMessage("*--------------------------------------------*");
            return true;
        }
        switch (args[1]) {
            case "add":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|           Topic: /wl add <player>          |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Will set a player's whitelist status to  |");
                sender.sendMessage("|   true. Will create a record, if one does  |");
                sender.sendMessage("|   not already exist.                       |");
                sender.sendMessage("*--------------------------------------------*");
                break;
            case "remove":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|          Topic: /wl remove <player>        |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Will set a player's whitelist status to  |");
                sender.sendMessage("|   false. Will not create a record, if one  |");
                sender.sendMessage("|   does not already exist.                  |");
                sender.sendMessage("*--------------------------------------------*");
                break;
            case "delete":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|          Topic: /wl delete <player>        |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Will delete a player's recorded data.    |");
                sender.sendMessage("|   This cannot be reversed by any method.   |");
                sender.sendMessage("*--------------------------------------------*");
                break;
            case "view":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|           Topic: /wl view <player>         |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Will return any recorded player data,    |");
                sender.sendMessage("|   if the executor has permission to see.   |");
                sender.sendMessage("*--------------------------------------------*");
                break;
            case "getid":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|          Topic: /wl getid <player>         |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Will return the UUID of the referred     |");
                sender.sendMessage("|   player. Will NOT use player records, to  |");
                sender.sendMessage("|   lookup. For that, refer to 'view'.       |");
                sender.sendMessage("*--------------------------------------------*");
                break;
            case "set":
                sender.sendMessage("*--------------------------------------------*");
                sender.sendMessage("|             GrimList Help Menu             |");
                sender.sendMessage("|        Topic: /wl set <conf> <setting>     |");
                sender.sendMessage("|                                            |");
                sender.sendMessage("| Function :                                 |");
                sender.sendMessage("|   Allows you to change your configuration  |");
                sender.sendMessage("|   settings from within the game. Incorrect |");
                sender.sendMessage("|   settings will be denied.                 |");
                sender.sendMessage("*--------------------------------------------*");
                break;
        }
        return true;
    }

    private boolean runPlayer(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9/wl help <topic>"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lValid Topic:&r"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7add - Add player to whitelist."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7remove - Remove player from whitelist."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7delete - Remove player record."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7view - View recorded player data."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7set - Avoid configuration files, entirely."));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
            return true;
        }
        switch (args[1]) {
            case "add":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl add <player>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Will set a player's whitelist status to"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7true. Will create a record, if one does"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7not already exist."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            case "remove":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl remove <player>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Will set a player's whitelist status to"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7false. Will not create a record, if one"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7does not already exist."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            case "delete":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl delete <player>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Will delete a player's recorded data."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7This cannot be reversed by any method."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            case "view":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl view <player>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Will return any recorded player data,"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7if the executor has permission to see."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            case "getid":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl getid <player>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Will return the UUID of the referred"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7player. Will NOT use player records, to"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7lookup. For that, refer to 'view'."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            case "set":
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &9&lTopic:&r&9 /wl set <conf> <setting>"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &l&aFunction :"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7Allows you to change your configuration"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7settings from within the game. Incorrect"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|     &7settings will be denied."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &lGrimList Help Menu&r"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6|  &c&lNo Help Topic Found ('&r&c&o" + args[1] + "&r&c&l')"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7----------------------------------&6*"));
                break;
        }
        return true;
    }
}
