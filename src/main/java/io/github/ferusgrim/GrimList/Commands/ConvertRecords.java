/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.ExportManagers.ExportFileToMysql;
import io.github.ferusgrim.GrimList.ExportManagers.ExportMysqlToFile;
import io.github.ferusgrim.GrimList.GrimList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConvertRecords {
    private final GrimList plugin;

    public ConvertRecords(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String[] args) {
        String c0 = args[0].toLowerCase();
        String c1 = args[1].toLowerCase();
        if ((!c0.equals("export") && !c0.equals("import")) || (!c1.equals("file") && !c1.equals("mysql"))){
            sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Invalid " + c0 + " argument!");
            return true;
        }
        switch (plugin.focusOn) {
            case "file":
                if (c1.equals("file")) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "You can't " + (c0.equals("export") ? "export to" : "import from")  + " the method you're using!");
                    return true;
                }
                if (c1.equals("mysql")) {
                    if (c0.equals("export")) {
                        ExportFileToMysql export = new ExportFileToMysql(plugin);
                        return export.run(sender);
                    }
                    if (c0.equals("import")) {
                        ExportMysqlToFile export = new ExportMysqlToFile(plugin);
                        return export.run(sender);
                    }
                }
                break;
            case "mysql":
                if (c1.equals("mysql")) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "You can't " + (c0.equals("export") ? "export to" : "import from")  + " the method you're using!");
                }
                if (c1.equals("file")) {
                    if (c0.equals("export")) {
                        ExportMysqlToFile export = new ExportMysqlToFile(plugin);
                        return export.run(sender);
                    }
                    if (c0.equals("import")) {
                        ExportFileToMysql export = new ExportFileToMysql(plugin);
                        return export.run(sender);
                    }
                }
                break;
            default:
                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Currently, no other supported focus method than \"file\" or \"mysql\"!");
                break;
        }
        return true;
    }
}
