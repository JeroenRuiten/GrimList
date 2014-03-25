package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.UpdateManager;

import org.bukkit.entity.Player;

/**
 * Copyright (C) 2014 FerusGrim
 * @author FerusGrim
 */

public class Update {
	static GrimList plugin;

	public static boolean Start(Player player, String[] args) {
		if(args.length < 2){
			if(!UpdateManager.update){
				plugin.toExecutor(player, "GrimList is currently running the latest version!");
			}else{
				plugin.toExecutor(player, "There is an update available!");
			}
		}
		return true;
	}

}
