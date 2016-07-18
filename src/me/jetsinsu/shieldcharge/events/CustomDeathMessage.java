package me.jetsinsu.shieldcharge.events;

import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CustomDeathMessage implements Listener{
	
	private static final Random random = new Random();
	private static final String[] deathMessages = new String[]{
		"%killed% was bashed to death by %killer%",
		"%killed% was killed by %killer% using the force of his shield",
		"%killer% bashed %killed% to the next dimension",
		"%killed%'s skull was crushed by %killer%'s shield",
		"A lethal dose of %killer%'s shield was applied to %killed%"
	};
	
	public static boolean bashed = false;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event){
		if (!bashed) return;
		if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			
			String deathMsg = deathMessages[random.nextInt(deathMessages.length)];
			event.setDeathMessage(deathMsg.replace("%killed%", e.getEntity().getName()).replace("%killer%", e.getDamager().getName()));
			bashed = false;
		}
	}
}