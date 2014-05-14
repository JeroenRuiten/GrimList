/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.FocusManagers;

import io.github.ferusgrim.GrimList.GrimList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MySQLManager {
    private final GrimList plugin;

    public MySQLManager(GrimList plugin) {
        this.plugin = plugin;
        setupPlayerDataTable();
        setupLogTable();
        setupKnownUsernames();
        setupKnownAddresses();
    }

    private Connection sqlConnection() {
        String host = plugin.getConfig().getString("MySQL.host");
        int port = plugin.getConfig().getInt("MySQL.port");
        String database = plugin.getConfig().getString("MySQL.database");
        String username = plugin.getConfig().getString("MySQL.username");
        String password = plugin.getConfig().getString("MySQL.password");
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&user=" + username + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setupPlayerDataTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata (" +
                    "uuid varchar(36) NOT NULL, " +
                    "isWhitelisted tinyint(1) NOT NULL, " +
                    "lastUsername varchar(16) NOT NULL," +
                    "lastAddress varchar(15) NOT NULL, " +
                    "firstLogin varchar(19) NOT NULL, " +
                    "lastLogin varchar(19) NOT NULL, " +
                    "loginCount int(9) NOT NULL, " +
                    "UNIQUE KEY uuid (uuid)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    private void setupLogTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS uselogs (" +
                    "vicUuid varchar(36) NOT NULL, " +
                    "vicName varchar(16) NOT NULL, " +
                    "command varchar(64) NOT NULL, " +
                    "exUuid varchar(36) NOT NULL, " +
                    "exName varchar(16) NOT NULL, " +
                    "datePerformed varchar(19) NOT NULL, " +
                    "commandNumber int(9) NOT NULL AUTO_INCREMENT, " +
                    "PRIMARY KEY (commandNumber)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    private void setupKnownUsernames() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS previoususernames (" +
                    "uuid varchar(36) NOT NULL, " +
                    "usernames varchar(16) NOT NULL, " +
                    "UNIQUE KEY usernames (usernames), " +
                    "KEY uuid (uuid)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    private void setupKnownAddresses() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS previousaddresses (" +
                    "uuid varchar(36) NOT NULL, " +
                    "addresses varchar(16) NOT NULL, " +
                    "UNIQUE KEY addresses (addresses), " +
                    "KEY uuid (uuid)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    public void addPlayerToWhitelist(String uuid, String name) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + ".playerdata (uuid, isWhitelisted, lastUsername, lastAddress, firstLogin, lastLogin, loginCount) VALUES (" +
                    "?, 1, ?, '', '', '', 0) ON DUPLICATE KEY UPDATE isWhitelisted = 1");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
    }

    public void removePlayerFromWhitelist(String uuid, String name) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + ".playerdata (uuid, isWhitelisted, lastUsername, lastAddress, firstLogin, lastLogin, loginCount) VALUES (" +
                    "?, 0, ?, '', '', '', 0) ON DUPLICATE KEY UPDATE isWhitelisted = 0");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
    }

    public void deletePlayerFromRecord(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("DELETE FROM " + database + ".playerdata WHERE playerdata.uuid = ?");
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("DELETE FROM " + database + ".previoususernames WHERE previoususernames.uuid = ?");
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("DELETE FROM " + database + ".previousaddresses WHERE previousaddresses.uuid = ?");
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    public void recordOnLogin(String uuid, String name, String address) {
        String database = plugin.getConfig().getString("MySQL.database");
        String date = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss").format(new Date());
        boolean isFirstLoginSet = isFirstLoginSet(uuid);
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + ".playerdata (uuid, isWhitelisted, lastUsername, lastAddress, firstLogin, lastLogin, loginCount) " +
                    "VALUES (?, 0, ?, ?, ?, ?, 1)" +
                    "ON DUPLICATE KEY UPDATE lastUsername = ?, lastAddress = ?, " +
                    (isFirstLoginSet ? "firstLogin = ?, " : "") +
                    "lastLogin = ?, loginCount = loginCount + 1");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, date);
            ps.setString(5, date);
            ps.setString(6, name);
            ps.setString(7, address);
            ps.setString(8, date);
            if (isFirstLoginSet) {
                ps.setString(9, date);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
        if (!isAddressAPreviousAddress(uuid, address)) {
            addNewAddress(uuid, address);
        }
    }

    public void addCommandLog(String vicName, String vicUuid, String command, String exUuid, String exName) {
        String database = plugin.getConfig().getString("MySQL.database");
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + (".uselogs (vicUuid, vicName, command, exUuid, exName, datePerformed, commandNumber) VALUES (" +
                    "?, ?, ?, ?, ?, ?, NULL)"));
            ps.setString(1, vicUuid);
            ps.setString(2, vicName);
            ps.setString(3, command);
            ps.setString(4, exUuid);
            ps.setString(5, exName);
            ps.setString(6, fDate.format(new Date()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    public void addNewUsername(String uuid, String name) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + ".previoususernames (uuid, usernames) VALUES (?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    public void addNewAddress(String uuid, String address) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("INSERT INTO " + database + ".previousaddresses (uuid, addresses) VALUES (?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, address);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    public boolean doesRecordExist(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM " + database + ".playerdata WHERE playerdata.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) < 1) {
                    clean(conn, ps);
                    return false;
                }
            } else {
                clean(conn, ps);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return true;
    }

    public boolean isPlayerWhitelisted(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT isWhitelisted FROM " + database + ".playerdata WHERE playerdata.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 1) {
                clean(conn, ps);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return false;
    }

    public boolean isFirstLoginSet(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT firstLogin FROM " + database + ".playerdata WHERE playerdata.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                clean(conn, ps);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return true;
    }

    public boolean isNameAPreviousName(String uuid, String name) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT usernames FROM " + database + ".previoususernames WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(name)) {
                    clean(conn, ps);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return false;
    }

    public boolean isAddressAPreviousAddress(String uuid, String address) {
        String database = plugin.getConfig().getString("MySQL.database");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT addresses FROM " + database + ".previousaddresses WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(address)) {
                    clean(conn, ps);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return false;
    }

    public String getUUID(String name) {
        String database = plugin.getConfig().getString("MySQL.database");
        String uuid = "";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT uuid FROM " + database + ".playerdata WHERE playerdata.lastUsername = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                uuid = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return uuid;
    }

    private List<String> playerData(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        List<String> playerData = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT * FROM " + database + ".playerdata WHERE playerdata.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                playerData.add(rs.getString(1));
                playerData.add(rs.getInt(2) == 1 ? "Yes" : "No");
                playerData.add(rs.getString(3));
                playerData.add(rs.getString(4));
                playerData.add(rs.getString(5));
                playerData.add(rs.getString(6));
                playerData.add(String.valueOf(rs.getInt(7)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return playerData;
    }

    private List<String> previousUsernames(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        List<String> usernames = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT usernames FROM " + database + ".previoususernames WHERE previoususernames.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    usernames.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return usernames;
    }

    private List<String> previousAddresses(String uuid) {
        String database = plugin.getConfig().getString("MySQL.database");
        List<String> addresses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT addresses FROM " + database + ".previousaddresses WHERE previousaddresses.uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    addresses.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
        return addresses;
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

    public void doViewPlayer(CommandSender sender, String uuid) {
        List<String> playerData = new ArrayList<>(playerData(uuid));
        String uuidCopy = playerData.get(0);
        if (!uuid.equals(uuidCopy)) {
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "lolwut");
        }
        String isWhitelisted = playerData.get(1);
        String lastUsername = playerData.get(2);
        String firstLogin = playerData.get(4);
        String lastLogin = playerData.get(5);
        String loginCount = playerData.get(6);
        if (!(sender instanceof Player)) {
            sender.sendMessage("*-- " + lastUsername + " --*");
            sender.sendMessage("UUID: " + uuid);
            sender.sendMessage("Whitelisted: " + isWhitelisted);
            sender.sendMessage("Previous Usernames:");
            for (String viewPU : new ArrayList<>(previousUsernames(uuid))) {
                sender.sendMessage("  - " + viewPU);
            }
            sender.sendMessage("Previous Addresses:");
            for (String viewPI : new ArrayList<>(previousAddresses(uuid))) {
                sender.sendMessage("  - " + viewPI);
            }
            sender.sendMessage("First Login: " + (firstLogin == null ? "Never" : firstLogin));
            sender.sendMessage("Last Login: " + (lastLogin == null ? "Never" : lastLogin));
            sender.sendMessage("Logged in: " + loginCount + " times");
            sender.sendMessage("*-- " + lastUsername + " --*");
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + lastUsername + " &r&7--&6*"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lUUID: &r&e" + uuid));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lWhitelisted: &r&e" + isWhitelisted));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Usernames:"));
            for (String viewPU : new ArrayList<>(previousUsernames(uuid))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&e" + viewPU));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lPrevious Addresses:"));
            for (String viewPI : new ArrayList<>(previousAddresses(uuid))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "  &a&l- &r&e" + viewPI));
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lFirst Login: &r&e" + (firstLogin == null ? "Never" : firstLogin)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLast Login: &r&e" + (lastLogin == null ? "Never" : lastLogin)));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lLogged in: &r&e" + loginCount + " times"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6*&7-- &6&l" + lastUsername + " &r&7--&6*"));
        }
    }
}
