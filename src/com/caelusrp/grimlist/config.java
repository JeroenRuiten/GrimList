// Copyright Nicholas Badger (A.K.A. FerusGrim) //
// Attribution 3.0 Unported (CC BY 3.0) //

package com.caelusrp.grimlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class config {
    public static YamlConfiguration config;

    public static void SetDefault(String Path, Object Value){
        config.set(Path, config.get(Path, Value));
    }

    public static YamlConfiguration loadMain(boolean Create){
        String maindir = "plugins/GrimList/";
        File Settings = new File(maindir + "config.yml");
        try {
            config = new YamlConfiguration();
            if(Create == false){
                config.load(Settings);
            }
            SetDefault("grimlist.enabled", true);
            SetDefault("grimlist.connection", "mysql");
            SetDefault("grimlist.debug", false);
            SetDefault("grimlist.notification.disallow-message", "§e[GL]§f Sorry, {player}, but you aren't whitelisted.");
            SetDefault("grimlist.notification.print-failure", true);
            SetDefault("grimlist.notification.failure-message", "§e[GL]§f {player} attempted to join.");
            SetDefault("grimlist.file.name", "players.txt");
            SetDefault("grimlist.file.update-interval", "10");
            SetDefault("grimlist.mysql.host", "localhost");
            SetDefault("grimlist.mysql.port", 3306);
            SetDefault("grimlist.mysql.username", "root");
            SetDefault("grimlist.mysql.password", "toor");
            SetDefault("grimlist.mysql.database", "whitelist");
            SetDefault("grimlist.mysql.table", "users");
            SetDefault("grimlist.mysql.field", "username");
            SetDefault("grimlist.mysql.query", "SELECT {field} FROM `{table}`;");
            config.save(Settings);
            return config;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}