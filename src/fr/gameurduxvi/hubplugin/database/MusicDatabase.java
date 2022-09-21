package fr.gameurduxvi.hubplugin.database;

import org.bukkit.entity.Player;

public class MusicDatabase {
	private Player player;
	private String songName;
	
	public MusicDatabase(Player player, String songName) {
		this.player = player;
		this.songName = songName;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getSongName() {
		return songName;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setSongName(String songName) {
		this.songName = songName;
	}
}
