/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package net.caelumcraft.GrimList.FocusManagers;

import net.caelumcraft.GrimList.GrimList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManager {
    private final GrimList plugin;

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

    public boolean isPlayersPopulated() {
        return get().getConfigurationSection("Players") != null;
    }

    public boolean doesRecordExist(String uuid) {
        registerPaths(uuid);
        return get().isSet(path);
    }

    public boolean isPlayerWhitelisted(String uuid) {
        registerPaths(uuid);
        return isPlayersPopulated() && doesRecordExist(uuid) && get().getBoolean(isWhitelisted);
    }

    public String getUUID(String name) {
        for (String uuid : get().getConfigurationSection("Players").getKeys(false)) {
            if (get().getString("Players." + uuid + ".lastUsername").equalsIgnoreCase(name)) {
                return uuid;
            }
        }
        return "";
    }

    public void recordAfterIdLookup(String uuid, String name) {
        registerPaths(uuid);
        get().createSection(path);
        get().set(isWhitelisted, false);
        get().set(lastUsername, name);
        save();
    }

    public void addPlayerToWhitelist(String uuid, String name) {
        registerPaths(uuid);
        get().set(isWhitelisted, true);
        get().set(lastUsername, name);
        save();
    }

    public void removePlayerFromWhitelist(String uuid, String name) {
        registerPaths(uuid);
        get().set(isWhitelisted, false);
        get().set(lastUsername, name);
        save();
    }

    public void deleteRecord(String uuid) {
        registerPaths(uuid);
        get().set(path, null);
        save();
    }

    public void ViewPlayer(CommandSender sender, String uuid) {
        registerPaths(uuid);
        String lastUsername = get().getString(this.lastUsername);
        String isWhitelisted = get().getBoolean(this.isWhitelisted) ? "Yes" : "No";
        String firstLogin = get().getString(this.firstLogin);
        String lastLogin = get().getString(this.lastLogin);
        String loginCount = String.valueOf(get().getString(this.loginCount));
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + lastUsername + " &r&7--&6*"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lUUID: &r&e" + uuid));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lWhitelisted: &r&e" + isWhitelisted));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Usernames:"));
            for (String viewPU : new ArrayList<>(get().getStringList(previousUsernames))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&e" + viewPU));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Addresses:"));
            for (String viewPI : new ArrayList<>(get().getStringList(previousAddresses))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&e" + viewPI));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lFirst Login: &r&e" + (firstLogin == null ? "Never" : firstLogin)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLast Login: &r&e" + (lastLogin == null ? "Never" : lastLogin)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLogged in: &r&e" + loginCount + " times"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + lastUsername + " &r&7--&6*"));
        } else {
            sender.sendMessage("*-- " + lastUsername + " --*");
            sender.sendMessage("UUID: " + uuid);
            sender.sendMessage("Whitelisted: " + isWhitelisted);
            sender.sendMessage("Previous Usernames:");
            for (String viewPU : new ArrayList<>(get().getStringList(previousUsernames))) {
                sender.sendMessage("  - " + viewPU);
            }
            sender.sendMessage("Previous Addresses:");
            for (String viewPI : new ArrayList<>(get().getStringList(previousAddresses))) {
                sender.sendMessage("  - " + viewPI);
            }
            sender.sendMessage("First Login: " + (firstLogin == null ? "Never" : firstLogin));
            sender.sendMessage("Last Login: " + (lastLogin == null ? "Never" : lastLogin));
            sender.sendMessage("Logged in: " + loginCount + " times");
            sender.sendMessage("*-- " + lastUsername + " --*");
        }
    }

    public void recordOnLogin(String uuid, String playerName, String playerAddress) {
        registerPaths(uuid);
        if (!isPlayersPopulated()) {
            get().createSection("Players");
        }
        if (!doesRecordExist(uuid)) {
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
        if (!get().isSet(firstLogin)) {
            get().set(firstLogin, fDate.format(new Date()));
        }
        get().set(lastLogin, fDate.format(new Date()));
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
