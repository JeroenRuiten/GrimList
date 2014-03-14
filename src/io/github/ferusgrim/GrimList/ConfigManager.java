package io.github.ferusgrim.GrimList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	private static final String mDirString = "plugins/GrimList/";
	public static final File mDir = new File(mDirString);
	private static File ConfigFile = new File(mDir.getAbsolutePath() + File.separator + "config.yml");
	public static YamlConfiguration Config;
	private static File playerFile;
	
	public static YamlConfiguration loadConfig(boolean inputNewConfiguration) {
		try {
			Config = new YamlConfiguration();
			if(!inputNewConfiguration){
				Config.load(ConfigFile);
			}
			DefaultConfig("GrimList.Enabled", true);
			DefaultConfig("GrimList.Debug-Level", 3);
			DefaultConfig("GrimList.Updater.Notify-Update", true);
			DefaultConfig("GrimList.Updater.Verbose-Update", false);
			DefaultConfig("GrimList.Use.MySQL", false);
			DefaultConfig("GrimList.Use.File", true);
			DefaultConfig("GrimList.Use.URL", false);
			DefaultConfig("GrimList.MySQL.Host", "localhost");
			DefaultConfig("GrimList.MySQL.Port", 3306);
			DefaultConfig("GrimList.MySQL.Database", "whitelist");
			DefaultConfig("GrimList.MySQL.Username", "root");
			DefaultConfig("GrimList.MySQL.Password", "toor");
			DefaultConfig("GrimList.File.Name", "players.txt");
			DefaultConfig("GrimList.File.URL", "");
			Config.save(ConfigFile);
			return Config;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidConfigurationException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static void DefaultConfig(String Path, Object Value) {
		Config.set(Path, Config.get(Path, Value));
	}
	
	public static void Start() {
		if(!mDir.exists()){
			mDir.mkdir();
		}
		ConfigFile = new File(mDir.getAbsolutePath() + File.separator + "config.yml");
		if(ConfigFile.exists()){
			Config = loadConfig(false);
		}else try{
			ConfigFile.createNewFile();
			Config = loadConfig(true);
		}catch(IOException e){
			e.printStackTrace();
		}
		SetVariables();
		playerFile = new File(mDir.getAbsolutePath() + File.separator + filesource);
		if(!playerFile.exists()) try{
			playerFile.createNewFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static boolean glEnabled;
	public static int debuglevel;
	
	public static String filesource;
	public static String urlsource;
	
	public static String sqlhost;
	public static int sqlport;
	public static String sqldatabase;
	public static String sqlusername;
	public static String sqlpassword;
	
	public static void SetVariables(){
		glEnabled = Config.getBoolean("GrimList.Enabled");
		debuglevel = Config.getInt("GrimList.Debug-Level");
		filesource = Config.getString("GrimList.File.Name");
		urlsource = Config.getString("GrimList.File.URL");
		sqlhost = Config.getString("GrimList.MySQL.Host");
		sqlport = Config.getInt("GrimList.MySQL.Port");
		sqldatabase = Config.getString("GrimList.MySQL.Database");
		sqlusername = Config.getString("GrimList.MySQL.Username");
		sqlpassword = Config.getString("GrimList.MySQL.Password");
	}
}
