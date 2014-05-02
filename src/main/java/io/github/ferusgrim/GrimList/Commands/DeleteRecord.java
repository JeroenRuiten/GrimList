/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.utils.AsyncThenSyncOperation;
import io.github.ferusgrim.GrimList.utils.UUIDFetcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class DeleteRecord {
    private final GrimList plugin;

    public DeleteRecord(GrimList plugin) {
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
                            plugin.filem.deleteRecord(uuid);
                            if (plugin.getConfig().getBoolean("LogUsage.Delete")) {
                                plugin.mysqlm.addCommandLog(name, uuid, "/whitelist delete " + name, (sender instanceof Player ? plugin.getServer().getPlayerExact(sender.getName()).getUniqueId().toString() : "CONSOLE"), sender.getName());
                            }
                            if (plugin.getConfig().getBoolean("KickRemove") && plugin.getServer().getPlayerExact(name) != null) {
                                plugin.getServer().getPlayerExact(name).kickPlayer(plugin.mStart + "You were removed from the whitelist!");
                            }
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record was deleted!");
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID: (" + uuid + ")");
                        }
                    } else {
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record doesn't exist!");
                    }
                    break;
                case "mysql":
                    String uuid = plugin.mysqlm.getUUID(name);
                    if (uuid.isEmpty()) {
                        runOperation(sender, name);
                    } else {
                        plugin.mysqlm.deletePlayerFromRecord(uuid);
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record was deleted!");
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID: (" + uuid + ")");
                    }
                    break;
            }
        }
        return true;
    }

    private void runOperation(CommandSender sender, String name) {
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
                        if (plugin.filem.doesRecordExist(uuid)) {
                            plugin.filem.deleteRecord(uuid);
                            if (plugin.getConfig().getBoolean("LogUsage.Delete")) {
                                plugin.mysqlm.addCommandLog(name, uuid, "/whitelist delete " + name, (sender instanceof Player ? plugin.getServer().getPlayerExact(sender.getName()).getUniqueId().toString() : "CONSOLE"), sender.getName());
                            }
                            if (plugin.getConfig().getBoolean("KickRemove") && plugin.getServer().getPlayerExact(name) != null) {
                                plugin.getServer().getPlayerExact(name).kickPlayer(plugin.mStart + "You were removed from the whitelist!");
                            }
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record was deleted!");
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID: (" + uuid + ")");
                        } else {
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record doesn't exist!");
                        }
                        break;
                    case "mysql":
                        if (plugin.mysqlm.doesRecordExist(uuid)) {
                            plugin.mysqlm.deletePlayerFromRecord(uuid);
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record was deleted!");
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID: (" + uuid + ")");
                        } else {
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record doesn't exist!");
                        }
                }
            }
        };
    }
}
