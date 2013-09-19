//////////////////////////////////////////////////////////
///// Copyright Nicholas Badger (a.k.a. FerusGrim) //////
////////////////////////////////////////////////////////
/////////////////////////// Attribution 3.0 Unported //
////////////////////////// (CC BY 3.0) ///////////////
/////////////////////////////////////////////////////
package com.caelusrp.grimlist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Nicholas Badger (a.k.a. FerusGrim)
 */

public class grimlist extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");

    static String maindir = "plugins/GrimList/";
    public static YamlConfiguration Settings;
    static File SettingsFile = new File(maindir + "config.yml");
    static ArrayList<String> WhiteListedPlayers = new ArrayList<String>();
    int RefreshWhitelistTaskID = -1;
    static boolean WhitelistON = true;

    public void onDisable() {
        //Clears the whitelist array to clear up memory
        WhiteListedPlayers = new ArrayList<String>();
        //Removes the config to clear up memory
        Settings = null;

        this.getServer().getScheduler().cancelAllTasks();
        RefreshWhitelistTaskID = -1;
    }

    
	public void onEnable() {
        new File(maindir).mkdir();
        //Checks if file exists
        if (!SettingsFile.exists()) {
            //Creates the config file if it doesn't exist
            try {
                SettingsFile.createNewFile();
                Settings = config.loadMain(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //If it exists load the config
            Settings = config.loadMain(false);
        }
        // Sets the whitelists mode
        WhitelistON = grimlist.Settings.getBoolean("grimlist.enabled");

        PluginManager pm = getServer().getPluginManager();

        //Registers the Listener class
        pm.registerEvents(new listener(), this);

        log.log(Level.INFO, "[GL] Whitelist is " + (WhitelistON == true ? "active" : "inactive"));
        DebugPrint("Debug is active - if you do not wish to see these messages, disable debug in the 'config.yml' file.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String commandLabel, String[] args) {
        String commandName = command.getName().toLowerCase();
        String[] trimmedArgs = args;

        if (commandName.equals("grimlist")) {
            return CommandHandler(sender, trimmedArgs);
        }
        return false;
    }

    public static void SendMessage(CommandSender Player, String MSG){
        Player.sendMessage("§7[GL] §f" + MSG);
    }

    public boolean CommandHandler(CommandSender sender, String[] trimmedArgs) {
        if (trimmedArgs.length > 0) {
            String[] args = RearangeString(1, trimmedArgs);
            String CommandName = trimmedArgs[0];
            if (CommandName.equals("add")) {
                return AddPlayerToWhitelist(sender, args);
            }
            if (CommandName.equals("remove")) {
                return RemovePlayerFromWhitelist(sender, args);
            }
            if (CommandName.equals("reload")) {
                return ReloadPlugin(sender, args);
            }
            if (CommandName.equals("on")) {
                return WhitelistOn(sender, args);
            }
            if (CommandName.equals("off")) {
                return WhitelistOff(sender, args);
            }
        }
        PrintHelper(sender);
        return true;
    }

    private boolean WhitelistOn(CommandSender sender, String[] args){
        boolean auth = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (player.hasPermission("grimlist.toggle"))
                auth = true;
        } else {
            auth = true;
        }
        if (auth) {
            WhitelistON = true;
            log.log(Level.INFO, "[GL] "
                    + (player == null ? "A console administrator" : player.getName())
                    + " activated GrimList!");
            sender.sendMessage("[GL] GrimList is now activated!");
            return true;
        }
        sender.sendMessage("§cAccess to GrimList has been denied!");
        return true;
    }

    private boolean WhitelistOff(CommandSender sender, String[] args){
        boolean auth = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (player.hasPermission("grimlist.toggle"))
                auth = true;
        } else {
            auth = true;
        }
        if (auth) {
            WhitelistON = false;
            log.log(Level.INFO, "[GL] "
                    + (player == null ? "A console administrator" : player.getName())
                    + " deactivated GrimList!");
            sender.sendMessage("[GL] GrimList is now deactivated!");
            return true;
        }
        sender.sendMessage("§cAccess to GrimList has been denied!");
        return true;
    }

    private void PrintHelper(CommandSender sender){
        sender.sendMessage("Variables:");
        sender.sendMessage("    on/off");
        sender.sendMessage("    add/remove [player]");
        sender.sendMessage("    reload");
    }

    private boolean ReloadPlugin(CommandSender sender, String[] args){
        boolean auth = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (player.hasPermission("grimlist.reload"))
                auth = true;
        } else {
            auth = true;
        }
        if (auth) {
            onDisable();
            onEnable();
            log.log(Level.INFO, "[GL] "
                    + (player == null ? "A console administrator" : player.getName())
                    + " has reloaded GrimList!");
            sender.sendMessage("[GL] The GrimList configuration has been reloaded!");
            return true;
        }
        sender.sendMessage("§cAccess to GrimList has been denied!");
        return true;
    }

    @SuppressWarnings("unused")
	private ArrayList<String> GetWhitelist(String Type){
        ArrayList<String> tmpArray = new ArrayList<String>();
        Connection conn = connector.getSQLConnection();
        if (conn == null) {
             log.log(Level.SEVERE,
                     "[GL-x1] Could not establish SQL connection.");
             return null;
         } else {

             PreparedStatement ps = null;
             ResultSet rs = null;
             try {
                 String Query = grimlist.Settings.getString("grimlist.sql.query");
                 Query = Query.replace("{table}",
                         grimlist.Settings.getString("grimlist.sql.table"));
                 Query = Query.replace("{name}",
                         grimlist.Settings.getString("grimlist.sql.field"));
                 Query = Query.replace("{time}", "" + GetTime());
                 ps = conn.prepareStatement(Query);
                 rs = ps.executeQuery();
                 while (rs.next()) {
                     tmpArray.add(rs.getString(
                             grimlist.Settings.getString("grimlist.sql.field")).toLowerCase());
                 }
                 DebugPrint("Whitelist (type:" + Type +" count: " + tmpArray.toArray().length + ")");
                 return tmpArray;
             } catch (SQLException ex) {
                 log.log(Level.SEVERE,
                         "[GL-x2] Could not execute SQL statement: ",
                         ex);
             } finally {
                 try {
                     if (ps != null)
                         ps.close();
                     if (conn != null)
                         conn.close();
                 } catch (SQLException ex) {
                     log.log(Level.SEVERE,
                             "[GL-x3] Could not close SQL connection: ",
                             ex);
                 }
             }

             try {
                 conn.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
        return null;
    }

    private boolean AddPlayerToWhitelist(CommandSender sender, String[] args) {
        boolean auth = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (player.hasPermission("grimlist.add"))
                auth = true;
        } else {
            auth = true;
        }
        if (auth) {
            if (args.length > 0) {
                String p = args[0];

                if (grimlist.WhiteListedPlayers.contains(p
                        .toLowerCase())) {
                    sender.sendMessage("§cThe player '§e" + p
                            + "§c' is already whitelisted!");
                    return true;
                }

                grimlist.WhiteListedPlayers.add(p.toLowerCase());
                    Connection conn = null;
                    PreparedStatement ps = null;
                    try {
                        conn = connector.getSQLConnection();
                        ps = conn.prepareStatement("INSERT INTO "
                                + Settings.getString("grimlist.sql.table") + " (" + Settings.getString("grimlist.sql.field") + ") VALUES(?)");
                        ps.setString(1, p);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE,
                                "[GL-x4] Could not execute SQL statement: ",
                                ex);
                    } finally {
                        try {
                            if (ps != null)
                                ps.close();
                            if (conn != null)
                                conn.close();
                        } catch (SQLException ex) {
                            log.log(Level.SEVERE,
                                    "[GL-x5] Could not close SQL connection: ",
                                    ex);
                        }
                    }

                    log.log(Level.INFO, "[GL] "
                            + (player == null ? "A console administrator" : player.getName())
                            + " added " + p + " to the whitelist.");
                    sender.sendMessage("§6Successfully added " + p + " to the whitelist!");
                    return true;
            }
        }
        sender.sendMessage("§cAccess to GrimList has been denied!");
        return true;
    }

    private boolean RemovePlayerFromWhitelist(CommandSender sender, String[] args) {
        boolean auth = false;
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (player.hasPermission("grimlist.remove"))
                auth = true;
        } else {
            auth = true;
        }
        if (auth) {
            if (args.length > 0) {
                String p = args[0];
                Player victim = this.getServer().getPlayer(p);

                if (!grimlist.WhiteListedPlayers.contains(p
                        .toLowerCase())) {
                    sender.sendMessage("§cThe player '§e" + p
                            + "§c' is not whitelisted!");
                    return true;
                }

                if (victim != null) {
                    p = victim.getName();
                }

                grimlist.WhiteListedPlayers.remove(p
                        .toLowerCase());
                    Connection conn = null;
                    PreparedStatement ps = null;
                    try {
                        conn = connector.getSQLConnection();
                        ps = conn.prepareStatement("DELETE FROM " + Settings.getString("grimlist.sql.table") + " WHERE (" + Settings.getString("grimlist.sql.field") + ") = ?");
                        ps.setString(1, p);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE,
                                "[GL-x5] Could not execute SQL statement: ",
                                ex);
                    } finally {
                        try {
                            if (ps != null)
                                ps.close();
                            if (conn != null)
                                conn.close();
                        } catch (SQLException ex) {
                            log.log(Level.SEVERE,
                                    "[GL-x6] Could not close SQL connection: ",
                                    ex);
                        }
                    }
                log.log(Level.INFO, "[GL] "
                        + (player == null ? "A console administrator" : player.getName())
                        + " removed " + p + " from the whitelist.");
                sender.sendMessage(p + " was successfully removed from the whitelist!");
                return true;
            }
        }
        sender.sendMessage("§cAccess to GrimList has been denied!");
        return true;
    }

    public static void DebugPrint(String MSG) {
        if (Settings.getBoolean("grimlist.debug")) {
            log.log(Level.INFO, "[GL-debug] " + MSG);
        }
    }

    public static String[] RearangeString(int startIndex, String[] string) {
        String TMPString = "";
        for (int i = startIndex; i < string.length; i++) {
            String Add = " ";
            if (i == startIndex) {
                Add = "";
            }
            TMPString += Add + string[i];
        }
        return TMPString.split("\\ ");
    }

    public long GetTime() {
        return System.currentTimeMillis() / 1000L;
    }
}