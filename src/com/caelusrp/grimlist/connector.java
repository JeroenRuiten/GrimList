// Copyright Nicholas Badger (A.K.A. FerusGrim) //
// Attribution 3.0 Unported (CC BY 3.0) //

package com.caelusrp.grimlist;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;

public class connector {

    public static Connection getSQLConnection() {
    	
        try {
            return DriverManager.getConnection("jdbc:mysql://" + grimlist.Settings.getString("grimlist.mysql.host") + ":" + grimlist.Settings.getInt("grimlist.mysql.port") +"/" + grimlist.Settings.getString("grimlist.mysql.database") + "?autoReconnect=true&user=" + grimlist.Settings.getString("grimlist.mysql.username") + "&password=" + grimlist.Settings.getString("grimlist.mysql.password"));
        } catch (SQLException ex) {
        	grimlist.log.log(Level.SEVERE, "§e[GL]§c Connection to the server was refused!", ex);
        }
        return null;
    }

}