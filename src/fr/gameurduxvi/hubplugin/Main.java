package fr.gameurduxvi.hubplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.hubplugin.EventListeners.EventListener;
import fr.gameurduxvi.hubplugin.EventListeners.Menus;
import fr.gameurduxvi.hubplugin.config.config;
import fr.gameurduxvi.hubplugin.config.loots;
import fr.gameurduxvi.hubplugin.tasks.HeaderAndFooter;
import fr.gameurduxvi.hubplugin.tasks.MeneboxTimer;
import fr.gameurduxvi.hubplugin.tasks.NameTag;
import fr.gameurduxvi.meneliaapi.MeneliaAPI;
import fr.gameurduxvi.meneliaapi.Databases.Accounts;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin {
	
	// Main instance plugin
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	// Other instances
	private EventListener eventListener;
	private Menus menus;
	
	public EventListener getEventListener() {
		return eventListener;
	}
	
	public Menus getMenus() {
		return menus;
	}
	
	// Plugin properties
	public String pluginPrefix = "§6[HubPlugin]";
	private ScoreboardManager manager;
	
	public ArrayList<Accounts> getAccounts(){
		//return this.accounts;
		return MeneliaAPI.getInstance().getAccounts();
	}
	
	// Databases
	public Location HubLocation;	
	
	//private ArrayList<AdvancementsMenelia> advancements = new ArrayList<>();
	
	
	
	/*public ArrayList<AdvancementsMenelia> getAdvancementsMenelia(){
		return this.advancements;
	}*/
	
	
	
	
	//=======================================================================================================
	// 
	// Onload and Unload Plugin Actions
	// 
	//=======================================================================================================
	
	
	
	
	@Override
	public void onEnable() {
		// Plugin loading message
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(pluginPrefix + " Enabling HubPlugin");
		Bukkit.getConsoleSender().sendMessage(pluginPrefix + "=========================================================");
		
		// Main plugin instance
		instance = this;
		
		// Saving other instances
		eventListener = new EventListener();
		menus = new Menus();
		
		// Saving other instances
		config ConfigClass = new config();
		loots LootsClass = new loots();
				
		ConfigClass.load();
		LootsClass.load();
				
		// Command event listener
		getCommand("hub").setExecutor(new CommandListener());
		getCommand("sethub").setExecutor(new CommandListener());
		getCommand("chest").setExecutor(new CommandListener());
		getCommand("music").setExecutor(new CommandListener());
		getCommand("editmusic").setExecutor(new CommandListener());
		
		// Loading EventListeners
		getServer().getPluginManager().registerEvents(eventListener, this);
		getServer().getPluginManager().registerEvents(menus, this);
		
		// Channel loader
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageListener());
		getServer().getMessenger().registerOutgoingPluginChannel(this, "MeneliaChannel");
		getServer().getMessenger().registerIncomingPluginChannel(this, "MeneliaChannel", new PluginCustomMessageListener());
		
		// Setting up plugin properties
		HubLocation = new Location(Bukkit.getWorld(config.getConfig().getString("HubWorldName")), config.getConfig().getInt("HubLocation.x"),config.getConfig().getInt("HubLocation.y"), config.getConfig().getInt("HubLocation.z"));
		manager = Bukkit.getScoreboardManager();
		
		// Starting Tasks
		HeaderAndFooter task1 = new HeaderAndFooter();
		task1.runTaskTimer(this, 0, 10);
		
		NameTag task2 = new NameTag();
		task2.runTaskTimer(this, 0, 100);
		
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
					HubInventory(loopPlayer);
					refreshScoreboard(loopPlayer);
				}
			}
		}, 20);
		
		
		
		/*for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
		ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
		
		out1.writeUTF("GetProfile");
		out1.writeUTF(loopPlayer.getUniqueId().toString());
		Bukkit.getConsoleSender().sendMessage(MeneliaAPI.getInstance().colorize(Main.INSTANCE.pluginPrefix + " &6Sending request for account \"" + loopPlayer.getName() +"\" to bungeecord..."));
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, ()->{
			loopPlayer.sendPluginMessage(Main.INSTANCE, "MeneliaChannel", out1.toByteArray());
		}, 3L);
		}*/

		//sendMessageToThePlugin("GetServer", null, false, null);
		/*
		
		for(String string: getConfig().getConfigurationSection("kits").getKeys(false)) {
			System.out.println(getConfig().getConfigurationSection("kits").getInt(string + ".id"));
		}
		
		for(String string: getConfig().getStringList("kits")) {
			System.out.println(string);
		}
		*/		
		Bukkit.getConsoleSender().sendMessage(pluginPrefix + "=========================================================");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("");
	}		
	
	@Override
	public void onDisable() {
		// Plugin unloading Message
		Bukkit.getConsoleSender().sendMessage(MeneliaAPI.getInstance().colorize(pluginPrefix + " Disabling HubPlugin"));
		
		// Plugin unloading Message
		for(Player loopPlayer: Bukkit.getOnlinePlayers()) {
			loopPlayer.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void refreshScoreboard(Player loopPlayer) {
			Scoreboard board = manager.getNewScoreboard();
			
			/*for(Team loopTeam: board.getTeams()) {
				loopTeam.unregister();
			}*/
			for(Player lp: Bukkit.getOnlinePlayers()) {
				String tab = PermissionsEx.getPermissionManager().getGroup(MeneliaAPI.getFunctions().getGroup(lp)).getOption("tabprefix");				
				int priority = Integer.parseInt(PermissionsEx.getPermissionManager().getGroup(MeneliaAPI.getFunctions().getGroup(lp)).getOption("priority"));
				
				Team team = board.registerNewTeam(priority + "_" + lp.getName());
				team.setPrefix(MeneliaAPI.getInstance().colorize("§" + priority + tab + " "));
				team.addPlayer(lp);
			}
			
			for(Objective loopObjective: board.getObjectives())
			{
				if(loopObjective.getName().equals(loopPlayer.getName())) {
					loopObjective.unregister();
				}
			}
			Objective objective = board.registerNewObjective(loopPlayer.getName(), "dummy");
			
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			objective.setDisplayName("&f[&5play&f.&5menelia&f.&5fr&f]".replace("&", "§"));
			
			String colorname = PermissionsEx.getPermissionManager().getGroup(MeneliaAPI.getFunctions().getGroup(loopPlayer)).getOption("colorname");	
			
			Score score1 = objective.getScore("§a");
			Score score2 = objective.getScore("§b⇨ §" + colorname + loopPlayer.getName());
			Score score3 = objective.getScore("§b⇨ §7Grade : §" + colorname + MeneliaAPI.getFunctions().getGroup(loopPlayer));
			Score score4 = objective.getScore("§b");
			Score score5 = objective.getScore("§b⇨ §7" + MeneliaAPI.getInstance().getLang(loopPlayer, "Connectés", "Connected") + " : §b" + Bukkit.getOnlinePlayers().size());
			//Score score2 = objective.getScore("&b> &3".replace("&", "§") + MeneliaAPI.getInstance().getLang(loopPlayer, "Joueurs connectés", "Players connected") + " : " + MeneliaAPI.getInstance().getOtherServerOnlinePlayers("51.38.240.221", 25565));
			Score score7 = objective.getScore("§b⇨ §7Lobby : §b01");
			Score score8 = objective.getScore("§d");
			int moneyBase = 0;
			int moneySpecial = 0;
			for(Accounts account: getAccounts()) {
				if(account.getName().equals(loopPlayer.getName())) {
					moneyBase = account.getMoneyBase();
					moneySpecial = account.getMoneySpecial();
				}
			}
			Score score9 = objective.getScore("§b⇨ §3Astrolys §7: §b" + moneyBase);
			Score score10 = objective.getScore("§b⇨ §6Staralys §7: §b" + moneySpecial);
			Score score11 = objective.getScore("§e");

			score1.setScore(10);
			score2.setScore(9);
			score3.setScore(8);
			score4.setScore(7);
			score5.setScore(6);
			score7.setScore(5);
			score8.setScore(4);
			score9.setScore(3);
			score10.setScore(2);
			score11.setScore(1);
			loopPlayer.setScoreboard(board);	
	}
	
	public void Hub(Player player) {
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().clear();
		HubInventory(player);	
		
		player.teleport(HubLocation);
	}
	
	@SuppressWarnings("deprecation")
	public void HubInventory(Player player) {
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setExp(0);
		player.setLevel(0);
		
		ItemStack PlayerHead = new ItemStack(org.bukkit.Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta customMPlayerHead = (SkullMeta)PlayerHead.getItemMeta();
		customMPlayerHead.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bProfil", "§bProfile"));	
		customMPlayerHead.setOwner(player.getName());
		PlayerHead.setItemMeta(customMPlayerHead);
		player.getInventory().setItem(0, PlayerHead);
		
		ItemStack NetherStar = new ItemStack(org.bukkit.Material.NETHER_STAR, 1);
		ItemMeta customMNetherStar = NetherStar.getItemMeta();	
		customMNetherStar.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bMenu des jeux", "§bGame Menu"));		
		NetherStar.setItemMeta(customMNetherStar);
		player.getInventory().setItem(4, NetherStar);
		
		ItemStack Emerald = new ItemStack(org.bukkit.Material.EMERALD, 1);
		ItemMeta customMEmerald = Emerald.getItemMeta();	
		customMEmerald.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bBoutique", "§bShop"));		
		Emerald.setItemMeta(customMEmerald);
		player.getInventory().setItem(7, Emerald);
		
		ItemStack EnderChest = new ItemStack(org.bukkit.Material.ENDER_CHEST, 1);
		ItemMeta customMEnderChest = EnderChest.getItemMeta();	
		customMEnderChest.setDisplayName(MeneliaAPI.getInstance().getLang(player, "§bCosmétiques", "§bCosmetics"));		
		EnderChest.setItemMeta(customMEnderChest);
		player.getInventory().setItem(8, EnderChest);
		
		player.updateInventory();
	}
	
	public void openMenebox(Player player, String level) {
		Inventory invMenu = Bukkit.createInventory(null, 27, "Stock");
		
		int i = 0;
		while(i<9) {
			i++;
			player.openInventory(invMenu);
			
			invMenu.setItem(0, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(1, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(2, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(3, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(4, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(5, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(6, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(7, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(8, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			
			invMenu.setItem(18, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(19, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(20, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(21, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(22, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(23, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(24, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(25, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			invMenu.setItem(26, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 10, "&a", ""));
			
			invMenu.setItem(4, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
			invMenu.setItem(18+4, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
			
			if(level.equals("2")||level.equals("3")) {
				invMenu.setItem(2, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
				invMenu.setItem(18+2, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
			}
			else {
				invMenu.setItem(2, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 14, "&a", ""));
				invMenu.setItem(18+2, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 14, "&a", ""));
			}
			
			if(level.equals("3")) {
				invMenu.setItem(6, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
				invMenu.setItem(18+6, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 5, "&a", ""));
			}
			else {
				invMenu.setItem(6, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 14, "&a", ""));
				invMenu.setItem(18+6, EventListener.getItem(Material.STAINED_GLASS_PANE, 1, 14, "&a", ""));
			}
			
			String category = openMeneboxGetCategory();
			String loot = openMeneboxGetLoot(category);
			invMenu.setItem(i + 8, openMeneboxGetItem(player, invMenu, category, loot));
		}
		MeneboxTimer task = new MeneboxTimer(player, invMenu, level);
		task.runTaskTimer(this, 0, 1);
	}
	
	public String openMeneboxGetCategory() {
		Random random = new Random();
		List<String> list = new ArrayList<>();
		
		for(String lootsCategory: loots.getConfig().getConfigurationSection("").getKeys(false)) {
			list.add(lootsCategory);	
		}
		
		String element = list.get(random.nextInt(list.size()));
		
		return element;
	}
	
	public String openMeneboxGetLoot(String category) {
		Random random = new Random();
		List<String> list = new ArrayList<>();
		for(String loots: loots.getConfig().getConfigurationSection(category).getKeys(false)) {
			list.add(loots);	
		}
		
		String element = list.get(random.nextInt(list.size()));
		
		return element;
	}
		
	@SuppressWarnings("deprecation")
	public ItemStack openMeneboxGetItem(Player player, Inventory inv, String category, String loot) {		
		if(category.equals("pets")) {
			if(loots.getConfig().getString(category + "." + loot + ".item").equals("spawn_egg")) {
				String type = MeneliaAPI.getInstance().colorize(loots.getConfig().getString(category + "." + loot + ".nbt"));
				ItemStack It = new ItemStack(Material.MONSTER_EGG, 1, EntityType.fromName(type).getTypeId());
				ItemMeta metaIt = It.getItemMeta();
				
				metaIt.setDisplayName(MeneliaAPI.getInstance().colorize(MeneliaAPI.getInstance().getLang(player, loots.getConfig().getString(category + "." + loot + ".nameFR"), loots.getConfig().getString(category + "." + loot + ".nameEN"))));
				metaIt.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				
				It.setItemMeta(metaIt);
				return It;
			}
		}
		else {
			ItemStack It = new ItemStack(Material.GOLD_NUGGET, 1);
			ItemMeta metaIt = It.getItemMeta();
			
			metaIt.setDisplayName(MeneliaAPI.getInstance().colorize(loots.getConfig().getString(category + "." + loot + ".amount") + "" + loots.getConfig().getString(category + "." + loot + ".type")));
			metaIt.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			
			It.setItemMeta(metaIt);
			return It;
		}
		ItemStack It = new ItemStack(Material.DIAMOND, 1);
		return It;
	}
	
}
