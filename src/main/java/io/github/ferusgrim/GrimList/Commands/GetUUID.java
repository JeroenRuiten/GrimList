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

public class GetUUID {
    private GrimList plugin;

    public GetUUID(GrimList plugin){
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name){
        switch(plugin.focusOn){
            case "file":
                String uuid = plugin.filem.getUUID(name);
                if(uuid.isEmpty()){
                    runOperation(sender, name);
                }else{
                    outputText(sender, uuid, name);
                }
                break;
        }
        return true;
    }

    public void outputText(CommandSender sender, String uuid, String name){
        if (sender instanceof Player){
            sender.sendMessage(plugin.mStart + "UUID of " + name + ":");
            sender.sendMessage(plugin.mStart + uuid);
        } else {
            plugin.log("INFO", "UUID of " + name + ":");
            plugin.log("INFO", uuid);
        }
    }

    public void runOperation(CommandSender sender, String name){
        if (sender instanceof Player) {
            sender.sendMessage(plugin.mStart + "Looking up UUID. This can take a moment...");
        } else {
            plugin.log("INFO", "Looking up UUID. This can take a moment...");
        }
        new AsyncThenSyncOperation(plugin, true){
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try{
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
                switch(plugin.focusOn){
                    case "file":
                        if (plugin.filem.recordExists(uuid)) {
                            outputText(sender, uuid, name);
                        } else {
                            if (!plugin.filem.recordExists(uuid) && plugin.getConfig().getBoolean("SaveQueries")) {
                                plugin.filem.newPlayerRecord(uuid, name);
                                outputText(sender, uuid, name);
                                return;
                            }
                        }
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "Player record not found!");
                        }else{
                            plugin.log("WARNING", "Player record not found!");
                        }
                        break;
                }
            }
        };
    }
}
