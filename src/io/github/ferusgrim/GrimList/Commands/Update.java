package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.UpdateManager;

import org.bukkit.command.CommandSender;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class Update {
	static GrimList plugin;

	public static boolean Start(CommandSender sender, String[] args) {
		if(args.length < 2){
			if(!UpdateManager.update){
				sender.sendMessage("GrimList is currently running the latest version!");
			}else{
				sender.sendMessage("There is an update available!");
			}
		}
		return true;
	}

}
