/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package net.caelumcraft.GrimList.Commands;

import net.caelumcraft.GrimList.GrimList;
import net.caelumcraft.GrimList.utils.AsyncThenSyncOperation;
import net.caelumcraft.GrimList.utils.UUIDFetcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class AddPlayer {
    private final GrimList plugin;

    public AddPlayer(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name) {
        if (plugin.getConfig().getBoolean("AlwaysLookup")) {
            runOperation(sender, name);
        } else {
            switch (plugin.focusOn) {
                case "file":
                    if (plugin.filem.isPlayersPopulated()) {
                        String uuid = plugin.filem.getUUID(name);
                        if (uuid.isEmpty()) {
                            runOperation(sender, name);
                        } else {
                            if (plugin.filem.isPlayerWhitelisted(uuid)) {
                                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player is already whitelisted!");
                            } else {
                                plugin.filem.addPlayerToWhitelist(uuid, name);
                                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player was added to the whitelisted!");
                            }
                        }
                    } else {
                        runOperation(sender, name);
                    }
                    break;
                case "mysql":
                    String uuid = plugin.mysqlm.getUUID(name);
                    if (uuid.isEmpty()) {
                        runOperation(sender, name);
                    } else {
                        if (plugin.mysqlm.isPlayerWhitelisted(uuid)) {
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player is already whitelisted!");
                        } else {
                            plugin.mysqlm.addPlayerToWhitelist(uuid, name);
                            if (plugin.getConfig().getBoolean("LogUsage.Add")) {
                                plugin.mysqlm.addCommandLog(name, uuid, "/whitelist add " + name, (sender instanceof Player ? plugin.getServer().getPlayerExact(sender.getName()).getUniqueId().toString() : "CONSOLE"), sender.getName());
                            }
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player was added to the whitelist!");
                        }
                    }
                    break;
            }
        }
        return true;
    }

    private void runOperation(final CommandSender sender, final String name) {
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Looking up UUID. This can take a moment...");
        new AsyncThenSyncOperation(plugin, true) {
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try {
                    response = new UUIDFetcher(Arrays.asList(name.toLowerCase())).call();
                } catch (Exception e) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Error while looking up UUID. Check Logs.");
                    plugin.log("SEVERE", "UUID ERROR : STACK TRACE");
                    e.printStackTrace();
                }
            }

            @Override
            protected void execSyncThen() {
                if (response.get(name.toLowerCase()) == null) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID Query returned null! No user by this name?");
                    return;
                }
                String uuid = response.get(name.toLowerCase()).toString();
                switch (plugin.focusOn) {
                    case "file":
                        if (plugin.filem.isPlayerWhitelisted(uuid)) {
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player is already whitelisted!");
                        } else {
                            plugin.filem.addPlayerToWhitelist(uuid, name);
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player was added to the whitelisted!");
                        }
                        break;
                    case "mysql":
                        if (plugin.mysqlm.doesRecordExist(uuid)) {
                            if (plugin.mysqlm.isPlayerWhitelisted(uuid)) {
                                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player is already whitelisted!");
                            } else {
                                plugin.mysqlm.addPlayerToWhitelist(uuid, name);
                                if (plugin.getConfig().getBoolean("LogUsage.Add")) {
                                    plugin.mysqlm.addCommandLog(name, uuid, "/whitelist add " + name, (sender instanceof Player ? plugin.getServer().getPlayerExact(sender.getName()).getUniqueId().toString() : "CONSOLE"), sender.getName());
                                }
                                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player was added to the whitelist!");
                            }
                        } else {
                            plugin.mysqlm.addPlayerToWhitelist(uuid, name);
                            if (plugin.getConfig().getBoolean("LogUsage.Add")) {
                                plugin.mysqlm.addCommandLog(name, uuid, "/whitelist add " + name, (sender instanceof Player ? plugin.getServer().getPlayerExact(sender.getName()).getUniqueId().toString() : "CONSOLE"), sender.getName());
                            }
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player was added to the whitelist!");
                        }
                        break;
                }
            }
        };
    }
}
