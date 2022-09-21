package fr.gameurduxvi.hubplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.meneliaapi.MeneliaAPI;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class NameTag extends BukkitRunnable {

	@Override
	public void run() {
		for(Player loopPlayer: Bukkit.getOnlinePlayers())
		{			
			Scoreboard sc = loopPlayer.getScoreboard();
			for(Team loopTeam: sc.getTeams()) {
				for(Player lp: Bukkit.getOnlinePlayers()) {
					String tab = PermissionsEx.getPermissionManager().getGroup(MeneliaAPI.getFunctions().getGroup(lp)).getOption("tabprefix");				
					int priority = Integer.parseInt(PermissionsEx.getPermissionManager().getGroup(MeneliaAPI.getFunctions().getGroup(lp)).getOption("priority"));
					if(loopTeam.getName().equals(priority + "_" + lp.getName())) {
						loopTeam.setPrefix(MeneliaAPI.getInstance().colorize("§" + priority + "" + tab + " "));
					}
				}
			}
			loopPlayer.setScoreboard(sc);
		}
	}

}
