/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
    private GrimList plugin;
    private FileConfiguration playerData = null;
    private File playerDataF = null;

    public PlayerData(GrimList plugin) {
        this.plugin = plugin;
    }

    public void reload(){
        if(playerDataF == null){
            playerDataF = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataF);
        InputStream pDataStream = plugin.getResource("playerdata.yml");
        if(pDataStream != null){
            YamlConfiguration pDataDef = YamlConfiguration.loadConfiguration(pDataStream);
            playerData.setDefaults(pDataDef);
        }
    }
    
    public void save(){
        if(playerData == null || playerDataF == null){
            return;
        }
        try{
            get().save(playerDataF);
        }catch(IOException e){
            plugin.log("SEVERE", "Couldn't save playerdata configuration!");
        }
    }
    
    public FileConfiguration get(){
        if(playerData == null){
            reload();
        }
        return playerData;
    }
    
    public void saveDefault(){
        if(playerDataF == null){
            playerDataF = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        if(!playerDataF.exists()){
            plugin.saveResource("playerdata.yml", false);
        }
    }
}
