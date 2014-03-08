package io.github.ferusgrim.GrimList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	private static GrimList gl;
	private static File ConfigFile = new File(gl.pluginDir.getAbsolutePath() + File.separator + "config.yml");
	private static File PhraseFile = new File(gl.pluginDir.getAbsolutePath() + File.separator + "phrase.yml");
	public static YamlConfiguration Config;
	public static YamlConfiguration Phrase;
	
	public static YamlConfiguration loadConfig(boolean inputNewConfiguration) {
		Config = new YamlConfiguration();
		if(!inputNewConfiguration) try{
			Config.load(ConfigFile);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidConfigurationException e){
			e.printStackTrace();
		}else try{
			DefaultConfig("GrimList.Enabled", true);
			DefaultConfig("GrimList.Debug-Level", 3);
			DefaultConfig("GrimList.Method", 1);
			DefaultConfig("GrimList.MySQL.Host", "localhost");
			DefaultConfig("GrimList.MySQL.Port", 3306);
			DefaultConfig("GrimList.MySQL.Database", "whitelist");
			DefaultConfig("GrimList.MySQL.Username", "root");
			DefaultConfig("GrimList.MySQL.Password", "toor");
			DefaultConfig("GrimList.File.Name", "players.txt");
			DefaultConfig("GrimList.File.Update-Interval", 180);
			DefaultConfig("GrimList.URL.URL", "https://my.com/players.txt");
			Config.save(ConfigFile);
			return Config;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static YamlConfiguration loadPhrase(boolean inputNewConfiguration) {
		Phrase = new YamlConfiguration();
		if(!inputNewConfiguration) try{
			Phrase.load(PhraseFile);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidConfigurationException e){
			e.printStackTrace();
		}else try{
			DefaultPhrase("Phrases.KickMessage", "Phrase");
			DefaultPhrase("Phrases.NotifyFail", "Phrase");
			Phrase.save(PhraseFile);
			return Phrase;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static void DefaultConfig(String Path, Object Value) {
		Config.set(Path, Config.get(Path, Value));
	}
	
	private static void DefaultPhrase(String Path, Object Value) {
		Phrase.set(Path, Phrase.get(Path, Value));
	}
	
	public static void Start(boolean start) {
		if(start){
			if(!gl.pluginDir.exists()){
				gl.pluginDir.mkdir();
			}
			if(ConfigFile.exists()){
				Config = loadConfig(false);
			}else try{
				ConfigFile.createNewFile();
				Config = loadConfig(true);
			}catch(IOException e){
				e.printStackTrace();
			}
			if(PhraseFile.exists()){
				Phrase = loadPhrase(false);
			}else try{
				PhraseFile.createNewFile();
				Phrase = loadPhrase(true);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			Config = null;
			Phrase = null;
		}
	}
}
