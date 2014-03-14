package io.github.ferusgrim.GrimList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerManager implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event){
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
		if(ConfigManager.Config.getBoolean("GrimList.Updater.Notify-Update")){
			Player player = event.getPlayer();
			if(player.hasPermission("grimlist.update-notify")){
				player.sendMessage("An update is available!: " + UpdateManager.name);
				player.sendMessage("Download this update!:" + UpdateManager.link);
				player.sendMessage("To update this plugin automatically, type '/whitelist update'");
			}
		}
	}
}
