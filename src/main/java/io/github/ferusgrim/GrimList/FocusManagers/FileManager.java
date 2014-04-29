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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class FileManager {
    private GrimList plugin;
    private PlayerData pd;
    
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
        pd = new PlayerData(plugin);
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
        if(!pd.get().isSet(path)){
            return false;
        }
        if(!pd.get().getBoolean(isWhitelisted)){
            return false;
        }
        return true;
    }

    public boolean recordExists(String uuid){
        registerPaths(uuid);
        return pd.get().isSet(path);
    }

    public void deleteRecord(String uuid){
        registerPaths(uuid);
        pd.get().set(path, null);
        pd.save();
    }

    public List<String> setupViewPlayers(String uuid){
        registerPaths(uuid);
        List<String> setupView = new ArrayList<String>();
        setupView.add(pd.get().getString(lastUsername));
        setupView.add(pd.get().getBoolean(isWhitelisted)? "Yes" : "No");
        setupView.add(pd.get().getString(firstLogin));
        setupView.add(pd.get().getString(lastLogin));
        setupView.add(String.valueOf(pd.get().getInt(loginCount)));
        return setupView;
    }

    public List<String> setupPreviousUsernames(String uuid){
        registerPaths(uuid);
        List<String> usernames = new ArrayList<String>();
        List<String> tmpStr = new ArrayList<String>(pd.get().getStringList(previousUsernames));
        for(int i = 0; i < tmpStr.size(); i++){
            usernames.add(tmpStr.get(i));
        }
        tmpStr.clear();
        return usernames;
    }

    public List<String> setupPreviousAddresses(String uuid){
        registerPaths(uuid);
        List<String> addresses = new ArrayList<String>();
        List<String> tmpStr = new ArrayList<String>(pd.get().getStringList(previousAddresses));
        for(int i = 0; i < tmpStr.size(); i++){
            addresses.add(tmpStr.get(i));
        }
        tmpStr.clear();
        return addresses;
    }

    public String getUUID(String name){
        for(String uuid : pd.get().getConfigurationSection("Players").getKeys(false)){
            if(pd.get().getString("Players." + uuid + ".lastUsername").equalsIgnoreCase(name)){
                return uuid;
            }
        }
        return "";
    }

    public void toggleIsWhitelisted(String uuid, String name){
        registerPaths(uuid);
        if(alreadyOnWhitelist(uuid)){
            pd.get().set(isWhitelisted, false);
        }else{
            pd.get().set(isWhitelisted, true);
        }
        pd.get().set(lastUsername, name);
        pd.save();
    }

    public void onLoginRecordUpdater(String uuid, String playerName, String playerAddress) {
        registerPaths(uuid);
        if(!pd.get().isConfigurationSection(path)){
            pd.get().createSection(path);
        }
        if(!pd.get().isSet(isWhitelisted)){
            pd.get().set(isWhitelisted, false);
        }
        pd.get().set(lastUsername, playerName);
        List<String> tmpUn = new ArrayList<String>(pd.get().getStringList(previousUsernames));
        if(!tmpUn.contains(playerName)){
            tmpUn.add(playerName);
            pd.get().set(previousUsernames, tmpUn);
        }
        pd.get().set(lastAddress, playerAddress);
        List<String> tmpAd = new ArrayList<String>(pd.get().getStringList(previousAddresses));
        if(!tmpAd.contains(playerAddress)){
            tmpAd.add(playerAddress);
            pd.get().set(previousAddresses, tmpAd);
        }
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        Date rDate = new Date();
        String sDate = fDate.format(rDate);
        if(!pd.get().isSet(firstLogin)){
            pd.get().set(firstLogin, sDate);
        }
        pd.get().set(lastLogin, sDate);
        pd.get().set(loginCount, pd.get().getInt(loginCount) + 1);
        pd.save();
    }
}
