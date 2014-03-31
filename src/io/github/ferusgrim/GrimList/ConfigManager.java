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
	
	public static void AddPlayerData(String Path, String Value) {
		PlayerData.set(Path, Value);
		try {
			PlayerData.save(PlayerFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void Start(GrimList plugin){
		if(!mDir.exists()){
			mDir.mkdir();
		}
		plugin.saveDefaultConfig();
		PlayerFile = new File(mDir.getAbsolutePath() + File.separator + "playerdata.yml");
		if(PlayerFile.exists()){
			PlayerData = loadPlayerData();
		}else try{
			PlayerFile.createNewFile();
			PlayerData = loadPlayerData();
		}catch(IOException e){
			e.printStackTrace();
		}
		ConfigureVariables(plugin);
	}
	
	public static boolean glEnabled;
	
	public static boolean wlEnabled;
	public static boolean motdEnabled;
	public static boolean updaterEnabled;
	public static boolean metricsEnabled;
	
	public static boolean upNotify;
	public static boolean upVerbose;
	
	public static boolean useFile;
	public static boolean useSQL;
	public static boolean useURL;
	
	public static String strURL;
	public static URL wlURL;
	
	public static int dLevel;
	
	public static void ConfigureVariables(GrimList plugin) {
		glEnabled = plugin.getConfig().getBoolean("GrimList");
		wlEnabled = plugin.getConfig().getBoolean("Features.Whitelist");
		motdEnabled = plugin.getConfig().getBoolean("Features.MotD");
		updaterEnabled = plugin.getConfig().getBoolean("Features.Updater");
		metricsEnabled = plugin.getConfig().getBoolean("Features.Metrics");
		upNotify = plugin.getConfig().getBoolean("Updater.Notify");
		upVerbose = plugin.getConfig().getBoolean("Updater.Verbose");
		useFile = plugin.getConfig().getBoolean("Sources.File");
		useSQL = plugin.getConfig().getBoolean("Sources.MySQL");
		useURL = plugin.getConfig().getBoolean("Sources.URL");
		dLevel = plugin.getConfig().getInt("Debug");
		if(useFile) ConfigureFile(plugin);
		if(useSQL) ConfigureSQL(plugin);
		if(useURL) ConfigureURL(plugin);
	}
	
	public static void ConfigureFile(GrimList plugin) {
		//TODO Put something here. >.>;; <.<;;
	}
	
	public static void ConfigureSQL(GrimList plugin) {
		boolean usepassword;
		int sqlPort;
		String sqlHost = "";
		String sqlDatabase = "";
		String sqlUsername = "";
		String sqlPassword = "";
		sqlHost = plugin.getConfig().getString("MySQL.Host").isEmpty()? "localhost" : plugin.getConfig().getString("MySQL.Host");
		sqlPort = plugin.getConfig().getInt("MySQL.Port") == 0? 3306 : plugin.getConfig().getInt("MySQL.Port");
		sqlDatabase = plugin.getConfig().getString("MySQL.Database").isEmpty()? "whitelist" : plugin.getConfig().getString("MySQL.Database");
		sqlUsername = plugin.getConfig().getString("MySQL.Username").isEmpty()? "root" : plugin.getConfig().getString("MySQL.Username");
		usepassword = plugin.getConfig().getString("MySQL.Password").isEmpty()? false : true;
		QUERY_CONNECTION = "jdbc:mysql://" + sqlHost + ":" + sqlPort + "/" + sqlDatabase + "?user=" + sqlUsername;
		if(usepassword){
			sqlPassword = plugin.getConfig().getString("MySQL.Password");
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
	
	public static void ConfigureURL(GrimList plugin) {
		strURL = plugin.getConfig().getString("URL");
		try {
			wlURL = new URL(strURL);
		}catch(MalformedURLException e){
			GrimList.toLog(1, "Invalid URL");
			useURL = false;
		}
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
