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
    private GrimList plugin;

    public DeleteRecord(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name) {
        switch(plugin.focusOn){
            case "file":
                String uuid = plugin.filem.getUUID(name);
                if(uuid.isEmpty()){
                    runOperation(sender, name);
                }else{
                    if(plugin.filem.recordExists(uuid)){
                        plugin.filem.deleteRecord(uuid);
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Deleted '" + name + "'s record.");
                            sender.sendMessage(plugin.mStart + "UUID: ('" + uuid + "')");
                        }else{
                            plugin.log("INFO", "Deleted '" + name + "'s record.");
                            plugin.log("INFO", "UUID: ('" + uuid + "')");
                        }
                    }else{
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "Player record not found!");
                        }else{
                            plugin.log("INFO", "Player record not found!");
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void runOperation(CommandSender sender, String name) {
        if (sender instanceof Player) {
            sender.sendMessage(plugin.mStart + "Looking up UUID. This can take a moment...");
        } else {
            plugin.log("INFO", "Looking up UUID. This can take a moment...");
        }
        new AsyncThenSyncOperation(plugin, true) {
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try {
                    response = new UUIDFetcher(Arrays.asList(name.toLowerCase())).call();
                } catch (Exception e) {
                    plugin.log("WARNING", "Exception while running UUIDFetcher!");
                    e.printStackTrace();
                }
            }

            @Override
            protected void execSyncThen() {
                if (response.get(name.toLowerCase()) == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage(plugin.mStart + "UUID Query returned null! Invalid username?");
                    } else {
                        plugin.log("WARNING", "UUID Query returned null! Username might not exist.?");
                    }
                    return;
                }
                String uuid = response.get(name.toLowerCase()).toString();
                switch (plugin.focusOn) {
                    case "file":
                        if(plugin.filem.recordExists(uuid)){
                            plugin.filem.deleteRecord(uuid);
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Deleted '" + name + "'s record.");
                                sender.sendMessage(plugin.mStart + "UUID: ('" + uuid + "')");
                            }else{
                                plugin.log("INFO", "Deleted '" + name + "'s record.");
                                plugin.log("INFO", "UUID: ('" + uuid + "')");
                            }
                        }else{
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "Player record not found!");
                            }else{
                                plugin.log("INFO", "Player record not found!");
                            }
                        }
                        break;
                }
            }
        };
    }
}
