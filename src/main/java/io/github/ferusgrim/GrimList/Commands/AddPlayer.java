/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.utils.UUIDFetcher;
import io.github.ferusgrim.GrimList.utils.AsyncThenSyncOperation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.FocusManagers.FileManager;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class AddPlayer {
    private GrimList plugin;

    public AddPlayer(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name){
        switch(plugin.focusOn) {
            case "file":
                String uuid = plugin.filem.getUUID(name);
                if (uuid.isEmpty()) {
                    runOperation(sender, name);
                } else {
                    if (plugin.filem.alreadyOnWhitelist(uuid)) {
                        if (sender instanceof Player) {
                            sender.sendMessage(plugin.mStart + "'" + name + "' is already whitelisted!");
                        } else {
                            plugin.log("WARNING", "'" + name + "' is already whitelisted!");
                        }
                    } else {
                        plugin.filem.toggleIsWhitelisted(uuid, name);
                        if(sender instanceof Player){
                            sender.sendMessage(plugin.mStart + "'" + name + "' was added to the whitelist!");
                        }else{
                            plugin.log("INFO", "'" + name + "' was added to the whitelist!");
                        }
                    }
                }
                break;
        }
        return true;
    }

    private void runOperation(CommandSender sender, String name){
        new AsyncThenSyncOperation(plugin, true){
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try{
                    response = new UUIDFetcher(Arrays.asList(name.toLowerCase())).call();
                }catch(Exception e){
                    plugin.log("WARNING", "Exception while running UUIDFetcher!");
                    e.printStackTrace();
                }
            }

            @Override
            protected void execSyncThen() {
                if(response.get(name.toLowerCase()) == null){
                    if(sender instanceof Player){
                        sender.sendMessage(plugin.mStart + "UUID Query returned null! Invalid username?");
                    }else{
                        plugin.log("WARNING", "UUID Query returned null! Username might not exist.?");
                    }
                    return;
                }
                String uuid = response.get(name.toLowerCase()).toString();
                switch(plugin.focusOn) {
                    case "file":
                        if(plugin.filem.alreadyOnWhitelist(uuid)){
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "'" + name + "' is already whitelisted!");
                            }else{
                                plugin.log("WARNING", "'" + name + "' is already whitelisted!");
                            }
                        }else{
                            plugin.filem.toggleIsWhitelisted(uuid, name);
                            if(sender instanceof Player){
                                sender.sendMessage(plugin.mStart + "'" + name + "' was added to the whitelist!");
                            }else{
                                plugin.log("INFO", "'" + name + "' was added to the whitelist!");
                            }
                        }
                        break;
                }
            }
        };
    }
}
