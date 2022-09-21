package fr.gameurduxvi.hubplugin;

import java.util.UUID;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;

public class AdvancementsMenelia {
	private UUID uuid;
	private AdvancementManager manager;
	
	public AdvancementsMenelia(UUID uuid, AdvancementManager manager){
		this.uuid = uuid;
		this.manager = manager;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public AdvancementManager getManager() {
		return manager;
	}
}
