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

public class GetUUID {
    private final GrimList plugin;

    public GetUUID(GrimList plugin) {
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
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID of " + name + ":");
                            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + uuid);
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
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID of " + name + ":");
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + uuid);
                    }
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
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID of " + name + ":");
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + uuid);
                if (plugin.getConfig().getBoolean("SaveQueries")) {
                    switch (plugin.focusOn) {
                        case "file":
                            if (!plugin.filem.doesRecordExist(uuid)) {
                                plugin.filem.recordAfterIdLookup(uuid, name);
                            }
                            break;
                        case "mysql":
                            plugin.mysqlm.removePlayerFromWhitelist(uuid, name);
                            break;
                    }
                }
            }
        };
    }
}
