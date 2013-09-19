//////////////////////////////////////////////////////////
///// Copyright Nicholas Badger (a.k.a. FerusGrim) //////
////////////////////////////////////////////////////////
/////////////////////////// Attribution 3.0 Unported //
////////////////////////// (CC BY 3.0) ///////////////
/////////////////////////////////////////////////////
package com.caelusrp.grimlist;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;

public class connector {

    public static Connection getSQLConnection() {

        try {
            return DriverManager.getConnection("jdbc:mysql://" + grimlist.Settings.getString("grimlist.sql.host") + ":" + grimlist.Settings.getInt("grimlist.sql.port") +"/" + grimlist.Settings.getString("grimlist.sql.database") + "?autoReconnect=true&user=" + grimlist.Settings.getString("grimlist.sql.username") + "&password=" + grimlist.Settings.getString("grimlist.sql.password"));
        } catch (SQLException ex) {
            grimlist.log.log(Level.SEVERE, "Unable to retreive connection", ex);
        }
        return null;
    }

}