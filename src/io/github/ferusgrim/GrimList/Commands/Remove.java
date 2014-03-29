package io.github.ferusgrim.GrimList.Commands;

import org.bukkit.command.CommandSender;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class Remove {

	public static boolean Start(CommandSender sender, String[] args) {
		boolean usernameIsValid = false;
		if(args.length < 2){
			sender.sendMessage("You forgot to enter a username!");
			return true;
		}
		usernameIsValid = checkUsernameValidity(args[1]);
		if(usernameIsValid){
			sender.sendMessage(args[1] + " was removed from the whitelist!");
			return true;
		}else{
			sender.sendMessage("Please enter a valid username!");
			return true;
		}
	}
	
	public static boolean checkUsernameValidity(String username){
		if(username.length() < 3 || username.length() > 16 || !username.matches("[a-zA-Z0-9_]")){
			return false;
		}
		return true;
	}
}
