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
		if(checkUsernameValidity(args[1])){
			if(ConfigManager.useSQL){
				if(ConfigManager.isPlayerInRecord(args[1])){
					if(!ConfigManager.isPlayerActive(args[1])){
						MakeActive(args[1]);
					}else{
						sender.sendMessage(Plugin + args[1] + " is already whitelisted!");
						return true;
					}
				}else{
					ConfigManager.createPlayerRecord(args[1]);
					MakeActive(args[1]);
				}
			}
			sender.sendMessage(Plugin + args[1] + " was added to the whitelist!");
			return true;
		}else{
			sender.sendMessage(Plugin + "Please enter a valid username!");
			return true;
		}
	}
	
	public static void MakeActive(String player){
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
		ConfigManager.CleanUp(conn, ps);
	}
	
	
	public static boolean checkUsernameValidity(String username){
		if(username.length() < 3 || username.length() > 16 || !username.matches("[a-zA-Z0-9_]")){
			return false;
		}
		return true;
	}
}
