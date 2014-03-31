package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class Add {
	private final static String Plugin = "[GrimList] ";
	
	public static boolean Start(CommandSender sender, String[] args) {
		if(args.length < 2){
			sender.sendMessage(Plugin + "You forgot to enter a username!");
			return true;
		}
		if(!(args[1].length() < 3) && !(args[1].length() > 16) && args[1].matches("[a-zA-Z0-9_]")){
			if(ConfigManager.useSQL) addUsingSQL(args[1], sender);
			if(ConfigManager.useFile) addUsingFile(args[1], sender);
			if(ConfigManager.useURL) addUsingURL(args[1], sender);
			sender.sendMessage(Plugin + args[1] + " was added to the whitelist!");
			return true;
		}else{
			sender.sendMessage(Plugin + " Please enter a valid username!");
			return true;
		}
	}
	
	public static void addUsingFile(String player, CommandSender sender){
	}
	
	public static void addUsingSQL(String player, CommandSender sender){
		if(ConfigManager.isPlayerInRecord(player, "mysql")){
			if(!ConfigManager.isPlayerActive(player, "mysql")){
				MakeActive(player, "mysql");
				return;
			}
		}
		ConfigManager.createPlayerRecord(player, "mysql");
		MakeActive(player, "mysql");
	}
	
	public static void addUsingURL(String player, CommandSender sender){
	}
	
	public static void MakeActive(String player, String type){
		if(type == "file"){
		}
		if(type == "mysql"){
			Connection conn = null;
			PreparedStatement ps = null;
			try{
				conn = ConfigManager.sqlConnection();
				ps = conn.prepareStatement(ConfigManager.QUERY_ALLOWPLAYER);
				ps.setString(1, player);
				ps.executeQuery();
			}catch(SQLException e){
				e.printStackTrace();
			}
			ConfigManager.CleanUpSQL(conn, ps);
		}
		if(type == "url"){
		}
	}
}
