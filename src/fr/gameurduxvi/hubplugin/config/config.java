package fr.gameurduxvi.hubplugin.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.gameurduxvi.hubplugin.Main;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;

public class config {
	
	private static File file;
	private static FileConfiguration fileConfig;
	
	public void load() {
		// Loading plugin config file
		file = new File("plugins/Menelia/hubplugin.yml");
		Bukkit.getConsoleSender().sendMessage(MeneliaAPI.getInstance().colorize(Main.getInstance().pluginPrefix + " Config File &e" + file.getPath() + " (config.java)&6 has been loaded !"));
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
				
		fileConfig = YamlConfiguration.loadConfiguration(file);
		
		if(fileConfig.getString("CurrentServerName") == null) {
			fileConfig.set("CurrentServerName", "serveur1");
		}
		if(fileConfig.getString("HubWorldName") == null) {
			fileConfig.set("HubWorldName", "world");
		}
		if(fileConfig.getString("HubLocation.x") == null) {
			fileConfig.set("HubLocation.x", 0);
		}
		if(fileConfig.getString("HubLocation.y") == null) {
			fileConfig.set("HubLocation.y", 120);
		}
		if(fileConfig.getString("HubLocation.z") == null) {
			fileConfig.set("HubLocation.z", 0);
		}
		if(fileConfig.getString("ConfigFiles.MeneboxLoots") == null) {
			fileConfig.set("ConfigFiles.MeneboxLoots", "plugins/Menelia/loots.yml");
		}
		
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File getFile() {
		return file;
	}
	
	public static FileConfiguration getConfig() {
		return fileConfig;
	}
}
