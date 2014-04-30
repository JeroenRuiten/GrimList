/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.FocusManagers;

import io.github.ferusgrim.GrimList.GrimList;


public class MySQLManager {
    private GrimList plugin;

    public MySQLManager(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean doesRecordExist(String uuid) {
        return false;
    }

    public boolean isPlayerWhitelisted(String uuid) {
        return false;
    }

    public boolean getUUID(String uuid) {
        return false;
    }
}
