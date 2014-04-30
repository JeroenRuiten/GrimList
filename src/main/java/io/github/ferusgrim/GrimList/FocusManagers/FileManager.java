/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.FocusManagers;

import io.github.ferusgrim.GrimList.GrimList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileManager {
    private GrimList plugin;

    private FileConfiguration playerData = null;
    private File playerDataF = null;

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
        if (!new File(plugin.getDataFolder(), "playerdata.yml").exists()) {
            saveDefault();
        }
    }

    private void registerPaths(String uuid) {
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

    public boolean alreadyOnWhitelist(String uuid) {
        registerPaths(uuid);
        return get().isSet(path) && get().getBoolean(isWhitelisted);
    }

    public boolean recordExists(String uuid) {
        registerPaths(uuid);
        return get().isSet(path);
    }

    public void deleteRecord(String uuid) {
        registerPaths(uuid);
        get().set(path, null);
        save();
    }

    public List<String> setupViewPlayers(String uuid) {
        registerPaths(uuid);
        List<String> setupView = new ArrayList<>();
        setupView.add(get().getString(lastUsername));
        setupView.add(get().getBoolean(isWhitelisted) ? "Yes" : "No");
        setupView.add(get().getString(firstLogin));
        setupView.add(get().getString(lastLogin));
        setupView.add(String.valueOf(get().getInt(loginCount)));
        return setupView;
    }

    public List<String> setupPreviousUsernames(String uuid) {
        registerPaths(uuid);
        List<String> usernames = new ArrayList<>();
        List<String> tmpStr = new ArrayList<>(get().getStringList(previousUsernames));
        usernames.addAll(tmpStr.stream().collect(Collectors.toList()));
        tmpStr.clear();
        return usernames;
    }

    public List<String> setupPreviousAddresses(String uuid) {
        registerPaths(uuid);
        List<String> addresses = new ArrayList<>();
        List<String> tmpStr = new ArrayList<>(get().getStringList(previousAddresses));
        addresses.addAll(tmpStr.stream().collect(Collectors.toList()));
        tmpStr.clear();
        return addresses;
    }

    public String getUUID(String name) {
        if (get().getConfigurationSection("Players") == null) {
            return "";
        }
        for (String uuid : get().getConfigurationSection("Players").getKeys(false)) {
            if (get().getString("Players." + uuid + ".lastUsername").equalsIgnoreCase(name)) {
                return uuid;
            }
        }
        return "";
    }

    public void newPlayerRecord(String uuid, String name) {
        registerPaths(uuid);
        get().createSection(path);
        get().set(isWhitelisted, false);
        get().set(lastUsername, name);
        save();
    }

    public void toggleIsWhitelisted(String uuid, String name) {
        registerPaths(uuid);
        if (alreadyOnWhitelist(uuid)) {
            get().set(isWhitelisted, false);
        } else {
            get().set(isWhitelisted, true);
        }
        get().set(lastUsername, name);
        save();
    }

    public void onLoginRecordUpdater(String uuid, String playerName, String playerAddress) {
        registerPaths(uuid);
        if (!get().isConfigurationSection(path)) {
            get().createSection(path);
        }
        if (!get().isSet(isWhitelisted)) {
            get().set(isWhitelisted, false);
        }
        get().set(lastUsername, playerName);
        List<String> tmpUn = new ArrayList<>(get().getStringList(previousUsernames));
        if (!tmpUn.contains(playerName)) {
            tmpUn.add(playerName);
            get().set(previousUsernames, tmpUn);
        }
        get().set(lastAddress, playerAddress);
        List<String> tmpAd = new ArrayList<>(get().getStringList(previousAddresses));
        if (!tmpAd.contains(playerAddress)) {
            tmpAd.add(playerAddress);
            get().set(previousAddresses, tmpAd);
        }
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        Date rDate = new Date();
        String sDate = fDate.format(rDate);
        if (!get().isSet(firstLogin)) {
            get().set(firstLogin, sDate);
        }
        get().set(lastLogin, sDate);
        get().set(loginCount, get().getInt(loginCount) + 1);
        save();
    }

    public void reload() {
        if (playerDataF == null) {
            playerDataF = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        playerData = YamlConfiguration.loadConfiguration(playerDataF);
        InputStream pDataStream = plugin.getResource("playerdata.yml");
        if (pDataStream != null) {
            YamlConfiguration pDataDef = YamlConfiguration.loadConfiguration(pDataStream);
            playerData.setDefaults(pDataDef);
        }
    }

    public void save() {
        if (playerData == null || playerDataF == null) {
            return;
        }
        try {
            get().save(playerDataF);
        } catch (IOException e) {
            plugin.log("SEVERE", "Couldn't save playerdata configuration!");
        }
    }

    public FileConfiguration get() {
        if (playerData == null) {
            reload();
        }
        return playerData;
    }

    public void saveDefault() {
        if (playerDataF == null) {
            playerDataF = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        if (!playerDataF.exists()) {
            plugin.saveResource("playerdata.yml", false);
        }
    }
}
