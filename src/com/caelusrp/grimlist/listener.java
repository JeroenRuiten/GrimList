// Copyright Nicholas Badger (A.K.A. FerusGrim) //
// Attribution 3.0 Unported (CC BY 3.0) //

package com.caelusrp.grimlist;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.entity.Player;

public class listener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event){
        if( grimlist.WhitelistON == true){
            Player player = event.getPlayer();
            //Checks if player is on the whitelist
            if(event.getResult() == Result.ALLOWED){
                if(!grimlist.WhiteListedPlayers.contains(player.getName().toLowerCase())){
                    //Kick message
                    String DisMSG = grimlist.Settings.getString("grimlist.notification.disallow-message")
                            .replace("{player}", player.getName());
                    event.setKickMessage(DisMSG);
                    event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);

                    grimlist.log.log(Level.INFO,"§e[GL]§3 the player '" + player.getName() + "' attempted to join, but was denied.");
                    //Check if print connection failures is enabled
                    if(grimlist.Settings.getBoolean("grimlist.notification.print-failure") == true){
                        Bukkit.getServer().broadcast("§e[GL] §3" + grimlist.Settings.getString("grimlist.notification.failure-message").replace("{player}", player.getName()), "grimlist.displayfails");
                    }
                }
            }else{
                grimlist.DebugPrint("§e[GL] \"" + player.getName() + "\" §3was already denied access by another plugin.");
            }
        }
    }
}