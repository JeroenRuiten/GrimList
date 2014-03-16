package io.github.ferusgrim.GrimList;

import net.gravitydevelopment.updater.Updater;

import org.bukkit.plugin.PluginDescriptionFile;

public class UpdateManager {
	public static final int ID = 65828;
	public static boolean update = false;
	public static String name = "";
	public static String version = "";

	public static void Start(GrimList plugin) {
		Updater updater = new Updater(plugin, ID, plugin.jarFile, Updater.UpdateType.NO_DOWNLOAD, false);
		PluginDescriptionFile descFile = plugin.getDescription();
		update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
		name = updater.getLatestName();
		version = name.replace("GrimList ", "");
		if(update){
			GrimList.toLog(3, "[GrimList] Update Available!");
			GrimList.toLog(3, "[GrimList] Current: v" + descFile.getVersion());
			GrimList.toLog(3, "[GrimList] Newest: " + version);
		}
	}
}
