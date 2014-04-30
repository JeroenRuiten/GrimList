/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import io.github.ferusgrim.GrimList.FocusManagers.FileManager;
import net.gravitydevelopment.updater.Updater;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Copyright (C) 2014 Nicholas Badger
 *
 * @author FerusGrim
 */

public class GrimList extends JavaPlugin {
    public boolean isUpdateAvailable;
    public FileManager filem;
    public String mStart = ChatColor.translateAlternateColorCodes('&', "&a&l[GrimList]&r&e ");
    public String version = "";
    public String link = "";
    public String focusOn;

    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        if (!getConfig().getBoolean("Whitelist")) {
            log("WARNING", "The whitelist has been disabled! Why do you even want me? :(");
        }
        focusOn = getConfig().getString("Focus");
        switch (focusOn) {
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
        if (getConfig().getBoolean("Updater")) {
            Updater updater = new Updater(this, 65828, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
            isUpdateAvailable = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
            version = updater.getLatestName();
            link = updater.getLatestFileLink();
        }
        if (getConfig().getBoolean("Metrics")) {
            MetricManager mm = new MetricManager(this);
            mm.setupMetric();
        }
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerManager(this), this);
        getCommand("whitelist").setExecutor(new Executor(this));
    }

    public void log(String lvl, String MSG) {
        if (lvl.equals("DEBUG")) {
            if (!getConfig().getBoolean("DebugMessages")) {
                return;
            }
            MSG = "Debug!: " + MSG;
            lvl = "INFO";
        }
        getLogger().log(Level.parse(lvl), MSG);
    }
}
