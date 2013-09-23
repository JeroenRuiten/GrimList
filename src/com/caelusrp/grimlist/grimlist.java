// Copyright Nicholas Badger (A.K.A. FerusGrim) //
// Attribution 3.0 Unported (CC BY 3.0) //

package com.caelusrp.grimlist;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class grimlist extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");

	static String maindir = "plugins/GrimList/";
	public static YamlConfiguration Settings;
	static File SettingsFile = new File(maindir + "config.yml");
	static ArrayList<String> WhiteListedPlayers = new ArrayList<String>();
	int RefreshWhitelistTaskID = -1;
	static boolean WhitelistON = true;

	public void onDisable() {
		//Clears the whitelist array to clear up memory
		WhiteListedPlayers = new ArrayList<String>();
		//Removes the config to clear up memory
		Settings = null;

		this.getServer().getScheduler().cancelAllTasks();
		RefreshWhitelistTaskID = -1;
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {
		new File(maindir).mkdir();
		//Checks if file exists
		if (!SettingsFile.exists()) {
			//Creates the config file if it doesn't exist
			try {
				SettingsFile.createNewFile();
				Settings = config.loadMain(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//If it exists load the config
			Settings = config.loadMain(false);
		}
		// Sets the whitelists mode
		WhitelistON = grimlist.Settings.getBoolean("grimlist.enabled");

		PluginManager pm = getServer().getPluginManager();

		//Registers the Listener class
		pm.registerEvents(new listener(), this);

		log.log(Level.INFO, "§e[GL]§3 GrimList has been" + (WhitelistON == true ? "§c activated§3." : "§c off§3."));
		DebugPrint("§8You're seeing debugging messages! To deactivate these, set \"debug\" to 'false'.");
		RefreshWhitelist(true);
		if(RefreshWhitelistTaskID < 0){
			RefreshWhitelistTaskID = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
				public void run() {
					RefreshWhitelist(false);
				}
			}, 0, Settings.getInt("grimlist.file.update-interval") * 20);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
		String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		String[] trimmedArgs = args;

		if (commandName.equals("whitelist")) {
			return CommandHandler(sender, trimmedArgs);
		}
		return false;
	}

	public static void SendMessage(CommandSender Player, String MSG){
		Player.sendMessage("§e[GL] §3" + MSG);
	}

	public boolean CommandHandler(CommandSender sender, String[] trimmedArgs) {
		if (trimmedArgs.length > 0) {
			String[] args = RearangeString(1, trimmedArgs);
			String CommandName = trimmedArgs[0];
			if (CommandName.equals("add")) {
				return AddPlayerToWhitelist(sender, args);
			}
			if (CommandName.equals("remove")) {
				return RemovePlayerFromWhitelist(sender, args);
			}
			if (CommandName.equals("reload")) {
				return ReloadPlugin(sender, args);
			}
			if (CommandName.equals("refresh")) {
				return RefreshWhitelist(sender, args);
			}
			if (CommandName.equals("on")) {
				return WhitelistOn(sender, args);
			}
			if (CommandName.equals("off")) {
				return WhitelistOff(sender, args);
			}
		}
		PrintHelper(sender);
		return true;
	}

	private boolean WhitelistOn(CommandSender sender, String[] args){
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.toggle"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			WhitelistON = true;
			log.log(Level.INFO, "§e[GL] §3"
					+ (player == null ? "A console administrator" : player.getName())
					+ " has §a activated §3GrimList!");
			sender.sendMessage("§e[GL] §3Grimlist is now §aactive§3!");
			return true;
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	private boolean WhitelistOff(CommandSender sender, String[] args){
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.toggle"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			WhitelistON = false;
			log.log(Level.INFO, "§e[GL] §3"
					+ (player == null ? "A console administrator" : player.getName())
					+ " has §c deactivated §3GrimList!");
			sender.sendMessage("§e[GL] §3Grimlist has now been§c deactived§3!");
			return true;
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	private boolean RefreshWhitelist(CommandSender sender, String[] args){
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.refresh"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			log.log(Level.INFO, "§e[GL] §3"
					+ (player == null ? "A console administrator" : player.getName())
					+ " has refreshed GrimList!");
			sender.sendMessage("§e[GL] §3Grimlist has been refreshed!");
			(new UpdateWhitelist(false)).start();
			return true;
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	private void PrintHelper(CommandSender sender){
		sender.sendMessage("§7== §8GrimList");
		sender.sendMessage("§7== > §a/whitelist §c[on/off]");
		sender.sendMessage("§7== > §a/whitelist §badd §c[player]");
		sender.sendMessage("§7== > §a/whitelist §bremove §c[player]");
		sender.sendMessage("§7== > §a/whitelist §brefresh");
		sender.sendMessage("§7== > §a/whitelist §breload");
	}

	private boolean ReloadPlugin(CommandSender sender, String[] args){
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.reload"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			onDisable();
			onEnable();
			log.log(Level.INFO, "§e[GL] §3"
					+ (player == null ? "A console administrator" : player.getName())
					+ " reloaded the GrimList configuration!");
			sender.sendMessage("§e[GL] §3Grimlist has been reloaded!");
			return true;
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	private ArrayList<String> GetWhitelist(String Type){
		ArrayList<String> tmpArray = new ArrayList<String>();
		if(Type.equals("file")){
			FileInputStream in;
			try {
				in = new FileInputStream(grimlist.Settings.getString("grimlist.file.name"));

				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					tmpArray.add(strLine.toLowerCase());
				}
				DebugPrint("Whitelist (type:" + Type +" count: " + tmpArray.toArray().length + ")");
				in.close();
				return tmpArray;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(Type.equals("mysql")){
				Connection conn = connector.getSQLConnection();
				if (conn == null) {
					log.log(Level.SEVERE,
							"§e[GL-001] §cGrimlist could not establish a SQL connection!");
					return null;
				} else {

					PreparedStatement ps = null;
					ResultSet rs = null;
					try {
						String Query = grimlist.Settings.getString("grimlist.mysql.query");
						Query = Query.replace("{table}",
								grimlist.Settings.getString("grimlist.mysql.table"));
						Query = Query.replace("{field}",
								grimlist.Settings.getString("grimlist.mysql.field"));
						Query = Query.replace("{time}", "" + GetTime());
						ps = conn.prepareStatement(Query);
						rs = ps.executeQuery();
						while (rs.next()) {
							tmpArray.add(rs.getString(
									grimlist.Settings.getString("grimlist.mysql.field")).toLowerCase());
						}
						DebugPrint("Whitelist (type:" + Type +" count: " + tmpArray.toArray().length + ")");
						return tmpArray;
					} catch (SQLException ex) {
						log.log(Level.SEVERE,
								"§e[GL-002] §cSQL statement couldn't be executed!: ",
								ex);
					} finally {
						try {
							if (ps != null)
								ps.close();
							if (conn != null)
								conn.close();
						} catch (SQLException ex) {
							log.log(Level.SEVERE,
									"§e[GL-003] §cFailed to close connection to database!: ",
									ex);
						}
					}

					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		}else{
			grimlist.log.log(Level.SEVERE,"§e[GL-004] §cThe connection type you've chosen, \"§e" + grimlist.Settings.getString("grimlist.connection") + "§c\" doesn't exist!");
		}
		return null;
	}

	private boolean AddPlayerToWhitelist(CommandSender sender, String[] args) {
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.add"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			if (args.length > 0) {
				String p = args[0];

				if (grimlist.WhiteListedPlayers.contains(p
						.toLowerCase())) {
					sender.sendMessage("§e[GL] §c The player, '§e" + p + "§c' is already whitelisted!");
					return true;
				}

				grimlist.WhiteListedPlayers.add(p.toLowerCase());
				String ConType = grimlist.Settings.getString("grimlist.connection");
				if(ConType.equals("file")){
						try{
							BufferedWriter fW = new BufferedWriter(new FileWriter(grimlist.Settings.getString("grimlist.file.name")));
							for(int i = 0; i< grimlist.WhiteListedPlayers.size(); i = i + 1){
								fW.write(grimlist.WhiteListedPlayers.get(i));
								fW.newLine();
							}
						    fW.close();
						}catch (Exception e){
							e.printStackTrace();
						}
						sender.sendMessage("§e[GL] §e" + p + " §a has been whitelisted!");
						return true;
				}else if(ConType.equals("mysql")){
						Connection conn = null;
						PreparedStatement ps = null;
						try {
							conn = connector.getSQLConnection();
							ps = conn.prepareStatement("INSERT INTO "
									+ Settings.getString("grimlist.mysql.table") + " (" + Settings.getString("grimlist.mysql.field") + ") VALUES(?)");
							ps.setString(1, p);
							ps.executeUpdate();
						} catch (SQLException ex) {
							log.log(Level.SEVERE,
									"§e[GL-005] §cSQL statement couldn't be executed!: ",
									ex);
						} finally {
							try {
								if (ps != null)
									ps.close();
								if (conn != null)
									conn.close();
							} catch (SQLException ex) {
								log.log(Level.SEVERE,
										"§e[GL-006] §cFailed to close connection to database!: ",
										ex);
							}
						}

						log.log(Level.INFO, "§e[GL] §3"
								+ (player == null ? "A console administrator" : player.getName())
								+ " whitelisted §e" + p + "§3!");
						sender.sendMessage("§e[GL] " + p + "§3 was added to the whitelist!");
						return true;
				}else{
					grimlist.log.log(Level.SEVERE,"§e[GL-007] §cThe connection type you've chosen, \"§e" + grimlist.Settings.getString("grimlist.connection") + "§c\" doesn't exist!");
				}
			}
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	private boolean RemovePlayerFromWhitelist(CommandSender sender, String[] args) {
		boolean auth = false;
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (player.hasPermission("grimlist.remove"))
				auth = true;
		} else {
			auth = true;
		}
		if (auth) {
			if (args.length > 0) {
				String p = args[0];
				Player victim = this.getServer().getPlayer(p);

				if (!grimlist.WhiteListedPlayers.contains(p
						.toLowerCase())) {
					sender.sendMessage("");
					return true;
				}

				if (victim != null) {
					p = victim.getName();
				}

				grimlist.WhiteListedPlayers.remove(p
						.toLowerCase());
				String ConType = grimlist.Settings.getString("grimlist.connection");
				if(ConType.equals("file")){
					try{
						BufferedWriter fW = new BufferedWriter(new FileWriter(grimlist.Settings.getString("grimlist.file.name")));
						for(int i = 0; i< grimlist.WhiteListedPlayers.size(); i = i + 1){
							fW.write(grimlist.WhiteListedPlayers.get(i));
							fW.newLine();
						}
					    fW.close();
					}catch (Exception e){
						e.printStackTrace();
					}
				}else if(ConType.equals("mysql")){
					Connection conn = null;
					PreparedStatement ps = null;
					try {
						conn = connector.getSQLConnection();
						ps = conn.prepareStatement("DELETE FROM "
								+ Settings.getString("grimlist.mysql.table") + " WHERE " + Settings.getString("grimlist.mysql.field") + " = ?");
						ps.setString(1, p);
						ps.executeUpdate();
					} catch (SQLException ex) {
						log.log(Level.SEVERE,
								"§e[GL-008] §cSQL statement couldn't be executed!: ",
								ex);
					} finally {
						try {
							if (ps != null)
								ps.close();
							if (conn != null)
								conn.close();
						} catch (SQLException ex) {
							log.log(Level.SEVERE,
									"§e[GL-009] §cFailed to close connection to database!: ",
									ex);
						}
					}
				}else{
					grimlist.log.log(Level.SEVERE,"§e[GL-010] §cThe connection type you've chosen, \"§e" + grimlist.Settings.getString("grimlist.connection") + "§c\" doesn't exist!");
					return true;
				}
				log.log(Level.INFO, "§e[GL] §3"
						+ (player == null ? "A console administrator" : player.getName())
						+ " removed §e" + p + "§3from the whitelist!");
				sender.sendMessage("§e[GL] " + p + "§3 was removed from the whitelist!");
				return true;
			}
		}
		sender.sendMessage("§e[GL] §cAccess to GrimList has been denied!");
		return true;
	}

	public static void DebugPrint(String MSG) {
		if (Settings.getBoolean("grimlist.debug")) {
			log.log(Level.INFO, "§e[GL-debug] §7" + MSG);
		}
	}

	public static String[] RearangeString(int startIndex, String[] string) {
		String TMPString = "";
		for (int i = startIndex; i < string.length; i++) {
			String Add = " ";
			if (i == startIndex) {
				Add = "";
			}
			TMPString += Add + string[i];
		}
		return TMPString.split("\\ ");
	}

	public void RefreshWhitelist(Boolean First) {
		new UpdateWhitelist(First).run();
	}

	public long GetTime() {
		return System.currentTimeMillis() / 1000L;
	}

	class UpdateWhitelist extends Thread {

		private Boolean First;

		public UpdateWhitelist(Boolean First) {
			this.First = First;
		}

		public void run() {
			String ConType = grimlist.Settings.getString("grimlist.connection");
			if(ConType.equals("file") || ConType.equals("mysql")){
				ArrayList<String> TmpArray = new ArrayList<String>();
				TmpArray = GetWhitelist(ConType);
				if(!TmpArray.equals(null)){
					if(First){
						log.log(Level.INFO, "§e[GL] §3Whitelist loaded successfully!");
					}
					grimlist.WhiteListedPlayers = TmpArray;
				}
				TmpArray = null;
				return;
			}
			grimlist.log.log(Level.SEVERE,"§e[GL-011] §cThe connection type you've chosen, \"§e" + grimlist.Settings.getString("grimlist.connection") + "§c\" doesn't exist!");
		}
	}
}