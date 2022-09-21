package fr.gameurduxvi.hubplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.hubplugin.Main;

public class MeneboxTimer extends BukkitRunnable {
	
	private Player player;
	private Inventory inv;
	private String level;
	
	public MeneboxTimer( Player getPlayer, Inventory getInv, String getLevel) {
		player = getPlayer;
		inv = getInv;
		level = getLevel;
	}
	
	int i = 0;
	int speed = 0;
	String slot1Category;
	String slot1Loot;
	
	String slot2Category;
	String slot2Loot;
	
	String slot3Category;
	String slot3Loot;
	
	String slot4Category;
	String slot4Loot;
	
	String slot5Category;
	String slot5Loot;
	
	String slot6Category;
	String slot6Loot;
	
	String slot7Category;
	String slot7Loot;
	
	String slot8Category;
	String slot8Loot;
	
	String slot9Category;
	String slot9Loot;

	@Override
	public void run() {
		
		
		if(i<=20) {
			i++;
			move();
		}
		else if(i<=40) {
			speed++;
			if(speed>=2) {
				i++;
				speed = 0;
				move();
			}
		}
		else if(i<=60) {
			speed++;
			if(speed>=3) {
				i++;
				speed = 0;
				move();
			}
		}
		else if(i<=70) {
			speed++;
			if(speed>=4) {
				i++;
				speed = 0;
				move();
			}
		}
		else if(i<=80) {
			speed++;
			if(speed>=5) {
				i++;
				speed = 0;
				move();
			}
		}
		else {
			Bukkit.broadcastMessage("That");
			Bukkit.broadcastMessage("Level1:  " + slot5Category + " / " + slot5Loot);
			
			if(level.equals("2")||level.equals("3")) {
				Bukkit.broadcastMessage("Level2:  " + slot3Category + " / " + slot3Loot);
			}
			
			if(level.equals("3")) {
				Bukkit.broadcastMessage("Level3:  " + slot7Category + " / " + slot7Loot);
			}
			
			player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
			cancel();
		}
	}
	
	int tone = 0;
	
	private void move() {
		if(tone == 0) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 10);
			tone = 1;
		}
		else if(tone == 1) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 0.8f);
			tone = 2;
		}
		else if(tone == 2) {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			tone = 3;
		}
		else {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 0.8f);
			tone = 0;
		}
		
		player.openInventory(inv);
			
		inv.setItem(17, inv.getItem(16));
		slot9Category = slot8Category;
		slot9Loot = slot8Loot;
		
		inv.setItem(16, inv.getItem(15));
		slot8Category = slot7Category;
		slot8Loot = slot7Loot;
		
		inv.setItem(15, inv.getItem(14));
		slot7Category = slot6Category;
		slot7Loot = slot6Loot;
		
		inv.setItem(14, inv.getItem(13));
		slot6Category = slot5Category;
		slot6Loot = slot5Loot;
		
		inv.setItem(13, inv.getItem(12));
		slot5Category = slot4Category;
		slot5Loot = slot4Loot;
		
		inv.setItem(12, inv.getItem(11));
		slot4Category = slot3Category;
		slot4Loot = slot3Loot;
		
		inv.setItem(11, inv.getItem(10));
		slot3Category = slot2Category;
		slot3Loot = slot2Loot;
		
		inv.setItem(10, inv.getItem(9));
		slot2Category = slot1Category;
		slot2Loot = slot1Loot;
		
		
		String category = Main.getInstance().openMeneboxGetCategory();
		String loot = Main.getInstance().openMeneboxGetLoot(category);
		slot1Category = category;
		slot1Loot = loot;
		inv.setItem(9, Main.getInstance().openMeneboxGetItem(player, inv, category, loot));
	}

}
