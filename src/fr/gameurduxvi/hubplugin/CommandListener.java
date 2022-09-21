package fr.gameurduxvi.hubplugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.hubplugin.config.config;
import fr.gameurduxvi.hubplugin.tasks.MusicReader;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player)sender;
			/*
			if(args.length == 0) {
				
			}
			if(args.length >=1) {
				StringBuilder bc = new StringBuilder();
				
				for(String part: args) {
					bc.append(part + " ");
				}
				
				Bukkit.broadcastMessage(bc.toString());
			}
			*/
			if(cmd.getName().equalsIgnoreCase("music")) {
				if(args.length > 0) {
					MusicReader task = new MusicReader(player, args[0]);
					task.runTaskTimer(Main.getInstance(), 0, 1);
				}
				else {
					player.sendMessage(Main.getInstance().pluginPrefix + " §c/music <file name>");
				}
				
			}
			if(cmd.getName().equalsIgnoreCase("editmusic")) {
				if(args.length > 0) {
					File file = new File("plugins/Menelia/songs/" + args[0] + ".yml");
					
					if(file.exists()) {
						player.sendMessage("§aSong found !");
						Main.getInstance().getEventListener().MusicEditor(player, args[0], 1);
					}
					else {
						player.sendMessage("§cThe song don't exist !");
						player.sendMessage("§aCreating a new one...");
						try {
							file.createNewFile();
							Main.getInstance().getEventListener().MusicEditor(player, args[0], 1);
						} catch (IOException e) {
							e.printStackTrace();
							player.sendMessage("§cAn error occured while creating a new file !");
						}
					}
				}
				else {
					player.sendMessage(Main.getInstance().pluginPrefix + " §c/music <file name>");
				}
				
			}
			if(cmd.getName().equalsIgnoreCase("chest")) {
				if(args.length > 0) {
					player.sendMessage("" + args[0].length());
					Main.getInstance().openMenebox(player, args[0]);
				}
				else {
					player.sendMessage(MeneliaAPI.getInstance().colorize(Main.getInstance().pluginPrefix + " &c/chest <level>"));
				}
				
			}/*
			else if(cmd.getName().equalsIgnoreCase("b")) {
				for(AdvancementsMenelia adv: Main.INSTANCE.getAdvancementsMenelia()) {
					if(player.getUniqueId().equals(adv.getUUID())) {
						Bukkit.broadcastMessage("remove");
						AdvancementManager manager = adv.getManager();
						Advancement root = manager.getAdvancement(new NameKey("Menelia", "root"));
						manager.removeAdvancement(root);
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("a")) {
				
			}
			else if(cmd.getName().equalsIgnoreCase("c")) {
				for(AdvancementsMenelia adv: Main.INSTANCE.getAdvancementsMenelia()) {
					if(player.getUniqueId().equals(adv.getUUID())) {
						Bukkit.broadcastMessage("grant");
						AdvancementManager manager = adv.getManager();
						for(Advancement adv2: manager.getAdvancements()) {
							manager.grantAdvancement(player, adv2);
							manager.saveProgress(player, "Menelia");
						}
					}
				}
			}	*/		
			else if(cmd.getName().equalsIgnoreCase("hub")) {
				Main.getInstance().Hub(player);
				/*for(Accounts account: Main.INSTANCE.getAcounts()) {
					Bukkit.broadcastMessage(account.getName() + " / " + account.getUUID());
				}*/
			}			
			else if(cmd.getName().equalsIgnoreCase("sethub")) {
				int x = (int) player.getLocation().getX();
				int y = (int) player.getLocation().getY();
				int z = (int) player.getLocation().getZ();
				String world = player.getLocation().getWorld().getName();
				
				config.getConfig().set("HubLocation.x", x);
				config.getConfig().set("HubLocation.y", y);
				config.getConfig().set("HubLocation.z", z);
				config.getConfig().set("HubWorldName", world);
				try {
					config.getConfig().save(config.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				player.sendMessage(MeneliaAPI.getInstance().colorize("&6[HubPlugin] x:" + x + " y:" + y + " z:" + z + " world:" + world));
			}
		}
		
		
		return false;
	}

}
