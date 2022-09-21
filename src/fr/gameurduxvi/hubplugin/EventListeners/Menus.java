package fr.gameurduxvi.hubplugin.EventListeners;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.gameurduxvi.hubplugin.Main;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;
import fr.gameurduxvi.meneliaapi.Databases.Accounts;
import fr.gameurduxvi.meneliaapi.Databases.Stats.TNTSmash;
import fr.gameurduxvi.meneliaapi.MySQL.MySQL;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Menus implements Listener {
	@EventHandler
	public void OnInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack it = event.getItem();
		
		if(it == null) return;
		
		if(player.getWorld().getName().equalsIgnoreCase(Main.getInstance().HubLocation.getWorld().getName()) ) {
			if(it.getType() == Material.SKULL_ITEM) {
				if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
					if(it.hasItemMeta()) {
						if(it.getItemMeta().getDisplayName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "§bProfil", "§bProfile"))) {
							InventoryProfile(player);
						}
					}
				}
			}
			else if(it.getType() == Material.NETHER_STAR) {
				if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
					if(it.hasItemMeta()) {
						if(it.getItemMeta().getDisplayName().equalsIgnoreCase(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "§bMenu des jeux", "§bGame Menu")))) {
							inventoryGames(player);
						}
					}
				}
			}
			else if(it.getType() == Material.EMERALD) {
				if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
					if(it.hasItemMeta()) {
						if(it.getItemMeta().getDisplayName().equalsIgnoreCase(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "§bBoutique", "§bShop")))) {
							inventoryShop(player);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void OnClick(InventoryClickEvent event) {
		
		Inventory inv = event.getInventory();
		Player player = (Player)event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		
		if(current == null) return;
		
		if(inv.getName().equalsIgnoreCase("Stock")) {
			event.setCancelled(true);
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "Statistiques >> TNTSmash", "Statistics >> TNTSmash"))) {
			event.setCancelled(true);
			switch(current.getType()) {
				case ARROW:
					InventoryStatistics(player);
					break;
				default:
					break;
			}
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "Statistiques", "Statistics"))){
			event.setCancelled(true);
			switch(current.getType()) {
				case TNT:
					InventoryStatisticsTNTSmash(player);
					break;
				case FEATHER:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case SKULL_ITEM:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case BRICK:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case BARRIER:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case ARROW:
					InventoryProfile(player);
					break;
				default: 
					break;
			}
		}
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "§B§1§rAscension", "§B§1§rAscension"))){
			event.setCancelled(true);
			switch(current.getType()) {
				case PAPER:
					MySQL.INSERT_GAME_JOINER(player.getUniqueId().toString(), "beta", "ascension", current.getItemMeta().getDisplayName());
					MeneliaAPI.getBungeecordInstance().sendToServer(player, "beta");
					break;
				case ARROW:
					inventoryGames(player);
					break;
				default:
					break;
			}
		}
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "Boutique", "Shop"))){
			event.setCancelled(true);
			switch(current.getType()) {
				case ENDER_CHEST:					
					/*Bukkit.broadcastMessage("event");
					ItemStack item = new ItemStack(Material.ENDER_CHEST);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bCosmétiques", "§bCosmetics"));
					item.setItemMeta(meta);
					player.getInventory().addItem(item);
					PlayerInteractEvent callEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, player.getLocation().getBlock(), BlockFace.DOWN);
					Bukkit.getPluginManager().callEvent(callEvent);*/
					/*ItemStack item = new ItemStack(Material.NETHER_STAR);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bMenu des jeux", "§bGame Menu"));
					item.setItemMeta(meta);
					PlayerInteractEvent callEvent2 = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, player.getLocation().getBlock(), BlockFace.UP);
					Bukkit.getPluginManager().callEvent(callEvent2);*/
					break;
				default: 
					break;
			}
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "Langues", "Languages"))) {
			event.setCancelled(true);
			switch(current.getType()) {
				case ARROW:
					InventoryProfile(player);
					break;
				case BANNER:
					if(current.getItemMeta().getDisplayName().contains("Français")) {
						for(Accounts account: Main.getInstance().getAccounts()) {
							if(account.getName().equals(player.getName())) {
								account.setLang("FR");
								InventoryProfile(player);
								Main.getInstance().HubInventory(player);
								Main.getInstance().refreshScoreboard(player);
							}
						}
					}
					else if(current.getItemMeta().getDisplayName().contains("English")) {
						for(Accounts account: Main.getInstance().getAccounts()) {
							if(account.getName().equals(player.getName())) {
								account.setLang("EN");
								InventoryProfile(player);
								Main.getInstance().HubInventory(player);
								Main.getInstance().refreshScoreboard(player);
							}
						}
					}
					else {
						player.sendMessage("&cAn error occured !");
						player.closeInventory();
					}
					break;
				default: 
					break;
			}
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase("Profile")) {
			event.setCancelled(true);
			switch(current.getType()) {
				case SKULL_ITEM:
					if(current.getItemMeta().getDisplayName().equals(MeneliaAPI.getInstance().getLang(player, "Langues", "Languages"))) {
						InventoryLanguage(player);
					}
					break;
				case BREWING_STAND_ITEM:
					InventoryStatistics(player);
					break;
				case ARMOR_STAND:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case BANNER:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case END_CRYSTAL:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				default: 
					break;
			}
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase(MeneliaAPI.getInstance().getLang(player, "Menu des jeux", "Game Menu"))) {
			
			event.setCancelled(true);
			
			switch(current.getType()) {
				case COMMAND:
					player.performCommand("admin");
					break;
				case COMPASS:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case TNT:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case FEATHER:
					inventoryGameAscension(player);
					//player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case SKULL_ITEM:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case BRICK:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				case BARRIER:
					player.sendMessage(MeneliaAPI.getInstance().getLang(player, "§cCette fonction est temporairement indisponible !", "§cThis feature is temporarily unavailable !"));
					break;
				default: 
					break;
			}
		}
		
		
		
		else if(inv.getName().equalsIgnoreCase("TNTSmash")) {
			event.setCancelled(true);
			
			switch(current.getType()) {
				case PAPER:
					String str = current.getItemMeta().getDisplayName();
					int numberOnly= Integer.parseInt(str.replaceAll("[^0-9]", ""));
					String[] args = {player.getName() + "", "tntsmash", "" + numberOnly};
					MeneliaAPI.getBungeecordInstance().SendPluginMessage(player, "games", "MeneliaChannel", "RequestGame", true, args);
					
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							MeneliaAPI.getBungeecordInstance().sendToServer(player, "games");
						}
					}, 10);		
					
					
					/*ByteArrayDataOutput out = ByteStreams.newDataOutput();
					
					out.writeUTF("RequestGame");
					out.writeUTF(player.getUniqueId().toString());
					out.writeUTF("TNTSmash");
					out.writeInt(numberOnly);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, ()->{
						player.sendPluginMessage(Main.INSTANCE, "MeneliaChannel", out.toByteArray());
					}, 3L);*/
					
					break;
				case ENDER_CHEST:
					String[] args2 = {player.getName(), "tntsmash", "5"};
					MeneliaAPI.getBungeecordInstance().SendPluginMessage(player, "games", "MeneliaChannel", "RequestGame", true, args2);
					break;
				default: 
					break;
			}
		}
		
		
		
		else if(player.getWorld().getName().equals(Main.getInstance().HubLocation.getWorld().getName())) {
			if(player.getGameMode().equals(GameMode.ADVENTURE)||player.getGameMode().equals(GameMode.SURVIVAL)) {
				event.setCancelled(true);
			}
		}
	}
	
	public void inventoryDefault(Inventory inv) {
		ItemStack item = EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 7, "§a", "");
		inv.setItem(0, item);
		inv.setItem(1, item);
		inv.setItem(2, item);
		inv.setItem(3, item);
		inv.setItem(4, item);
		inv.setItem(5, item);
		inv.setItem(6, item);
		inv.setItem(7, item);
		inv.setItem(8, item);
		
		inv.setItem(9, item);
		inv.setItem(18, item);
		inv.setItem(27, item);
		inv.setItem(36, item);
		
		
		inv.setItem(17, item);
		inv.setItem(26, item);
		inv.setItem(35, item);
		inv.setItem(44, item);
		
		inv.setItem(45, item);
		inv.setItem(46, item);
		inv.setItem(47, item);
		inv.setItem(48, item);
		inv.setItem(49, item);
		inv.setItem(50, item);
		inv.setItem(51, item);
		inv.setItem(52, item);
		inv.setItem(53, item);
	}
	
	public void inventoryShop(Player player) {
		Inventory invMenu = Bukkit.createInventory(null, 54, MeneliaAPI.getInstance().getLang(player, "Boutique", "Shop"));
		
		inventoryDefault(invMenu);
		
		invMenu.setItem(20, EventListener.getItem(Material.FEATHER, 1, 0, "§5Ascension", "", true));
		invMenu.setItem(21, EventListener.getItem(Material.TNT, 1, 0, "§4TNT§6Smash", "", true));
		invMenu.setItem(22, EventListener.getItem(Material.SKULL_ITEM, 1, 1, "Murder", "", true));
		invMenu.setItem(23, EventListener.getItem(Material.BRICK, 1, 0, "FakeBuilder", "", true));
		invMenu.setItem(24, EventListener.getItem(Material.BARRIER, 1, 0, "", "", true));
		
		invMenu.setItem(37, EventListener.getItem(Material.CHEST, 1, 0, "§5Mene§9Box", ""));
		invMenu.setItem(38, EventListener.getItem(Material.TRIPWIRE_HOOK, 1, 0, MeneliaAPI.getInstance().getLang(player, "§5Mene§9Clé", "§5Mene§9Key"), ""));
									
		invMenu.setItem(43, EventListener.getItem(Material.ENDER_CHEST, 1, 0,MeneliaAPI.getInstance().getLang(player, "§bCosmétiques", "§bCosmetics") , ""));
		
		invMenu.setItem(49, EventListener.getItem(Material.BEACON, 1, 0, MeneliaAPI.getInstance().getLang(player, "§b§lBoutique en ligne", "§b§lShop online"), ""));
		
		player.openInventory(invMenu);
	}
	
	public void inventoryGames(Player player) {
		Inventory invMenu = Bukkit.createInventory(null, 54, MeneliaAPI.getInstance().getLang(player, "Menu des jeux", "Game Menu"));
		
		inventoryDefault(invMenu);
		
		invMenu.setItem(20, EventListener.getItem(Material.FEATHER, 1, 0, "§5Ascension", "", true));
		invMenu.setItem(22, EventListener.getItem(Material.TNT, 1, 0, "§4TNT§6Smash", "", true));
		invMenu.setItem(24, EventListener.getItem(Material.SKULL_ITEM, 1, 1, "Murder", "", true));
		invMenu.setItem(30, EventListener.getItem(Material.BRICK, 1, 0, "FakeBuilder", "", true));
		invMenu.setItem(32, EventListener.getItem(Material.BARRIER, 1, 0, "", "", true));
		
		invMenu.setItem(45, EventListener.getItem(Material.COMPASS, 1, 0, "Lobbies", "", true));
		if(PermissionsEx.getUser(player).has("admin.open")) {
			invMenu.setItem(53, EventListener.getItem(Material.COMMAND, 1, 0, "StaffMenu", "", true));
		}						
	
		player.openInventory(invMenu);
	}
	
	public void inventoryGameAscension(Player player) {
		String invName = MeneliaAPI.getInstance().getLang(player, "§B§1§rAscension", "§B§1§rAscension");
		Inventory inv = Bukkit.createInventory(null, 54, invName);					
		
		inventoryDefault(inv);			    	
		
		ResultSet result = MySQL.GET_RESULT(MySQL.Table_Games, "WHERE `" + MySQL.Field_Games_GAME_TYPE + "` = 'ascension' AND `" + MySQL.Field_Games_GAME_STATUS + "` = '0' ORDER BY `" + MySQL.Field_Games_Content_GAME_ID + "` ASC");
		int i = 0;
		try {
			while(result.next()) {
				i++;
				if(i == 8 || i == 17 || i == 26 || i == 35) {
					i++;
					i++;
				}
				ItemStack item = new ItemStack(Material.PAPER);
				ItemMeta meta = item.getItemMeta();
				
				meta.setDisplayName(result.getString(MySQL.Field_Games_GAME_ID));
				
				ResultSet result2 = MySQL.GET_RESULT(MySQL.Table_Games_Content, "WHERE `" + MySQL.Field_Games_Content_GAME_TYPE + "` = 'ascension' AND `" + MySQL.Field_Games_Content_GAME_ID + "` = '" + result.getString(MySQL.Field_Games_GAME_ID) + "'");
				
				int amount = 0;
				while(result2.next()) {
					amount++;
				}
				
				ArrayList<String> lore = new ArrayList<>();
				
				lore.add("§7" + amount + "/" + result.getInt(MySQL.Field_Games_GAME_MAX));
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				inv.setItem(9 + i, item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ItemStack back = new ItemStack(Material.ARROW);
    	ItemMeta metaBack = back.getItemMeta();
    	
    	metaBack.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§7Retour", "§7Back"));
    	metaBack.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	
    	back.setItemMeta(metaBack);
    	
    	inv.setItem(49, back);	
    	
    	player.openInventory(inv);
    	
    	Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(player.getOpenInventory().getTitle().equals(inv.getName())) {
					inventoryGameAscension(player);
				}
			}
		}, 100);
	}
	
	@SuppressWarnings("deprecation")
	public void InventoryLanguage(Player player) {
		Inventory invMenu = Bukkit.createInventory(null, 54, MeneliaAPI.getInstance().getLang(player, "Langues", "Languages"));
		
		inventoryDefault(invMenu);
		
		ItemStack banniere1 = new ItemStack(Material.BANNER, 1);
		BannerMeta meta1 = (BannerMeta) banniere1.getItemMeta();
		meta1.setBaseColor(DyeColor.WHITE);
		meta1.setDisplayName(MeneliaAPI.getInstance().colorize("&bFrançais"));
		List<Pattern> patterns1 = new ArrayList<Pattern>();
		patterns1.add(new Pattern(DyeColor.RED, PatternType.getByIdentifier("rs")));
		patterns1.add(new Pattern(DyeColor.BLUE, PatternType.getByIdentifier("ls")));
		patterns1.add(new Pattern(DyeColor.BLUE, PatternType.getByIdentifier("ls")));
		patterns1.add(new Pattern(DyeColor.RED, PatternType.getByIdentifier("rs")));
		meta1.setPatterns(patterns1);
		meta1.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		banniere1.setItemMeta(meta1);
		invMenu.setItem(21, banniere1);
		
		ItemStack banniere2 = new ItemStack(Material.BANNER, 1);
		BannerMeta meta2 = (BannerMeta) banniere2.getItemMeta();
		meta2.setBaseColor(DyeColor.BLUE);
		meta2.setDisplayName(MeneliaAPI.getInstance().colorize("&bEnglish"));
		List<Pattern> patterns2 = new ArrayList<Pattern>();
		patterns2.add(new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("drs")));
		patterns2.add(new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("dls")));
		patterns2.add(new Pattern(DyeColor.RED, PatternType.getByIdentifier("cr")));
		patterns2.add(new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("cs")));
		patterns2.add(new Pattern(DyeColor.WHITE, PatternType.getByIdentifier("ms")));
		patterns2.add(new Pattern(DyeColor.RED, PatternType.getByIdentifier("sc")));
		meta2.setPatterns(patterns2);
		meta2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		banniere2.setItemMeta(meta2);
		invMenu.setItem(23, banniere2);
		
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrowM = arrow.getItemMeta();
		arrowM.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "&7Retour", "&7Back")));
		arrow.setItemMeta(arrowM);
		invMenu.setItem(49, arrow);
		
		player.openInventory(invMenu);
	}
	
	@SuppressWarnings("deprecation")
	public void InventoryProfile(Player player) {
		Inventory invMenu = Bukkit.createInventory(null, 54, "Profile");
		
		inventoryDefault(invMenu);
				
		invMenu.setItem(20, EventListener.getItem(Material.ARMOR_STAND, 1, 0, MeneliaAPI.getInstance().getLang(player, "Amis", "Friends"), "", true));
		invMenu.setItem(22, EventListener.getItem(Material.BANNER, 1, 0, MeneliaAPI.getInstance().getLang(player, "Guilde", "Guild"), "", true));
		invMenu.setItem(24, EventListener.getItem(Material.BREWING_STAND_ITEM, 1, 0, MeneliaAPI.getInstance().getLang(player, "Statistiques", "Statistics"), "", true));
		
		ItemStack head1 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta headMeta1 = (SkullMeta) head1.getItemMeta();
		headMeta1.setOwner("0qt");
		headMeta1.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "Langues", "Languages")));
		headMeta1.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		headMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		head1.setItemMeta(headMeta1);
		invMenu.setItem(29, head1);
		
		ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta headMeta2 = (SkullMeta) head2.getItemMeta();
		headMeta2.setOwner(player.getName());
		headMeta2.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "Info du joueur", "Player info")));
		headMeta2.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		headMeta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		int moneyBase = 0;
		int moneySpecial = 0;
		for(Accounts account: MeneliaAPI.getInstance().getAccounts()) {
			if(account.getUUID().equals(player.getUniqueId())) {
				moneyBase = account.getMoneyBase();
				moneySpecial = account.getMoneySpecial();
			}
		}		
		ArrayList<String> list = new ArrayList<>();
		list.add(MeneliaAPI.getInstance().getLang(player, "§7Langue: §bFrançais", "§7Language: §bEnglish"));
		list.add(MeneliaAPI.getInstance().getLang(player, "§3Astrolys§7: §b" + moneyBase, "§3Astrolys§7: §b" + moneyBase));
		list.add(MeneliaAPI.getInstance().getLang(player, "§6Staralys§7: §b" + moneySpecial, "§6Astrolys§7: §b" + moneySpecial));
		headMeta2.setLore(list);
		head2.setItemMeta(headMeta2);
		invMenu.setItem(31, head2);
		
		invMenu.setItem(33, EventListener.getItem(Material.END_CRYSTAL, 1, 0, MeneliaAPI.getInstance().getLang(player, "Progès", "Progress"), "", true));
		
		
								
		player.openInventory(invMenu);
	}
	
	public void InventoryStatistics(Player player) {
		Inventory invMenu = Bukkit.createInventory(null, 54, MeneliaAPI.getInstance().getLang(player, "Statistiques", "Statistics"));
		
		inventoryDefault(invMenu);
		
		invMenu.setItem(20, EventListener.getItem(Material.FEATHER, 1, 0, "§5Ascension", "", true));
		invMenu.setItem(22, EventListener.getItem(Material.TNT, 1, 0, "§4TNT§6Smash", "", true));
		invMenu.setItem(24, EventListener.getItem(Material.SKULL_ITEM, 1, 1, "Murder", "", true));
		invMenu.setItem(30, EventListener.getItem(Material.BRICK, 1, 0, "FakeBuilder", "", true));
		invMenu.setItem(32, EventListener.getItem(Material.BARRIER, 1, 0, "", "", true));
		
		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrowM = arrow.getItemMeta();
		arrowM.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "&7Retour", "&7Back")));
		arrow.setItemMeta(arrowM);
		invMenu.setItem(49, arrow);
		
		player.openInventory(invMenu);
	}
	
	public void InventoryStatisticsTNTSmash(Player player){
		Inventory invMenu = Bukkit.createInventory(null, 54, MeneliaAPI.getInstance().getLang(player, "Statistiques >> TNTSmash", "Statistics >> TNTSmash"));
		
		inventoryDefault(invMenu);
		
		for(TNTSmash data: MeneliaAPI.getInstance().getStatTNTSmash()) {
			if(data.getUuid().equals(player.getUniqueId().toString())) {
				invMenu.setItem(20, EventListener.getItem(Material.DIAMOND_SWORD, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bTotal Tués: " + data.getKills(), "&bTotal Kills: " + data.getKills()), ""));
	
				invMenu.setItem(22, EventListener.getItem(Material.STICK, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations au Baton: " + data.getMethodStick(), "&bEleminations with the Stick: " + data.getMethodStick()), ""));
				invMenu.setItem(23, EventListener.getItem(Material.TNT, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations a la TNT: " + data.getMethodExplosion(), "&bEleminations with the TNT: " + data.getMethodExplosion()), ""));
				invMenu.setItem(24, EventListener.getItem(Material.BOW, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations a la l'arc: " + data.getMethodArrow(), "&bEleminations with the bow: " + data.getMethodArrow()), ""));
	
				invMenu.setItem(29, EventListener.getItem(Material.PAPER, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bParties gagnés: " + data.getWins(), "&bGames Wins: " + data.getWins()), ""));
				
				invMenu.setItem(31, EventListener.getItem(Material.SNOW_BALL, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations a la Grenade: " + data.getMethodSnowball(), "&bEleminations with the Grenade: " + data.getMethodSnowball()), ""));
				invMenu.setItem(32, EventListener.getItem(Material.BLAZE_ROD, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations au Baton de Blaze: " + data.getMethodBlazeRod(), "&bEleminations with the Blaze Rod: " + data.getMethodBlazeRod()), ""));
				invMenu.setItem(33, EventListener.getItem(Material.FISHING_ROD, 1, 0, MeneliaAPI.getInstance().getLang(player, "&bEliminations a la Canne à Peche: " + data.getMethodFishing(), "&bEleminations with the Fishing Rod: " + data.getMethodFishing()), ""));
				
				ItemStack arrow = new ItemStack(Material.ARROW, 1);
				ItemMeta arrowM = arrow.getItemMeta();
				arrowM.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, "&7Retour", "&7Back")));
				arrow.setItemMeta(arrowM);
				invMenu.setItem(49, arrow);
				
				
			}
		}
		player.openInventory(invMenu);
	}
	
}
