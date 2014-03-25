package io.github.ferusgrim.GrimList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Copyright (C) 2014 FerusGrim
 * @author FerusGrim
 */

public class ConfigManager {
	private static final String mDirString = "plugins/GrimList/";
	public static final File mDir = new File(mDirString);
	public static YamlConfiguration Config;
	public static YamlConfiguration PlayerData;
	private static File ConfigFile = new File(mDir.getAbsolutePath() + File.separator + "config.yml");
	private static File PlayerFile = new File(mDir.getAbsolutePath() + File.separator + "playerdata.yml");
	
	public static YamlConfiguration loadConfig(boolean inputNewConfiguration) {
		try {
			Config = new YamlConfiguration();
			if(!inputNewConfiguration){
				Config.load(ConfigFile);
			}
			DefaultConfig("GrimList.Enabled", true);
			DefaultConfig("GrimList.MotD", true);
			DefaultConfig("GrimList.Debug-Level", 3);
			DefaultConfig("GrimList.Updater.Enabled", true);
			DefaultConfig("GrimList.Updater.Notify", true);
			DefaultConfig("GrimList.Updater.Verbose", false);
			DefaultConfig("GrimList.Source.MySQL", false);
			DefaultConfig("GrimList.Source.File", true);
			DefaultConfig("GrimList.Source.URL", false);
			DefaultConfig("GrimList.MySQL.Host", "localhost");
			DefaultConfig("GrimList.MySQL.Port", 3306);
			DefaultConfig("GrimList.MySQL.Database", "whitelist");
			DefaultConfig("GrimList.MySQL.Username", "root");
			DefaultConfig("GrimList.MySQL.Password", "toor");
			DefaultConfig("GrimList.URL", "http://localhost:80/players.txt");
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
	
	private static YamlConfiguration loadPlayerData() {
		try{
			PlayerData = new YamlConfiguration();
			PlayerData.load(PlayerFile);
			return PlayerData;
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
	
	public static void AddPlayerData(String Path, String Value) {
		PlayerData.set(Path, Value);
		try {
			PlayerData.save(PlayerFile);
		}catch(IOException e){
			e.printStackTrace();
		}
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
		PlayerFile = new File(mDir.getAbsolutePath() + File.separator + "playerdata.yml");
		if(PlayerFile.exists()){
			PlayerData = loadPlayerData();
		}else try{
			PlayerFile.createNewFile();
			PlayerData = loadPlayerData();
		}catch(IOException e){
			e.printStackTrace();
		}
		ConfigureVariables();
	}
	
	public static boolean glEnabled;
	public static boolean motdEnabled;
	public static int dLevel;
	public static boolean upEnabled;
	public static boolean upNotify;
	public static boolean upVerbose;
	public static boolean useSQL;
	public static boolean useURL;
	public static boolean useFILE;
	public static String sqlHost;
	public static int sqlPort;
	public static String sqlDatabase;
	public static String sqlUsername;
	public static String sqlPassword;
	public static String stringURL;
	public static URL wlURL;
	public static void ConfigureVariables() {
		glEnabled = Config.getBoolean("GrimList.Enabled");
		motdEnabled = Config.getBoolean("GrimList.MotD");
		dLevel = Config.getInt("GrimList.Debug-Level");
		upEnabled = Config.getBoolean("GrimList.Updater.Enabled");
		upNotify = Config.getBoolean("GrimList.Updater.Notify");
		upVerbose = Config.getBoolean("GrimList.Updater.Verbose");
		useSQL = Config.getBoolean("GrimList.Source.MySQL");
		useURL = Config.getBoolean("GrimList.Source.URL");
		useFILE = Config.getBoolean("GrimList.Source.File");
		sqlHost = Config.getString("GrimList.MySQL.Host");
		sqlPort = Config.getInt("GrimList.MySQL.Port");
		sqlDatabase = Config.getString("GrimList.MySQL.Database");
		sqlUsername = Config.getString("GrimList.MySQL.Username");
		sqlPassword = Config.getString("GrimList.MySQL.Password");
		if(useURL){
			stringURL = Config.getString("GrimList.URL");
			try {
				wlURL = new URL(stringURL);
			}catch(MalformedURLException e){
				GrimList.toLog(1, "Invalid URL");
				useURL = false;
			}
		}
	}
}
