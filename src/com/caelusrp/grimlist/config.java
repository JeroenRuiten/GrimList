//////////////////////////////////////////////////////////
///// Copyright Nicholas Badger (a.k.a. FerusGrim) //////
////////////////////////////////////////////////////////
/////////////////////////// Attribution 3.0 Unported //
////////////////////////// (CC BY 3.0) ///////////////
/////////////////////////////////////////////////////
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
            SetDefault("grimlist.debug", false);
            SetDefault("grimlist.notification.disallow-message", "Hi §1{player}§f, you are not on the whitelist!");
            SetDefault("grimlist.notification.print-failure", true);
            SetDefault("grimlist.notification.failure-message", "Disallowed §2{player}§f from joining");
            SetDefault("grimlist.sql.host", "localhost");
            SetDefault("grimlist.sql.port", 3306);
            SetDefault("grimlist.sql.username", "root");
            SetDefault("grimlist.sql.password", "toor");
            SetDefault("grimlist.sql.database", "whitelist");
            SetDefault("grimlist.sql.table", "users");
            SetDefault("grimlist.sql.field", "username");
            SetDefault("grimlist.sql.query", "SELECT {field} FROM `{table}`;");
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