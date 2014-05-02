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
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MySQLManager(GrimList plugin) {
        this.plugin = plugin;
        registerPaths();
        setupPlayerDataTable();
        setupLogTable();
        setupKnownUsernames();
        setupKnownAddresses();
    }

   private void registerPaths() {
        host = plugin.getConfig().getString("MySQL.host");
        port = plugin.getConfig().getInt("MySQL.port");
        database = plugin.getConfig().getString("MySQL.database");
        username = plugin.getConfig().getString("MySQL.username");
        password = plugin.getConfig().getString("MySQL.password");
    }

    private Connection sqlConnection() {
        registerPaths();
        try {
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&user=" + username + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeUpdate(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clean(conn, ps);
    }

    private void setupPlayerDataTable() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `playerdata` (" +
                "`uuid` varchar(36) NOT NULL, " +
                "`isWhitelisted` tinyint(1) NOT NULL, " +
                "`lastUsername` varchar(16) NOT NULL," +
                "`lastAddress` varchar(15) NOT NULL, " +
                "`firstLogin` varchar(19) NOT NULL, " +
                "`lastLogin` varchar(19) NOT NULL, " +
                "`loginCount` int(9) NOT NULL, " +
                "UNIQUE KEY `uuid` (`uuid`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    }

    private void setupLogTable() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `uselogs` (" +
                "`vicUuid` varchar(36) NOT NULL, " +
                "`vicName` varchar(16) NOT NULL, " +
                "`command` varchar(64) NOT NULL, " +
                "`exUuid` varchar(36) NOT NULL, " +
                "`exName` varchar(16) NOT NULL, " +
                "`datePerformed` varchar(19) NOT NULL, " +
                "`commandNumber` int(9) NOT NULL AUTO_INCREMENT, " +
                "PRIMARY KEY (`commandNumber`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    }

    private void setupKnownUsernames() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `previoususernames` (" +
                "`uuid` varchar(36) NOT NULL, " +
                "`usernames` varchar(16) NOT NULL, " +
                "UNIQUE KEY `usernames` (`usernames`), " +
                "KEY `uuid` (`uuid`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    }

    private void setupKnownAddresses() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `previousaddresses` (" +
                "`uuid` varchar(36) NOT NULL, " +
                "`addresses` varchar(16) NOT NULL, " +
                "UNIQUE KEY `addresses` (`addresses`), " +
                "KEY `uuid` (`uuid`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;");
    }

    public void addPlayerToWhitelist(String uuid, String name){
        String sql = "UPDATE `" + database + "`.`playerdata` SET `isWhitelisted` = '1' WHERE `playerdata`.`uuid` = '[UUID]'"
                .replace("[UUID]", uuid);
        executeUpdate(sql);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
    }

    public void removePlayerFromWhitelist(String uuid, String name){
        String sql = "UPDATE `" + database + "`.`playerdata` SET `isWhitelisted` = '0' WHERE `playerdata`.`uuid` = '[UUID]'"
                .replace("[UUID]", uuid);
        executeUpdate(sql);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
    }

    public void deletePlayerFromRecord(String uuid){
        String sql = "DELETE FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = '[UUID]';"
                .replace("[UUID]", uuid);
        executeUpdate(sql);
        sql = "DELETE FROM `" + database + "`.`previoususernames` WHERE `previoususernames`.`uuid` = '[UUID]';";
        executeUpdate(sql);
        sql = "DELETE FROM `" + database + "`.`previousaddresses` WHERE `previousaddresses`.`uuid` = '[UUID]';";
        executeUpdate(sql);
    }

    public void createRecordFromQuery(String uuid, String name){
        String sql = "INSERT INTO `" + database + ("`.`playerdata` (`uuid`, `isWhitelisted`, `lastUsername`, `lastAddress`, `firstLogin`, `lastLogin`, `loginCount`) VALUES (" +
                "'[UUID]', '0','[NAME]', '', '', '', '0');")
                .replace("[UUID]", uuid)
                .replace("[NAME]", name);
        executeUpdate(sql);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
    }

    public void createRecordFromLogin(String uuid, String name, String address){
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        String sql = "INSERT INTO `" + database + ("`.`playerdata` (`uuid`, `isWhitelisted`, `lastUsername`, `lastAddress`, `firstLogin`, `lastLogin`, `loginCount`) " +
                "VALUES ('[UUID]', '0', '[NAME]', '[ADDRESS]', '[DATE]', '[DATE]', '1');")
                .replace("[UUID]", uuid)
                .replace("[NAME]", name)
                .replace("[ADDRESS]", address)
                .replace("[DATE]", fDate.format(new Date()));
        executeUpdate(sql);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
        if (!isAddressAPreviousAddress(uuid, address)) {
            addNewAddress(uuid, address);
        }
    }

    public void alterRecordOnLogin(String uuid, String name, String address){
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        String sql = "UPDATE `" + database + "`.`playerdata` SET `lastUsername` = '[NAME]', `lastAddress` = '[ADDRESS]', "
                .replace("[NAME]", name)
                .replace("[ADDRESS]", address);
        sql = sql + (isFirstLoginSet(uuid) ? "`firstLogin` = '[DATE]', " : "") + "`lastLogin` = '[DATE]', `loginCount` = loginCount + 1 WHERE `playerdata`.`uuid` = '[UUID]';"
                .replace("[UUID]", uuid)
                .replace("[DATE]", fDate.format(new Date()));
        executeUpdate(sql);
        if (!isNameAPreviousName(uuid, name)) {
            addNewUsername(uuid, name);
        }
        if (!isAddressAPreviousAddress(uuid, address)) {
            addNewAddress(uuid, address);
        }
    }

    public void addCommandLog(String vicName, String vicUuid, String command, String exUuid, String exName){
        SimpleDateFormat fDate = new SimpleDateFormat("MM.dd.yyyy-HH:mm:ss");
        String sql = "INSERT INTO `" + database + ("`.`uselogs` (`vicUuid`, `vicName`, `command`, `exUuid`, `exName`, `datePerformed`, `commandNumber`) VALUES (" +
                "'[VICUUID]', '[VICNAME]', '[COMMAND]', '[EXUUID]', '[EXNAME]', '[DATE]', NULL);")
                .replace("[VICUUID]", vicUuid)
                .replace("[VICNAME]", vicName)
                .replace("[COMMAND]", command)
                .replace("[EXUUID]", exUuid)
                .replace("[EXNAME]", exName)
                .replace("[DATE]", fDate.format(new Date()));
        executeUpdate(sql);
    }

    public void addNewUsername(String uuid, String name) {
        String sql = "INSERT INTO `" + database + "`.`previoususernames` (`uuid`, `usernames`) VALUES ('[UUID]', '[NAME]');"
                .replace("[UUID]", uuid)
                .replace("[NAME]", name);
        executeUpdate(sql);
    }

    public void addNewAddress(String uuid, String address) {
        String sql = "INSERT INTO `" + database + "`.`previousaddresses` (`uuid`, `addresses`) VALUES ('[UUID]', '[ADDRESS]');"
                .replace("[UUID]", uuid)
                .replace("[ADDRESS]", address);
        executeUpdate(sql);
    }

    public boolean doesRecordExistUnderName(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 1) {
                    plugin.log("WARNING", "Discovered multiple UUIDs with the same lastUsername!");
                }
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

    public boolean doesRecordExistUnderUUID(String uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT COUNT(*) FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = ?;");
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `isWhitelisted` FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = ?;");
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `firstLogin` FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = ?");
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `usernames` FROM `" + database + "`.`previoususernames` WHERE `uuid` = ?;");
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
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `addresses` FROM `" + database + "`.`previousaddresses` WHERE `uuid` = ?;");
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
        String uuid = "";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `uuid` FROM `" + database + "`.`playerdata` WHERE `playerdata`.`lastUsername` = ?;");
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
        List<String> playerData = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT * FROM `" + database + "`.`playerdata` WHERE `playerdata`.`uuid` = ?;");
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
        List<String> usernames = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `usernames` FROM `" + database + "`.`previoususernames` WHERE `previoususernames`.`uuid` = ?;");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    usernames.add(rs.getString(1));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        clean(conn, ps);
        return usernames;
    }

    private List<String> previousAddresses(String uuid) {
        List<String> addresses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = sqlConnection();
            ps = conn.prepareStatement("SELECT `addresses` FROM `" + database + "`.`previousaddresses` WHERE `previousaddresses`.`uuid` = ?;");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    addresses.add(rs.getString(1));
                }
            }
        } catch (SQLException e){
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
