package io.github.ferusgrim.GrimList;

import java.io.IOException;

import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

/**
 * Copyright (C) 2014 Nicholas Badger
 * @author FerusGrim
 */

public class MetricsManager {
	
	public static void Start(GrimList plugin) {
		try{
			Metrics metrics = new Metrics(plugin);
			Graph datausage = metrics.createGraph("Most used Data Source?");
			Graph debugusage = metrics.createGraph("Most used Debug Level?");
			Graph listusage = metrics.createGraph("Are we actually on?");
			
			datausage.addPlotter(new Metrics.Plotter("MySQL"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getBoolean("GrimList.Use.MySQL")? 1 : 0;
				}
			});
			datausage.addPlotter(new Metrics.Plotter("File"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getBoolean("GrimList.Use.File")? 1 : 0;
				}
			});
			datausage.addPlotter(new Metrics.Plotter("URL"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getBoolean("GrimList.Use.URL")? 1 : 0;
				}
			});
			debugusage.addPlotter(new Metrics.Plotter("Severe"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getInt("GrimList.Debug-Level") == 1? 1 : 0;
				}
			});
			debugusage.addPlotter(new Metrics.Plotter("Warning"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getInt("GrimList.Debug-Level") == 2? 1 : 0;
				}
			});
			debugusage.addPlotter(new Metrics.Plotter("Info"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getInt("GrimList.Debug-Level") == 3? 1 : 0;
				}
			});
			debugusage.addPlotter(new Metrics.Plotter("Debug"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getInt("GrimList.Debug-Level") == 4? 1 : 0;
				}
			});
			listusage.addPlotter(new Metrics.Plotter("Yes"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getBoolean("GrimList.Enabled")? 1 : 0;
				}
			});
			listusage.addPlotter(new Metrics.Plotter("No"){
				@Override
				public int getValue(){
					return ConfigManager.Config.getBoolean("GrimList.Enabled")? 0 : 1;
				}
			});
			metrics.start();
		}catch(IOException e){
			GrimList.toLog(1, "Error in Metrics: " + e.getMessage());
		}
	}

}
