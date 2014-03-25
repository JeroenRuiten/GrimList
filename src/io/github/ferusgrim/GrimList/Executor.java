package io.github.ferusgrim.GrimList;

import io.github.ferusgrim.GrimList.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Copyright (C) 2014 FerusGrim
 * @author FerusGrim
 */

public class Executor implements CommandExecutor {
	private GrimList plugin;

	public Executor(GrimList plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 1 || args.length > 2){
			sender.sendMessage("Try '/whitelist help' for more information!");
			return false;
		}
		if(args[0].equals("on") || args[0].equals("off")){
			args[0] = "toggle";
		}
		Player player = null;
		boolean hasPerm = false;
		if(sender instanceof Player){
			player = (Player) sender;
			if(player.hasPermission("grimlist." + args[0])){
				hasPerm = true;
			}else{
				hasPerm = false;
			}
		}else{
			hasPerm = true;
		}
		if(hasPerm){
			if(args[0].equals("add")){
				return Add.Start(sender, args);
			}else if(args[0].equals("remove")){
				return Remove.Start(sender, args);
			}else if(args[0].equals("reload")){
				return Reload.Start(sender);
			}else if(args[0].equals("refresh")){
				return Refresh.Start(sender);
			}else if(args[0].equals("toggle")){
				return Toggle.Start(sender);
			}else if(args[0].equals("help")){
				return Help.Start(sender);
			}else if(args[0].equals("update")){
				return Update.Start(player, args);
			}else{
				plugin.toExecutor(player, "Unknown Argument!");
				return false;
			}
		}
		plugin.toExecutor(player, "Insufficient Permissions!");
		return true;
	}
}