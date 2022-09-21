package fr.gameurduxvi.hubplugin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.gameurduxvi.meneliaapi.MeneliaAPI;

public class PluginCustomMessageListener implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		if(channel.equals("MeneliaChannel")) {
			ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
			
			String server = in.readUTF();
			String sub = in.readUTF();
			
			if(server.equals(Bukkit.getServerName())) {
				if(sub.equals("SendTNTSmashGames")) {
					String readLine = in.readUTF();
					String playerUUID = in.readUTF();
					
					Inventory invMenu = Bukkit.createInventory(null, 54, "TNTSmash");
					
					Player receivePlayer = null;
					
					for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
						if(loopPlayer.getUniqueId().toString().equals(playerUUID)) {
							receivePlayer = loopPlayer;
						}
					}
					int slot = 9;
					String games = readLine;
					String[] arrOfStr = games.split("#");
					for(String game: arrOfStr) {
						String[] gameData = game.split("-");
						int gameNum = Integer.parseInt(gameData[0]);
						int current = Integer.parseInt(gameData[1]);
						int max = Integer.parseInt(gameData[2]);
						if(max!=0) {
							slot++;
							if(slot==17||slot==26||slot==35||slot==44) {
								slot++;
								slot++;
							}
							ItemStack gameItem = new ItemStack(Material.PAPER, 1);
							ItemMeta gameItemM = gameItem.getItemMeta();
							gameItemM.setDisplayName(MeneliaAPI.getInstance().getLang(receivePlayer, "§bPartie ", "§bGame ") + gameNum);
							String[] lore = {"§b" + current + "/" + max};
							gameItemM.setLore(Arrays.asList(lore));
							gameItem.setItemMeta(gameItemM);
							invMenu.setItem(slot, gameItem);
						}
					}
					receivePlayer.openInventory(invMenu);
					
					
				}
			
			
			
			}
		}

	}

}
