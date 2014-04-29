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
    
    public PlayerManager(GrimList plugin){
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event){
        if(!plugin.getConfig().getBoolean("Whitelist")){
            return;
        }
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String playerName = player.getName();
        String playerAddress = event.getAddress().getHostAddress();
        event.setKickMessage(plugin.mStart + "You're not whitelisted!");
        switch(plugin.focusOn){
        case "file":
            plugin.filem.onLoginRecordUpdater(uuid, playerName, playerAddress);
            event.setResult(plugin.filem.alreadyOnWhitelist(uuid)? Result.ALLOWED : Result.KICK_WHITELIST);
            break;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!plugin.getConfig().getBoolean("Updater") || !plugin.isUpdateAvailable || !player.hasPermission("grimlist.update")){
            return;
        }
        player.sendMessage(plugin.mStart + plugin.version + " available!");
        player.sendMessage(plugin.mStart + "Download: " + plugin.link);
    }
}
