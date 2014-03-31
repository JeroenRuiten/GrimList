package io.github.ferusgrim.GrimList;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public final class GrimList extends JavaPlugin {
	private static final Logger toLog = Logger.getLogger("Minecraft");
	public final String pName = "[GrimList] ";
	public final File jarFile = this.getFile();
	
	@Override
	public void onEnable(){
		ConfigManager.Start(this);
		MetricsManager.Start(this);
		UpdateManager.Start(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerManager(this), this);
		getCommand("whitelist").setExecutor(new Executor());
	}
	
	@Override
	public void onDisable(){
		ConfigManager.Config = null;
	}
	
	public static void toLog(int msgLevel, String MSG){
		if(msgLevel == 4){
			if(ConfigManager.dLevel== 4){
				toLog.log(Level.INFO, MSG);
			}
		}else if(msgLevel == 3){
			if(ConfigManager.dLevel == 3){
				toLog.log(Level.INFO, MSG);
			}
		}else if(msgLevel == 2){
			if(ConfigManager.dLevel == 2){
				toLog.log(Level.WARNING, MSG);
			}
		}else if(msgLevel == 1){
			if(ConfigManager.dLevel == 1){
				toLog.log(Level.SEVERE, MSG);
			}
		}
	}
}
