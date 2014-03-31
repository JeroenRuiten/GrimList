package io.github.ferusgrim.GrimList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Copyright (C) 2014 Nicholas Badger
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
	public static String QUERY_CREATETABLE_PLAYERDATA;
	public static String QUERY_CREATETABLE_OPERATIONLOGS;
	public static String QUERY_LOADWHITELIST;
	public static String QUERY_ALLOWPLAYER;
	public static String QUERY_DISALLOWPLAYER;
	public static String QUERY_CHECKFORPLAYER;
	public static String QUERY_CHECKFORACTIVE;
	public static String QUERY_ADDPLAYERRECORD;
	public static String QUERY_DELETEPLAYERRECORD;
	public static String QUERY_ADDLOG;
	public static YamlConfiguration loadConfig(boolean inputNewConfiguration) {
		try {
			Config = new YamlConfiguration();
			if(!inputNewConfiguration){
				Config.load(ConfigFile);
			}
			DefaultConfig("GrimList", true);
			DefaultConfig("Features.Whitelist", true);
			DefaultConfig("Features.MotD", true);
			DefaultConfig("Features.Updater", true);
			DefaultConfig("Features.Metrics", true);
			DefaultConfig("Updater.Notify", true);
			DefaultConfig("Updater.Verbose", false);
			DefaultConfig("Sources.File", true);
			DefaultConfig("Sources.MySQL", false);
			DefaultConfig("Sources.URL", false);
			DefaultConfig("MySQL.Host", "localhost");
			DefaultConfig("MySQL.Port", 3306);
			DefaultConfig("MySQL.Database", "whitelist");
			DefaultConfig("MySQL.Username", "root");
			DefaultConfig("MySQL.Password", "toor");
			DefaultConfig("URL", "http://localhost:80/players.txt");
			DefaultConfig("Debug", 3);
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
		QUERY_CREATETABLE_PLAYERDATA = "CREATE TABLE IF NOT EXISTS `playerdata` ("
				+ "`player` varchar(16) NOT NULL, "
				+ "`last_login_ip` varchar(16) NOT NULL, "
				+ "`last_login_date` timestamp(4) NOT NULL DEFAULT CURRENT_TIMESTAMP(4) ON UPDATE_CURRENT_TIMESTAMP(4), "
				+ "`still_active` int(1) NOT NULL, "
				+ "UNIQUE KEY `player` (`player`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		QUERY_CREATETABLE_OPERATIONLOGS = "CREATE TABLE IF NOT EXISTS `operation_logs` ("
				+ "`executor` varchar(16) NOT NULL, "
				+ "`operation` varchar(32) NOT NULL, "
				+ "`victim` varchar(16) NOT NULL, "
				+ "`timestamp` timestamp(4) NOT NULL DEFAULT CURRENT_TIMESTAMP(4) ON UPDATE CURRENT_TIMESTAMP(4)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		QUERY_CHECKFORPLAYER = "SELECT COUNT(*) FROM `playerdata` WHERE `player` = '?';";
		QUERY_CHECKFORACTIVE = "SELECT `still_active` FROM `playerdata` WHERE `player` = '?'";
		QUERY_ADDPLAYERRECORD = "INSERT INTO `" + sqlDatabase + "`.`playerdata` ("
				+ "`player`, `last_login_date`) VALUES ("
				+ "'?', '0000');";
		QUERY_DELETEPLAYERRECORD = "DELETE FROM `" + sqlDatabase + "`.`playerdata`` "
				+ "WHERE `playerdata`.`player` = '?';";
		QUERY_ALLOWPLAYER = "UPDATE `" + sqlDatabase + "`.`playerdata`"
				+ "SET `still_active` = '1', "
				+ "WHERE `playerdata`.`player` = '?';";
		QUERY_DISALLOWPLAYER = "UPDATE `" + sqlDatabase + "`.`playerdata` "
				+ "SET `still_active` = '0' "
				+ "WHERE `playerdata`.`player` = '?';";
		QUERY_ADDLOG = "INSERT INTO `" + sqlDatabase + "`.`operation_logs` ("
				+ "`executor`, "
				+ "`operation`, "
				+ "`victim`, "
				+ "`timestamp`) VALUES ("
				+ "'?', '?', '?', TIMESTAMP('CURRENT_TIMESTAMP(4)'));";
	}
	
	public static boolean isPlayerInRecord(String player, String type){
		if(type == "file"){
		}
		if(type == "mysql"){
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = sqlConnection();
				ps = conn.prepareStatement(QUERY_CHECKFORPLAYER);
				ps.setString(1, player);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					CleanUpSQL(conn, ps);
					return true;
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
			CleanUpSQL(conn, ps);
		}
		if(type == "url"){
		}
		return false;
	}
	
	public static boolean isPlayerActive(String player, String type){
		if(type == "file"){
		}
		if(type == "mysql"){
			Connection conn = null;
			PreparedStatement ps = null;
			try{
				conn = sqlConnection();
				ps = conn.prepareStatement(QUERY_CHECKFORACTIVE);
				ps.setString(1, player);
				ResultSet rs = ps.executeQuery();
				rs.next();
				if(rs.getInt("still_active") == 1){
					CleanUpSQL(conn, ps);
					return true;
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
			CleanUpSQL(conn, ps);
		}
		if(type == "url"){
		}
		return false;
	}
	
	public static Connection sqlConnection() {
		try{
			return DriverManager.getConnection(QUERY_CONNECTION);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void createPlayerRecord(String player, String type){
		if(type == "file"){
		}
		if(type == "mysql"){
			Connection conn = null;
			PreparedStatement ps = null;
			try{
				conn = sqlConnection();
				ps = conn.prepareStatement(QUERY_ADDPLAYERRECORD);
				ps.setString(1, player);
				ps.executeQuery();
			}catch(SQLException e){
				e.printStackTrace();
			}
			CleanUpSQL(conn, ps);
		}
		if(type == "url"){
		}
	}
	
	public static void CleanUpSQL(Connection conn, PreparedStatement ps){
		try{
			if(ps != null) ps.close();
			if(conn != null) conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
