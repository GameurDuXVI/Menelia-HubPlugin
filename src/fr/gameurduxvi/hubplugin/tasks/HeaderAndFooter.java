package fr.gameurduxvi.hubplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.titleapi.TitleAPI;

public class HeaderAndFooter extends BukkitRunnable {
	
	private int i1 = 0;
	String[] games = {"§4§lTNT§6§lSmash", "&5§lAscension"};
	int currentGame = 0;

	@Override
	public void run() {    	
		i1++;
		if(i1>=15) {
			i1 = 0;
		}
		String text1 = "§7";
		String text2 = "§7";
		for (int i = 0; i < 15; i++) {
			if(i==i1) {
				text1 = text1 + "§b-§7";
			}
			else {
				text1 = text1 + "-";
			}
			int i2 = 14 - i1;
			if(i==i2) {
				text2 = text2 + "§b-§7";
			}
			else {
				text2 = text2 + "-";
			}
		}
		if(i1==4||i1==10) {
			currentGame++;
		}
		if(currentGame >= games.length) {
			currentGame = 0;
		}
		for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
			TitleAPI.sendTabTitle(loopPlayer, "\n§d§lMenelia\n", "\n§d§lNouveautés\n§f§l>>> " + games[currentGame] + " §f§l<<<\n\n      §7[" + text1 + "§b" + text2 + "§7]");
		}
	}
}
