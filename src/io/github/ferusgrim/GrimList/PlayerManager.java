package io.github.ferusgrim.GrimList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class PlayerManager implements Listener {
	private GrimList plugin;
	
	public PlayerManager(GrimList plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event){
		if(ConfigManager.Config.getBoolean("GrimList.Enabled")){
			if(event.getResult() == Result.ALLOWED){
				Player player = event.getPlayer();
				if(playerIsWhitelisted(player)){
					//TODO: Actions for disallowing player who isn't whitelisted.
				}
			}
		}
	}
	
	public boolean playerIsWhitelisted(Player player){
		boolean PlayerIsInRecord = false;
		boolean playerIsActive = false;
		if(ConfigManager.useFile) if(!PlayerIsInRecord) ConfigManager.isPlayerInRecord(player.getName());
		if(ConfigManager.useSQL) if(!PlayerIsInRecord) ConfigManager.isPlayerInRecord(player.getName());
		if(ConfigManager.useURL) if(!PlayerIsInRecord) ConfigManager.isPlayerInRecord(player.getName());
		if(PlayerIsInRecord){
			if(ConfigManager.useFile) if(!playerIsActive) ConfigManager.isPlayerActive(player.getName());
			if(ConfigManager.useSQL) if(!playerIsActive) ConfigManager.isPlayerActive(player.getName());
			if(ConfigManager.useURL) if(!playerIsActive) ConfigManager.isPlayerActive(player.getName());
		}
		if(playerIsActive) return true;
		return false;
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
		if(ConfigManager.Config.getBoolean("GrimList.MotD")){
			Player player = event.getPlayer();
			String playerIP = player.getAddress().getAddress().toString();
			playerIP = playerIP.replaceAll("/", "");
			playerIP = playerIP.replaceAll("\\.", "-");
			if(ConfigManager.PlayerData.getString(player.getName() + ".IP").isEmpty()) 
			while(ConfigManager.PlayerData.getString(player.getName() + ".IP").contains(playerIP)){
			}
		}
	}	
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onServerListPing(final ServerListPingEvent event){
		/*if(ConfigManager.Config.getBoolean("GrimList.MotD")){
			String playerIP = event.getAddress().toString();
			playerIP = playerIP.replaceAll("/", "");
			playerIP = playerIP.replaceAll("\\.", "-");
			boolean userHasPlayed = ConfigManager.PlayerData.contains(playerIP);
			if(userHasPlayed){
				String playerName = ConfigManager.PlayerData.getString(playerIP);
				event.setMotd(playerName + ": YOU ARE WHITELISTED!");
			}
			for(String parent : ConfigManager.PlayerData.getConfigurationSection(playerIP).getKeys(false)){
				if(ConfigManager.PlayerData.getString(parent) == playerIP){
					event.setMotd(parent + ": YOU ARE WHITELISTED!");
				}
			}
		}*/
	}
}
