package fr.gameurduxvi.hubplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.gameurduxvi.meneliaapi.MeneliaAPI;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    switch(subchannel) {
	    	case "GetServer":
	    		String data1 = in.readUTF();
	    		Bukkit.getConsoleSender().sendMessage(MeneliaAPI.getInstance().colorize(Main.getInstance().pluginPrefix + " The current server name is &e" + data1));
	    		//Main.currentBungeeServer = data;
	    		break;
	    	default:
	    		player.sendMessage("Channel n'existe pas" + subchannel);
	    		break;
	    }
	  }

}
