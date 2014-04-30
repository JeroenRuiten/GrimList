/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.IOException;

public class MetricManager {
    private GrimList plugin;

    public MetricManager(GrimList plugin) {
        this.plugin = plugin;
    }

    public void setupMetric() {
        try {
            Metrics metrics = new Metrics(plugin);
            Graph focus = metrics.createGraph("Most common data focus?");
            Graph write = metrics.createGraph("Most common write source?");
            focus.addPlotter(new Metrics.Plotter("File") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getString("Focus").equals("file") ? 1 : 0;
                }
            });
            focus.addPlotter(new Metrics.Plotter("MySQL") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getString("Focus").equals("mysql") ? 1 : 0;
                }
            });
            focus.addPlotter(new Metrics.Plotter("SQLite") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getString("Focus").equals("sqlite") ? 1 : 0;
                }
            });
            focus.addPlotter(new Metrics.Plotter("URL") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getString("Focus").equals("url") ? 1 : 0;
                }
            });
            write.addPlotter(new Metrics.Plotter("File") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getBoolean("Write.file") ? 1 : 0;
                }
            });
            write.addPlotter(new Metrics.Plotter("MySQL") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getBoolean("Write.mysql") ? 1 : 0;
                }
            });
            write.addPlotter(new Metrics.Plotter("SQLite") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getBoolean("Write.sqlite") ? 1 : 0;
                }
            });
            write.addPlotter(new Metrics.Plotter("URL") {
                @Override
                public int getValue() {
                    return plugin.getConfig().getBoolean("Write.url") ? 1 : 0;
                }
            });
            metrics.start();
        } catch (IOException e) {
            plugin.log("DEBUG", "Failed to add stats!");
        }
    }

}
