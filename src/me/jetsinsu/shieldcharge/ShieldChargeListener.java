package me.jetsinsu.shieldcharge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldChargeListener implements Listener{
	
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> delaytime = new ArrayList<String>();

	public ShieldCharge plugin;
	
	public ShieldChargeListener(ShieldCharge plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onUseShield(PlayerInteractEvent e){
		if (!plugin.getConfig().getBoolean("shieldcharge.enable")) return;
		
		Player p = e.getPlayer();
		if(!p.hasPermission("sc.usage")) return;

		int speed = plugin.getConfig().getInt("shieldcharge.speed");
		int delay = plugin.getConfig().getInt("shieldcharge.delay");
		
		if(plugin.getConfig().getBoolean("shieldcharge.enable")){
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (e.getItem() == null) return;
				if (e.getItem().getType().equals(Material.SHIELD)){
					if(delaytime.contains(p.getName())) return;
					
					list.add(p.getName());
					p.setWalkSpeed((float) speed/5);
					shieldTimeLimit(p);
					shieldCharge(p);
					p.sendMessage(plugin.sc + "Cooldown for " + delay + " seconds");
				}
			}
		}
	}
	
	public void shieldTimeLimit(final Player p){
		int timelimit = plugin.getConfig().getInt("shieldcharge.timelimit");
		new BukkitRunnable(){
			@Override
			public void run(){
				list.remove(p.getName());
				p.setWalkSpeed((float) 0.2);
				delaytime.add(p.getName());
				delayCoolDown(p);
			}
		}.runTaskLater(plugin, timelimit * 20);
	}
	
	public void shieldCharge(final Player p){
		final int radius = plugin.getConfig().getInt("shieldcharge.radius");
		new BukkitRunnable(){
			@Override
			public void run() {
				if(!list.contains(p.getName())) return;
				
				List<Entity> damaged = p.getNearbyEntities(radius, radius, radius);
				for(Entity e: damaged){
					if(e instanceof LivingEntity){
						e.setVelocity(p.getLocation().getDirection().setY(p.getLocation().getDirection().multiply(0.5).getY() + 0.5));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1, 1);
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
	
	private void delayCoolDown(final Player p) {
		int delay = plugin.getConfig().getInt("shieldcharge.delay");
		new BukkitRunnable(){
			@Override
			public void run() {
				delaytime.remove(p.getName());
				p.sendMessage(plugin.sc + "Cooldown ended");
			}
		}.runTaskLater(plugin, delay * 20);
	}
}
