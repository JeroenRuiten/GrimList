package io.github.ferusgrim.GrimList.Commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

/**
 * Copyright (C) 2014 FerusGrim
 * @author FerusGrim
 */

public class Add {

	public static boolean Start(CommandSender sender, String[] args) {
		boolean usernameIsValid = false;
		if(args.length < 2){
			sender.sendMessage("You forgot to enter a username!");
			return true;
		}
		usernameIsValid = checkUsernameValidity(args[1]);
		if(usernameIsValid){
			sender.sendMessage(args[1] + " was added to the whitelist!");
			return true;
		}else{
			sender.sendMessage("Please enter a valid username!");
			return true;
		}
	}
	
	public static boolean checkUsernameValidity(String username){
		Pattern p = Pattern.compile(".*\\W+.*");
		Matcher m = p.matcher(username);
		if(username.length() < 3 || username.length() > 16 || m.find()){
			return false;
		}
		return true;
	}
}
