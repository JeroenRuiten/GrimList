package io.github.ferusgrim.GrimList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Executor implements CommandExecutor {
	private GrimList gl;
	
	public Executor(GrimList gl) {
		this.gl = gl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length > 2){
			gl.toLog("Whoops! Too many inputs!");
			return true;
		}
		if(args.length < 1 || args[0].equalsIgnoreCase("help")){
			return false;
		}
		if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")){
			Pattern p = Pattern.compile(".*\\W+.*");
			Matcher m = p.matcher(args[1]);
			if(args.length < 2){
				gl.toLog("You forgot to enter a username!");
				return true;
			}else if(args[1].length() < 3 || args[1].length() > 16 || m.find()){
				gl.toLog("Invalid username!");
				return true;
			}
			gl.toLog("Worked!");
			return true;
		}
		return false;
	}
}