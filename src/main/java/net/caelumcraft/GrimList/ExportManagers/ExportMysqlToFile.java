/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package net.caelumcraft.GrimList.ExportManagers;

import net.caelumcraft.GrimList.FocusManagers.FileManager;
import net.caelumcraft.GrimList.GrimList;
import net.caelumcraft.GrimList.utils.AsyncThenSyncOperation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;

public class ExportMysqlToFile {
    private final GrimList plugin;
    private final FileManager fm;
    private boolean canRun = true;

    public ExportMysqlToFile(GrimList plugin) {
        this.plugin = plugin;
        this.fm = new FileManager(plugin);
    }

    public boolean run(CommandSender sender) {
        if (canRun) {
            gatherMysqlData(sender);
        } else  {
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Already running a conversion!");
        }
        return true;
    }

    private void gatherMysqlData(final CommandSender sender) {
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Exporting MySQL to File. This may take a moment...");
        new AsyncThenSyncOperation(plugin, true) {

            @Override
            protected void execAsyncFirst() {
                canRun = false;
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                String host = plugin.getConfig().getString("MySQL.host");
                int port = plugin.getConfig().getInt("MySQL.port");
                String database = plugin.getConfig().getString("MySQL.database");
                String username = plugin.getConfig().getString("MySQL.username");
                String password = plugin.getConfig().getString("MySQL.password");
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&user=" + username +
                            (password != null && password.isEmpty() ? "&password=" + password : ""));
                    ps = conn.prepareStatement("SELECT * FROM " + database + ".playerdata;");
                    rs = ps.executeQuery();
                    if (rs.isBeforeFirst()) {
                        if (!new File(plugin.getDataFolder(), "playerdata.yml").exists()) {
                            fm.saveDefault();
                        }
                        if (!fm.isPlayersPopulated()) {
                            fm.get().createSection("Players");
                        }
                        while (rs.next()) {
                            String path = "Players." + rs.getString(1);
                            if (!fm.get().isSet(path)) {
                                fm.get().createSection(path);
                                fm.get().set(path + ".isWhitelisted", rs.getInt(2) == 1);
                                fm.get().set(path + ".lastUsername", rs.getString(3));
                                fm.get().set(path + ".lastAddress", rs.getString(4));
                                fm.get().set(path + ".firstLogin", rs.getString(5));
                                fm.get().set(path + ".lastLogin", rs.getString(6));
                                fm.get().set(path + ".loginCount", rs.getInt(7));
                            }
                        }
                        fm.save();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void execSyncThen() {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "MySQL data written to file!");
                canRun = true;
            }
        };
    }
}
