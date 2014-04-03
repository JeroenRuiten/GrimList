package io.github.ferusgrim.GrimList;

import io.github.ferusgrim.GrimList.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class Executor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args[0].equals("on") || args[0].equals("off")) args[0] = "toggle";
		boolean hasPerm = false;
		if(!(sender instanceof Player)) hasPerm = true;
		if(!hasPerm){
			Player player = null;
			player = (Player) sender;
			if(player.hasPermission("grimlist." + args[0])) hasPerm = true; 
			if(!hasPerm) sender.sendMessage("Insufficient Permissions!"); return true;
		}
		if(args.length < 1 || args.length > 2){
			sender.sendMessage("Try '/whitelist help' for more information!"); 
			return false;
		}
		if(args[0].equals("add")) return Add.Start(sender, args);
		if(args[0].equals("remove")) return Remove.Start(sender, args);
		if(args[0].equals("delete")) return Delete.Start(sender, args);
		if(args[0].equals("reload")) return Reload.Start(sender);
		if(args[0].equals("refresh")) return Refresh.Start(sender);
		if(args[0].equals("toggle")) return Toggle.Start(sender);
		if(args[0].equals("help")) return Help.Start(sender);
		if(args[0].equals("update")) return Update.Start(sender, args);
		sender.sendMessage("Unknown Argument!");
		return false;
	}
}
