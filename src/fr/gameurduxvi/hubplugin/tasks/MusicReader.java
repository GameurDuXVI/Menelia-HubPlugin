package fr.gameurduxvi.hubplugin.tasks;

import java.io.File;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MusicReader extends BukkitRunnable{
	private boolean songExist = false;
	private Player player;
	private String songName;
	private FileConfiguration fileConfig;
	private int maxTime;
	
	private int tick;
	
	public MusicReader(Player player, String songName) {
		this.player = player;
		this.songName = songName;
		
		File file = new File("plugins/Menelia/songs/" + songName + ".yml");
		
		if(file.exists()) {
			songExist = true;
			fileConfig = YamlConfiguration.loadConfiguration(file);
			
			maxTime = 0;
			
			for(String item: fileConfig.getKeys(false)) {
				int time = Integer.parseInt(item);
				if(time > maxTime) {
					maxTime = time;
				}
			}
		}
		else {
			player.sendMessage("§cSong don't exist");
		}
	}

	@Override
	public void run() {
		if(songExist) {
			tick++;
			if(tick == maxTime) {
				cancel();
			}
			int secTotal = maxTime / 20;
			int sec = tick / 20;
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§9" + songName + " §7(" + sec + "s / " + secTotal + "s)"));
			
			if(fileConfig.isSet(tick + ".BASEDRUM")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 5F, (float) fileConfig.getDouble(tick + ".BASEDRUM"));
			}
			if(fileConfig.isSet(tick + ".BASS")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 5F, (float) fileConfig.getDouble(tick + ".BASS"));
			}
			if(fileConfig.isSet(tick + ".BELL")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BELL, 5F, (float) fileConfig.getDouble(tick + ".BELL"));
			}
			if(fileConfig.isSet(tick + ".CHIME")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_CHIME, 5F, (float) fileConfig.getDouble(tick + ".CHIME"));
			}
			if(fileConfig.isSet(tick + ".FLUTE")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_FLUTE, 5F, (float) fileConfig.getDouble(tick + ".FLUTE"));
			}
			if(fileConfig.isSet(tick + ".GUITAR")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_GUITAR, 5F, (float) fileConfig.getDouble(tick + ".GUITAR"));
			}
			if(fileConfig.isSet(tick + ".HARP")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 5F, (float) fileConfig.getDouble(tick + ".HARP"));
			}
			if(fileConfig.isSet(tick + ".HAT")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HAT, 5F, (float) fileConfig.getDouble(tick + ".HAT"));
			}
			if(fileConfig.isSet(tick + ".PLING")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 5F, (float) fileConfig.getDouble(tick + ".PLING"));
			}
			if(fileConfig.isSet(tick + ".SNARE")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 5F, (float) fileConfig.getDouble(tick + ".SNARE"));
			}
			if(fileConfig.isSet(tick + ".XYLOPHONE")) {
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 5F, (float) fileConfig.getDouble(tick + ".XYLOPHONE"));
			}
		}
		else{
			cancel();
		}
	}
}
