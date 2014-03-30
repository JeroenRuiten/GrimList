package io.github.ferusgrim.GrimList.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.github.ferusgrim.GrimList.ConfigManager;

import org.bukkit.command.CommandSender;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class Remove {
	private final static String Plugin = "[GrimList] ";

	public static boolean Start(CommandSender sender, String[] args) {
		if(args.length < 2){
			sender.sendMessage(Plugin + "You forgot to enter a username!");
			return true;
		}
		if(!(args[1].length() < 3) && !(args[1].length() > 16) && args[1].matches("[a-zA-Z0-9_]")){
			if(ConfigManager.useSQL){
				if(ConfigManager.isPlayerInRecord(args[1])){
					if(ConfigManager.isPlayerActive(args[1])){
						MakeInactive(args[1]);
					}else{
						sender.sendMessage(Plugin + args[1] + " isn't whitelisted!");
						return true;
					}
				}else{
					ConfigManager.createPlayerRecord(args[1]);
					MakeInactive(args[1]);
				}
			}
			sender.sendMessage(Plugin + args[1] + " was removed from the whitelist!");
			return true;
		}else{
			sender.sendMessage(Plugin + "Please enter a valid username!");
			return true;
		}
	}
	
	public static void MakeInactive(String player){
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = ConfigManager.sqlConnection();
			ps = conn.prepareStatement(ConfigManager.QUERY_DISALLOWPLAYER);
			ps.setString(1, player);
			ps.executeQuery();
		}catch(SQLException e){
			e.printStackTrace();
		}
		ConfigManager.CleanUp(conn, ps);
	}
}
