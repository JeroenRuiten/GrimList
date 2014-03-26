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
	public static String QUERY_CONNECTION;
	public static String QUERY_CREATEDATABASE;
	public static String QUERY_CREATETABLE;
	public static String QUERY_LOADWHITELIST;
	public static String QUERY_ADDPLAYER;
	public static String QUERY_REMOVEPLAYER;
	public static String QUERY_CHECKFORPLAYER;
	
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
		if(useURL){
			stringURL = Config.getString("GrimList.URL");
			try {
				wlURL = new URL(stringURL);
			}catch(MalformedURLException e){
				GrimList.toLog(1, "Invalid URL");
				useURL = false;
			}
		}
		if(useSQL) ConfigureSQL();
	}
	
	public static void ConfigureSQL() {
		boolean usepassword;
		int sqlPort;
		String sqlHost = "";
		String sqlDatabase = "";
		String sqlUsername = "";
		String sqlPassword = "";
		sqlHost = Config.getString("GrimList.MySQL.Host").isEmpty()? "localhost" : Config.getString("MySQL.Host");
		sqlPort = Config.getInt("MySQL.Port") == 0? 3306 : Config.getInt("MySQL.Port");
		sqlDatabase = Config.getString("MySQL.Database").isEmpty()? "whitelist" : Config.getString("MySQL.Database");
		sqlUsername = Config.getString("MySQL.Username").isEmpty()? "root" : Config.getString("MySQL.Username");
		usepassword = Config.getString("MySQL.Password").isEmpty()? false : true;
		QUERY_CONNECTION = "jdbc:mysql://" + sqlHost + ":" + sqlPort + "/" + sqlDatabase + "?user=" + sqlUsername;
		if(usepassword){
			sqlPassword = Config.getString("MySQL.Password");
			QUERY_CONNECTION = QUERY_CONNECTION + "&password=" + sqlPassword;
		}
		QUERY_CREATEDATABASE = "CREATE DATABASE IF NOT EXISTS `" + sqlDatabase + "` "
				+ "DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;";
		QUERY_CREATETABLE = "CREATE TABLE IF NOT EXISTS `playerdata` ("
				+ "`player` varchar(16) NOT NULL, "
				+ "`whitelisted_by` varchar(16) NOT NULL, "
				+ "`whitelisted_on` datetime NOT NULL, "
				+ "`last_login_ip` varchar(16) NOT NULL, "
				+ "`last_login_date` timestamp(4) NOT NULL DEFAULT CURRENT_TIMESTAMP(4) ON UPDATE_CURRENT_TIMESTAMP(4), "
				+ "`still_active` int(1) NOT NULL, "
				+ "`removed_by` varchar(16) NOT NULL, "
				+ "`removed_on` datetime NOT NULL, "
				+ "UNIQUE KEY `player` (`player`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		QUERY_LOADWHITELIST = "";
		QUERY_CHECKFORPLAYER = "";
		QUERY_ADDPLAYER = "INSERT INTO `" + sqlDatabase + "`.`playerdata` ("
				+ "`player`, "
				+ "`whitelisted_by`, "
				+ "`whitelisted_on`, "
				+ "`last_login_ip`, "
				+ "`last_login_date`, "
				+ "`still_active`, "
				+ "`removed_by`, "
				+ "`removed_on`) VALUES ("
				+ "'?', '?', CURRENT_TIME(), '', '', '1', '', '');";
		QUERY_REMOVEPLAYER = "UPDATE `" + sqlDatabase + "`.`playerdata`"
				+ "SET `still active` = '0',"
				+ "`removed_by` = '?',"
				+ "`removed_on` = CURRENT_TIME( )"
				+ "WHERE `playerdata`.`?`";
	}
}
