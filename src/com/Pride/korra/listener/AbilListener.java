package com.Pride.korra.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.Pride.korra.waterabilities.ArcticSnow;
import com.Pride.korra.waterabilities.Scald;
import com.Pride.korra.waterabilities.WaterSprite;
import com.projectkorra.projectkorra.BendingPlayer;

public class AbilListener implements Listener {
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event) {

		Player player = event.getPlayer();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

		if (event.isCancelled() || !event.isSneaking()) {
			return;
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("WaterSprite")) {
			new WaterSprite(player);
			
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Scald")) {
			new Scald(player);
		
		} else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("ArcticSnow")) {
			new ArcticSnow(player);
			
		}
	}

}
