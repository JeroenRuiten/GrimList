/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import io.github.ferusgrim.GrimList.FocusManagers.FileManager;
import io.github.ferusgrim.GrimList.FocusManagers.MySQLManager;

import java.io.File;
import java.util.logging.Level;

import io.github.ferusgrim.GrimList.FocusManagers.SQLiteManager;
import io.github.ferusgrim.GrimList.FocusManagers.URLManager;
import net.gravitydevelopment.updater.Updater;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class GrimList extends JavaPlugin {
    private PlayerData playerData;
    public boolean isUpdateAvailable;
    public FileManager filem;
    public MySQLManager mysqlm;
    public SQLiteManager sqlitem;
    public URLManager urlm;
    public String mStart = ChatColor.GREEN + "" + ChatColor.BOLD + "[GrimList] " + ChatColor.RESET + ChatColor.YELLOW + "";
    public String version = "";
    public String link = "";
    public String focusOn;
    public boolean whitelistOn;
    
    @Override
    public void onEnable(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
        whitelistOn = getConfig().getBoolean("Whitelist");
        if(!whitelistOn){
            log("WARNING", "The whitelist has been disabled! Why do you even want me? :(");
        }
        focusOn = getConfig().getString("Focus");
        switch(focusOn){
            case "file":
                filem = new FileManager(this);
                break;
            case "mysql":
                log("SEVERE", "GrimList is released, right now, to prepare users for the UUID changes. Right now, \"file\" is the only supported whitelist format.");
                log("SEVERE", "GrimList will be updated as soon as possible to include MySQL support, SQLite support, as well as URL support. I apologize for the inconvenience.");
                log("SEVERE", "Defaulting to \"file\" focus format.");
                filem = new FileManager(this);
                /*
                mysqlm = new MySQLManager(this);
                break;
            */
            case "sqlite":
                log("SEVERE", "GrimList is released, right now, to prepare users for the UUID changes. Right now, \"file\" is the only supported whitelist format.");
                log("SEVERE", "GrimList will be updated as soon as possible to include MySQL support, SQLite support, as well as URL support. I apologize for the inconvenience.");
                log("SEVERE", "Defaulting to \"file\" focus format.");
                filem = new FileManager(this);
                /*
                sqlitem = new SQLiteManager(this);
                break;
            */
            case "url":
                log("SEVERE", "GrimList is released, right now, to prepare users for the UUID changes. Right now, \"file\" is the only supported whitelist format.");
                log("SEVERE", "GrimList will be updated as soon as possible to include MySQL support, SQLite support, as well as URL support. I apologize for the inconvenience.");
                log("SEVERE", "Defaulting to \"file\" focus format.");
                filem = new FileManager(this);
                /*
                urlm = new URLManager(this);
                break;
            */
            default:
                log("SEVERE", "Whitelist focus is invalid, or blank! Defaulting to 'file'!");
                focusOn = "file";
                filem = new FileManager(this);
                break;
        }
        if(getConfig().getBoolean("Updater")){
            Updater updater = new Updater(this, 65828, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            isUpdateAvailable = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
            version = updater.getLatestName();
            link = updater.getLatestFileLink();
        }
        if(focusOn.equals("file")){
            playerData = new PlayerData(this);
            if(!new File(getDataFolder(), "playerdata.yml").exists()){
                playerData.saveDefault();
            }
        }
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerManager(this), this);
        getCommand("whitelist").setExecutor(new Executor(this));
    }

    public void log(String lvl, String MSG){
        if(lvl.equals("DEBUG")){
            if(!getConfig().getBoolean("DebugMessages")){
                return;
            }
            MSG = "Debug!: " + MSG;
            lvl = "INFO";
        }
        getLogger().log(Level.parse(lvl), MSG);
    }
}
