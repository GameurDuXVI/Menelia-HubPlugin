package fr.gameurduxvi.hubplugin.EventListeners;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.hubplugin.Main;
import fr.gameurduxvi.hubplugin.tasks.MusicReader;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;
import fr.gameurduxvi.meneliaapi.Databases.Accounts;
import fr.gameurduxvi.meneliaapi.EventHandlers.AccountLoadedEvent;
import fr.gameurduxvi.meneliaapi.EventHandlers.AccountRefreshedEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class EventListener implements Listener {
	
	@EventHandler
	public void onAccountLoaded(AccountLoadedEvent e) {
		Player player = Bukkit.getPlayer(e.getAccount().getName());
		Main.getInstance().Hub(player);
		for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
			Main.getInstance().refreshScoreboard(loopPlayer);
			if(loopPlayer.getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
				String highGroup = MeneliaAPI.getFunctions().getGroup(player);
				String colorName = PermissionsEx.getPermissionManager().getGroup(highGroup).getOption("colorname");
				loopPlayer.sendMessage(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(loopPlayer, "&7(&a+&7) &" + colorName + player.getName() + " &6a rejoint le hub !", "&7(&a+&7) &" + colorName + player.getName() + " &6has joined the hub !")));
				//loopPlayer.spigot().sendMessage(ComponentSerializer.parse("[\"\",{\"text\":\"(\",\"color\":\"gray\"},{\"text\":\"+\",\"color\":\"green\"},{\"text\":\")\",\"color\":\"gray\"},{\"text\":\" \"}," + Main.INSTANCE.getPlayer(player, loopPlayer) + ",{\"text\":\" " + MeneliaAPI.getInstance().getLang(player, "a rejoint le hub !", "joined the hub !") + "\",\"color\":\"gold\"}]"));
				
			}
		}
	}
	
	@EventHandler
	public void onAccountRefreshed(AccountRefreshedEvent e) {
		Player player = Bukkit.getPlayer(e.getAccount().getName());
		Main.getInstance().refreshScoreboard(player);
		if(player.getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			Main.getInstance().HubInventory(player);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Main.getInstance().Hub(e.getPlayer());
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		int i = 20;
		for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
			i++;
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Main.getInstance().refreshScoreboard(loopPlayer);
				}
			}, i);
		}
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(event.getMessage().length()<=1) {
			event.setCancelled(true);
			player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cVotre message est trop court !", "§cYour message is too short !"));
			return;
		}
		
		String group = MeneliaAPI.getFunctions().getGroup(player);
		String prefix = PermissionsEx.getPermissionManager().getGroup(group).getPrefix();
		String colorname = PermissionsEx.getPermissionManager().getGroup(group).getOption("colorname");
		
		String message1 = MeneliaAPI.getInstance().colorize(prefix);
		String message2 = MeneliaAPI.getInstance().colorize(" &" + colorname + player.getDisplayName());
		String message3 = MeneliaAPI.getInstance().colorize(" §7>> " + event.getMessage());
		
		TextComponent text1 = new TextComponent("§7[§c!§7]");
		text1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/me le truc de report"));
		text1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/me le truc de report"));
		
		TextComponent text2 = new TextComponent(" " + message1);
		
		TextComponent text3 = new TextComponent(message2);
		text3.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/msg " + player.getName()));
		
		TextComponent intertext = new TextComponent("");
		intertext.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, null));
		intertext.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, null));
		
		String lang = "EN";
		for(Accounts account: MeneliaAPI.getInstance().getAccounts()) {
			if(account.getUUID().toString().equals(player.getUniqueId().toString())) {
				lang = account.getLang();
			}
		}
		
		for(Player lp: Bukkit.getOnlinePlayers()) {
			String extraMessage;
			if(lp.hasPermission("admin.warns.see")) {
				extraMessage = MeneliaAPI.getInstance().getLang(lp, "\n§7Avertissements: ...", "\n§7Warns: ...");
			}
			else {
				extraMessage = "";
			}
			text1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MeneliaAPI.getInstance().getLang(lp, "§7Signaler cette phrase", "§7Report this sentence")).create()));
			TextComponent bulle = new TextComponent(MeneliaAPI.getInstance().getLang(lp, "§7Langue: §b" + lang + extraMessage, "§7Language: §b" + lang + extraMessage));
			text3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bulle).create()));
			lp.spigot().sendMessage(new ComponentBuilder(text1).append(intertext).append(text2).append(intertext).append(text3).append(intertext).append(TextComponent.fromLegacyText(message3)).create());
		}
		Bukkit.getConsoleSender().sendMessage(message1 + message2 + message3);
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(final FoodLevelChangeEvent e) {
		if(e.getEntity().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamageEvent(EntityDamageEvent e) {
		if(e.getEntityType().toString().equalsIgnoreCase("PLAYER")) {
			if(e.getEntity().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Main.getInstance().refreshScoreboard(e.getPlayer());
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void oninteract(PlayerInteractEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				if(e.getAction().equals(org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) || e.getAction().equals(org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(org.bukkit.event.block.Action.PHYSICAL)) {
					if(e.getClickedBlock().getType().toString().contains("DOOR") && !e.getClickedBlock().getType().toString().contains("TRAP_DOOR")) {
					}
					else {
						e.setCancelled(true);
					}
				}
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	// itemframe and NOT armor stand
	@EventHandler
	public void onRightClickEntity(PlayerInteractEntityEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPaintingBreak(HangingBreakByEntityEvent e) {
		if(e.getRemover() instanceof Player) {
			Player p = (Player) e.getRemover();
			if(p.getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
				if(p.getGameMode().equals(GameMode.ADVENTURE)||p.getGameMode().equals(GameMode.SURVIVAL)) {
					e.setCancelled(true);
				}
				else {
					if(!PermissionsEx.getUser(p).has("admin.inertact.hub")) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	// armor stand
	@EventHandler
	public void onRightClickEntity(PlayerInteractAtEntityEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)||e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
			else {
				if(!PermissionsEx.getUser(e.getPlayer()).has("admin.inertact.hub")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onLeftClickEntity(EntityDamageByEntityEvent e) {
		//if(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof ArmorStand) {
			if(e.getDamager() instanceof Player) {
				Player player = (Player) e.getDamager();
				if(player.getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
					if(player.getGameMode().equals(GameMode.ADVENTURE)||player.getGameMode().equals(GameMode.SURVIVAL)) {
						e.setCancelled(true);
					}
					else {
						if(!PermissionsEx.getUser(player).has("admin.inertact.hub")) {
							e.setCancelled(true);
						}
					}
				}
			}
		//}
	}
	
	
	
	public static ItemStack getItem(Material material, int amount, int dataTag, String customName, String lore) {
		ItemStack It = new ItemStack(material, amount, (byte)dataTag);
		ItemMeta metaIt = It.getItemMeta();
		
		
		metaIt.setDisplayName(MeneliaAPI.getInstance().colorize(customName));
		
		
		if(lore.length() != 0) {
			String[] arrOfStr = lore.split("/n");
			for (int i=0; i < arrOfStr.length; i++)
		    {
		      arrOfStr[i] = arrOfStr[i].replace("&", "§");
		    }
			metaIt.setLore(Arrays.asList(arrOfStr));
			//metaIt.setLore(Arrays.asList(Main.colorize(lore)));
		}
		
		metaIt.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		metaIt.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		It.setItemMeta(metaIt);
		return It;
	}
	
	
	public static ItemStack getItem(Material material, int amount, int dataTag, String customName, String lore, boolean hasEnchant) {
		ItemStack It = new ItemStack(material, amount, (byte)dataTag);
		ItemMeta metaIt = It.getItemMeta();
		
		
		metaIt.setDisplayName(MeneliaAPI.getInstance().colorize(customName));
		
		
		if(lore.length() != 0) {
			String[] arrOfStr = lore.split("/n");
			for (int i=0; i < arrOfStr.length; i++)
		    {
		      arrOfStr[i] = arrOfStr[i].replace("&", "§");
		    }
			metaIt.setLore(Arrays.asList(arrOfStr));
			//metaIt.setLore(Arrays.asList(Main.colorize(lore)));
		}
		
		if(hasEnchant) {
			metaIt.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		}
		
		metaIt.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		metaIt.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		It.setItemMeta(metaIt);
		return It;
	}
	
	
	public void MusicEditor(Player player, String songName, int tick) {
			File file = new File("plugins/Menelia/songs/" + songName + ".yml");
			
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			
			Inventory inv = Bukkit.createInventory(null, 54, "Music Editor >> " + songName);
			
			ItemStack panel = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
	    	ItemMeta metaPanel = panel.getItemMeta();
	    	
	    	metaPanel.setDisplayName("§a");
	    	
	    	panel.setItemMeta(metaPanel);
	    	
	    	for(int i = 0; i<54; i++) {
	    		inv.setItem(i, panel);
	    	}
			
			ArrayList<String> list = new ArrayList<>();
			list.add("BASEDRUM");
			list.add("BASS");
			list.add("BELL");
			list.add("CHIME");
			list.add("FLUTE");
			list.add("GUITAR");
			list.add("HARP");
			list.add("HAT");
			list.add("PLING");
			list.add("SNARE");
			list.add("XYLOPHONE");
			
			int maxTime = 0;
			
			for(String item: fileConfig.getKeys(false)) {
				int time = Integer.parseInt(item);
				if(time > maxTime) {
					maxTime = time;
				}
			}
			
			
			int i = 9;
			for(String item: list) {
				i++;
				if(i==17) {
					i++;
					i++;
				}
				if(fileConfig.isSet(tick + "." + item)) {
					//player.playSound(player.getLocation(), Sound.valueOf(item), 5F, (float) fileConfig.getDouble(tick + "." + item));
					ItemStack it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			    	ItemMeta metaIt = it.getItemMeta();
			    	
			    	ArrayList<String> lore = new ArrayList<>();
			    	lore.add(fileConfig.getDouble(tick + "." + item) + "");
			    	metaIt.setLore(lore);
			    	metaIt.setDisplayName("§a"+ item);
			    	
			    	it.setItemMeta(metaIt);
			    	
			    	inv.setItem(i, it);
				}
				else {
					ItemStack it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			    	ItemMeta metaIt = it.getItemMeta();
			    	
			    	metaIt.setDisplayName("§a"+ item);
			    	
			    	it.setItemMeta(metaIt);
			    	
			    	inv.setItem(i, it);
				}
			}			
			
			ItemStack previous = new ItemStack(Material.ARROW);
	    	ItemMeta metaPrevious = previous.getItemMeta();
	    	
	    	metaPrevious.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§7Précédent", "§7Previous"));
	    	metaPrevious.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	
	    	ArrayList<String> lore = new ArrayList<>();
	    	lore.add("§7Music Name: " + songName);
	    	lore.add("§7Current tick: " + tick);
	    	lore.add("§7Max tick time: " + maxTime);
	    	metaPrevious.setLore(lore);
	    	previous.setItemMeta(metaPrevious);
	    	
	    	inv.setItem(48, previous);
			
			ItemStack next = new ItemStack(Material.ARROW);
	    	ItemMeta metaNext = next.getItemMeta();
	    	
	    	metaNext.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§7Prochain", "§7Next"));
	    	metaNext.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	
	    	lore = new ArrayList<>();
	    	lore.add("§7Music Name: " + songName);
	    	lore.add("§7Current tick: " + tick);
	    	lore.add("§7Max tick time: " + maxTime);
	    	metaNext.setLore(lore);
	    	next.setItemMeta(metaNext);
	    	
	    	inv.setItem(50, next);
			
			ItemStack close = new ItemStack(Material.BARRIER);
	    	ItemMeta metaClose = close.getItemMeta();
	    	
	    	metaClose.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§aFermer", "§aClose"));
	    	metaClose.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	
	    	close.setItemMeta(metaClose);
	    	
	    	inv.setItem(49, close);	
			
			ItemStack playSong = new ItemStack(Material.EMERALD_BLOCK);
	    	ItemMeta metaPlaySong = playSong.getItemMeta();
	    	
	    	metaPlaySong.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§aJouer la musique", "§aPlay the music"));
	    	metaPlaySong.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	
	    	playSong.setItemMeta(metaPlaySong);
	    	
	    	inv.setItem(53, playSong);
			
			ItemStack playTick = new ItemStack(Material.EMERALD);
	    	ItemMeta metaPlayTick = playTick.getItemMeta();
	    	
	    	metaPlayTick.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§aPlay the tick moment", "§aPlay the tick moment"));
	    	metaPlayTick.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    	
	    	playTick.setItemMeta(metaPlayTick);
	    	
	    	inv.setItem(52, playTick);

			
			ItemStack info = new ItemStack(Material.PAPER);
	    	ItemMeta metaInfo = info.getItemMeta();
	    	
	    	metaInfo.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§aInfo sur la musique:", "§aInformation about the music:"));
	    	
	    	lore = new ArrayList<>();
	    	lore.add("§7Music Name: " + songName);
	    	lore.add("§7Current tick: " + tick);
	    	lore.add("§7Max tick time: " + maxTime);
	    	metaInfo.setLore(lore);
	    	
	    	info.setItemMeta(metaInfo);
	    	
	    	inv.setItem(4, info);	
			
			player.openInventory(inv);
	}
	
	@EventHandler
	public void OnClickInventory(InventoryClickEvent event) {
		
		Inventory inv = event.getInventory();
		Player player = (Player)event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		
		if(current == null) return;
		
		if(inv.getName().contains("Music Editor >> ")) {
			ArrayList<String> list1 = new ArrayList<>();
			list1.add("BASEDRUM");
			list1.add("BASS");
			list1.add("BELL");
			list1.add("CHIME");
			list1.add("FLUTE");
			list1.add("GUITAR");
			list1.add("HARP");
			list1.add("HAT");
			list1.add("PLING");
			list1.add("SNARE");
			list1.add("XYLOPHONE");
			if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "§7Prochain", "§7Next"))) {
				String songName = "";
				int tick = 0;
				for(String item: inv.getItem(4).getItemMeta().getLore()) {
					if(item.contains("§7Music Name: ")) {
						songName = item.replace("§7Music Name: ", "");
					}
					else if(item.contains("§7Current tick: ")) {
						tick = Integer.parseInt(item.replace("§7Current tick: ", ""));
					}
				}
				if(event.getClick().isShiftClick()) {
					MusicEditor(player, songName, tick + 10);
				}
				else {
					MusicEditor(player, songName, tick + 1);
				}
			}
			if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "§7Précédent", "§7Previous"))) {
				String songName = "";
				int tick = 0;
				for(String item: inv.getItem(4).getItemMeta().getLore()) {
					if(item.contains("§7Music Name: ")) {
						songName = item.replace("§7Music Name: ", "");
					}
					else if(item.contains("§7Current tick: ")) {
						tick = Integer.parseInt(item.replace("§7Current tick: ", ""));
					}
				}
				if(event.getClick().isShiftClick()) {
					if(tick - 10 > 0) {
						MusicEditor(player, songName, tick - 10);
					}
					else {
						MusicEditor(player, songName, 1);
					}
				}
				else {
					if(tick - 1 > 0) {
						MusicEditor(player, songName, tick - 1);
					}
					else {
						player.sendMessage("§cYou cannot go under 1 tick !");
					}
				}
				
			}
			if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "§aPlay the tick moment", "§aPlay the tick moment"))) {
				String songName = "";
				int tick = 0;
				for(String item: inv.getItem(4).getItemMeta().getLore()) {
					if(item.contains("§7Music Name: ")) {
						songName = item.replace("§7Music Name: ", "");
					}
					else if(item.contains("§7Current tick: ")) {
						tick = Integer.parseInt(item.replace("§7Current tick: ", ""));
					}
				}
				
				ArrayList<Sound> list2 = new ArrayList<>();
				list2.add(Sound.BLOCK_NOTE_BASEDRUM);
				list2.add(Sound.BLOCK_NOTE_BASS);
				list2.add(Sound.BLOCK_NOTE_BELL);
				list2.add(Sound.BLOCK_NOTE_CHIME);
				list2.add(Sound.BLOCK_NOTE_FLUTE);
				list2.add(Sound.BLOCK_NOTE_GUITAR);
				list2.add(Sound.BLOCK_NOTE_HARP);
				list2.add(Sound.BLOCK_NOTE_HAT);
				list2.add(Sound.BLOCK_NOTE_PLING);
				list2.add(Sound.BLOCK_NOTE_SNARE);
				list2.add(Sound.BLOCK_NOTE_XYLOPHONE);
				
				File file = new File("plugins/Menelia/songs/" + songName + ".yml");
				
				FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
				
				int i = -1;
				for(String item: list1) {
					i++;
					if(fileConfig.isSet(tick + "." + item)) {
						player.playSound(player.getLocation(), list2.get(i), 5F, (float) fileConfig.getDouble(tick + "." + item));
					}
				}
				
			}
			if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "§aJouer la musique", "§aPlay the music"))) {
				String songName = "";
				for(String item: inv.getItem(4).getItemMeta().getLore()) {
					if(item.contains("§7Music Name: ")) {
						songName = item.replace("§7Music Name: ", "");
					}
				}
				MusicReader task = new MusicReader(player, songName);
				task.runTaskTimer(Main.getInstance(), 0, 1);
			}
			
			if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "§aFermer", "§aClose"))) {
				player.closeInventory();
			}
			
			for(String item1: list1) {
				if(current.getItemMeta().getDisplayName().equals("§a" + item1)) {
					String songName = "";
					int tick = 0;
					for(String item2: inv.getItem(4).getItemMeta().getLore()) {
						if(item2.contains("§7Music Name: ")) {
							songName = item2.replace("§7Music Name: ", "");
						}
						else if(item2.contains("§7Current tick: ")) {
							tick = Integer.parseInt(item2.replace("§7Current tick: ", ""));
						}
					}
					File file = new File("plugins/Menelia/songs/" + songName + ".yml");
					
					FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
					
					double pitch = 0;
					if(current.getItemMeta().hasLore()) {
						for(String item2: current.getItemMeta().getLore()) {
							pitch = Double.parseDouble(item2);
						}
					}
					
					if(event.getClick().isShiftClick()) {
						fileConfig.set(tick + "." + item1, null);
					}
					else if(event.getClick().isLeftClick()) {
						if(pitch + 0.1 <= 2) {
							pitch = pitch + 0.1;
							DecimalFormat df = new DecimalFormat("#.#");
							pitch = Double.parseDouble(df.format(pitch));
							fileConfig.set(tick + "." + item1, pitch);	
						}
						else {
							fileConfig.set(tick + "." + item1, null);
						}
					}
					else if(event.getClick().isRightClick()){
						if(pitch - 0.1 >= 0) {
							pitch = pitch - 0.1;
							DecimalFormat df = new DecimalFormat("#.#");
							pitch = Double.parseDouble(df.format(pitch));
							fileConfig.set(tick + "." + item1, pitch);	
						}
						else {
							fileConfig.set(tick + "." + item1, null);
						}
					}
					try {
						fileConfig.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String songName2 = songName;
					int tick2 = tick;
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							MusicEditor(player, songName2, tick2);
						}
					}, 1);
					
				}
			}
			
			
		}
	}
}
