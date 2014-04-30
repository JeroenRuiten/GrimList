/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class PlayerManager implements Listener {
    private GrimList plugin;

    public PlayerManager(GrimList plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!plugin.getConfig().getBoolean("Whitelist")) {
            return;
        }
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String playerName = player.getName();
        String playerAddress = event.getAddress().getHostAddress();
        boolean denyUser = false;
        switch (plugin.focusOn) {
            case "file":
                plugin.filem.recordOnLogin(uuid, playerName, playerAddress);
                denyUser = !plugin.filem.isPlayerWhitelisted(uuid);
                break;
        }
        if (denyUser) {
            event.setKickMessage(plugin.mStart + "You're not whitelisted!");
            event.setResult(Result.KICK_WHITELIST);
            if (plugin.getConfig().getBoolean("Notify.Console")) {
                plugin.log("INFO", "User was denied access!: " + playerName);
            }
            if (plugin.getConfig().getBoolean("Notify.Player")) {
                plugin.getServer().broadcast(plugin.mStart + "User was denied access!: " + playerName, "grimlist.notify");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfig().getBoolean("Updater") || !plugin.isUpdateAvailable || !player.hasPermission("grimlist.update")) {
            return;
        }
        player.sendMessage(plugin.mStart + plugin.version + " available!");
        player.sendMessage(plugin.mStart + "Download: " + plugin.link);
    }
}
