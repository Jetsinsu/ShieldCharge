package me.jetsinsu.shieldcharge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CustomDeathMessage implements Listener{
	
	String deathMessage = "%killed% was bashed to death by %killer%";
	public static boolean bashed = false;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event){
		if (!bashed) return;
		if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			event.setDeathMessage(deathMessage.replace("%killed%", e.getEntity().getName()).replace("%killer%", e.getDamager().getName()));
			bashed = false;
		}
	}
}