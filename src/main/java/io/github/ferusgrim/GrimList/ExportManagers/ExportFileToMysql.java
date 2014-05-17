/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.ExportManagers;

import io.github.ferusgrim.GrimList.FocusManagers.FileManager;
import io.github.ferusgrim.GrimList.FocusManagers.MySQLManager;
import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.utils.AsyncThenSyncOperation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportFileToMysql {
    private final GrimList plugin;
    private final FileManager fm;
    private final MySQLManager mm;
    private boolean canRun = true;

    public ExportFileToMysql(GrimList plugin) {
        this.plugin = plugin;
        this.fm = new FileManager(plugin);
        this.mm = new MySQLManager(plugin);
    }

    public boolean run(CommandSender sender) {
        if (canRun) {
            gatherMysqlData(sender);
        } else  {
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Already running a conversion!");
        }
        return true;
    }

    private void gatherMysqlData(CommandSender sender) {
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Exporting File to MySQL. This may take a moment...");
        new AsyncThenSyncOperation(plugin, true) {

            @Override
            protected void execAsyncFirst() {
                canRun = false;
                Connection conn = null;
                PreparedStatement ps = null;
                String host = plugin.getConfig().getString("MySQL.host");
                int port = plugin.getConfig().getInt("MySQL.port");
                String database = plugin.getConfig().getString("MySQL.database");
                String username = plugin.getConfig().getString("MySQL.username");
                String password = plugin.getConfig().getString("MySQL.password");
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&user=" + username +
                            (password != null && password.isEmpty() ? "&password=" + password : ""));
                    if (fm.isPlayersPopulated()) {
                        for (String uuid : fm.get().getConfigurationSection("Players").getKeys(false)) {
                            if (!mm.doesRecordExist(uuid)) {
                                String path = "Players." + uuid;
                                List<String> playerData = new ArrayList<>(dataLister(path));
                                ps = conn.prepareStatement("INSERT INTO " + database + ".playerdata (uuid, isWhitelisted, lastUsername, lastAddress, firstLogin, lastLogin, loginCount) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?);");
                                ps.setString(1, uuid);
                                ps.setInt(2, playerData.get(0).equals("true") ? 1 : 0);
                                ps.setString(3, playerData.get(1));
                                ps.setString(4, playerData.get(2));
                                ps.setString(5, playerData.get(3));
                                ps.setString(6, playerData.get(4));
                                ps.setInt(7, Integer.valueOf(playerData.get(5)));
                                ps.executeUpdate();
                            }
                        }
                    }
                    clean(conn, ps);
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
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void execSyncThen() {
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "File data written to MySQL server.");
                canRun = true;
            }
        };
    }

    private List<String> dataLister(String path) {
        List<String> playerData = new ArrayList<>();
        if (fm.get().isSet(path + ".isWhitelisted")) {
            playerData.add(String.valueOf(fm.get().getBoolean(path + ".isWhitelisted")));
        } else {
            playerData.add("false");
        }
        if (fm.get().isSet(path + ".lastUsername")) {
            playerData.add(fm.get().getString(path + ".lastUsername"));
        } else {
            playerData.add("");
        }
        if (fm.get().isSet(path + ".lastAddress")) {
            playerData.add(fm.get().getString(path + ".lastAddress"));
        } else {
            playerData.add("");
        }
        if (fm.get().isSet(path + ".firstLogin")) {
            playerData.add(fm.get().getString(path + ".firstLogin"));
        } else {
            playerData.add("");
        }
        if (fm.get().isSet(path + ".lastLogin")) {
            playerData.add(fm.get().getString(path + ".lastLogin"));
        } else {
            playerData.add("");
        }
        if (fm.get().isSet(path + ".loginCount")) {
            playerData.add(String.valueOf(fm.get().getInt(path + ".loginCount")));
        } else {
            playerData.add("0");
        }
        return playerData;
    }

    private void clean(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
