package io.github.ferusgrim.GrimList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class PlayerManager implements Listener {
	private GrimList plugin;
	
	public PlayerManager(GrimList plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event){
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
		PluginDescriptionFile descFile = plugin.getDescription();
		if(ConfigManager.Config.getBoolean("GrimList.Updater.Notify-Update")){
			Player player = event.getPlayer();
			if(player.hasPermission("grimlist.update-notify") && UpdateManager.update){
				player.sendMessage("[GrimList - Update Available!]");
				player.sendMessage("Current: v" + descFile.getVersion());
				player.sendMessage("Newest: " + UpdateManager.version);
			}
		}
	}
}
