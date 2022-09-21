package fr.gameurduxvi.hubplugin.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.gameurduxvi.hubplugin.Main;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;

public class loots {
	
	private static File file;
	private static FileConfiguration fileConfig;
	
	public void load() {
		// Loading plugin config file
		file = new File(config.getConfig().getString("ConfigFiles.MeneboxLoots"));
		Bukkit.getConsoleSender().sendMessage(MeneliaAPI.getInstance().colorize(Main.getInstance().pluginPrefix + " Config File &e" + file.getPath() + " (loots.java)&6 has been loaded !"));
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
				
		fileConfig = YamlConfiguration.loadConfiguration(file);
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
