package io.github.ferusgrim.GrimList;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public final class GrimList extends JavaPlugin {
	
	public final File pluginDir = getDataFolder();
	
	@Override
	public void onEnable(){
		ConfigManager.Start(true);
		getCommand("whitelist").setExecutor(new Executor(this));
	}
	
	@Override
	public void onDisable(){
		ConfigManager.Start(false);
	}
	
	public void toLog(String MSG){
		getLogger().info(MSG);
	}
}
