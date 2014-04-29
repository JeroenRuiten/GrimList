/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.FocusManagers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.PlayerData;

public class FileManager {
    private GrimList plugin;
    
    private String path;
    private String isWhitelisted;
    private String lastUsername;
    private String previousUsernames;
    private String lastAddress;
    private String previousAddresses;
    private String firstLogin;
    private String lastLogin;
    private String loginCount;
    
    public FileManager(GrimList plugin) {
        this.plugin = plugin;
    }
    
    private void registerPaths(String uuid){
        path = "Players." + uuid;
        isWhitelisted = path + ".isWhitelisted";
        lastUsername = path + ".lastUsername";
        previousUsernames = path + ".knownUsernames";
        lastAddress = path + ".lastAddress";
        previousAddresses = path + ".knownAddresses";
        firstLogin = path + ".firstLogin";
        lastLogin = path + ".lastLogin";
        loginCount = path + ".loginCount";
    }

    public boolean alreadyOnWhitelist(String uuid){
        registerPaths(uuid);
        return plugin.playerData.get().isSet(path) && plugin.playerData.get().getBoolean(isWhitelisted);
    }

    public boolean recordExists(String uuid){
        registerPaths(uuid);
        return plugin.playerData.get().isSet(path);
    }

    public void deleteRecord(String uuid){
        registerPaths(uuid);
        plugin.playerData.get().set(path, null);
        plugin.playerData.save();
    }

    public List<String> setupViewPlayers(String uuid){
        registerPaths(uuid);
        List<String> setupView = new ArrayList<String>();
        setupView.add(plugin.playerData.get().getString(lastUsername));
        setupView.add(plugin.playerData.get().getBoolean(isWhitelisted)? "Yes" : "No");
        setupView.add(plugin.playerData.get().getString(firstLogin));
        setupView.add(plugin.playerData.get().getString(lastLogin));
        setupView.add(String.valueOf(plugin.playerData.get().getInt(loginCount)));
        return setupView;
    }

    public List<String> setupPreviousUsernames(String uuid){
        registerPaths(uuid);
        List<String> usernames = new ArrayList<String>();
        List<String> tmpStr = new ArrayList<String>(plugin.playerData.get().getStringList(previousUsernames));
        for (String aTmpStr : tmpStr) {
            usernames.add(aTmpStr);
        }
        tmpStr.clear();
        return usernames;
    }

    public List<String> setupPreviousAddresses(String uuid){
        registerPaths(uuid);
        List<String> addresses = new ArrayList<String>();
        List<String> tmpStr = new ArrayList<String>(plugin.playerData.get().getStringList(previousAddresses));
        for (String aTmpStr : tmpStr) {
            addresses.add(aTmpStr);
        }
        tmpStr.clear();
        return addresses;
    }

    public String getUUID(String name){
        if(plugin.playerData.get().getConfigurationSection("Players") == null){
            return "";
        }
        for(String uuid : plugin.playerData.get().getConfigurationSection("Players").getKeys(false)){
            if(plugin.playerData.get().getString("Players." + uuid + ".lastUsername").equalsIgnoreCase(name)){
                return uuid;
            }
        }
        return "";
    }

    public void toggleIsWhitelisted(String uuid, String name){
        registerPaths(uuid);
        if(alreadyOnWhitelist(uuid)){
            plugin.playerData.get().set(isWhitelisted, false);
        }else{
            plugin.playerData.get().set(isWhitelisted, true);
        }
        plugin.playerData.get().set(lastUsername, name);
        plugin.playerData.save();
    }

    public void onLoginRecordUpdater(String uuid, String playerName, String playerAddress) {
        registerPaths(uuid);
        if(!plugin.playerData.get().isConfigurationSection(path)){
            plugin.playerData.get().createSection(path);
        }
        if(!plugin.playerData.get().isSet(isWhitelisted)){
            plugin.playerData.get().set(isWhitelisted, false);
        }
        plugin.playerData.get().set(lastUsername, playerName);
        List<String> tmpUn = new ArrayList<String>(plugin.playerData.get().getStringList(previousUsernames));
        if(!tmpUn.contains(playerName)){
            tmpUn.add(playerName);
            plugin.playerData.get().set(previousUsernames, tmpUn);
        }
        plugin.playerData.get().set(lastAddress, playerAddress);
        List<String> tmpAd = new ArrayList<String>(plugin.playerData.get().getStringList(previousAddresses));
        if(!tmpAd.contains(playerAddress)){
            tmpAd.add(playerAddress);
            plugin.playerData.get().set(previousAddresses, tmpAd);
        }
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        Date rDate = new Date();
        String sDate = fDate.format(rDate);
        if(!plugin.playerData.get().isSet(firstLogin)){
            plugin.playerData.get().set(firstLogin, sDate);
        }
        plugin.playerData.get().set(lastLogin, sDate);
        plugin.playerData.get().set(loginCount, plugin.playerData.get().getInt(loginCount) + 1);
        plugin.playerData.save();
    }
}
