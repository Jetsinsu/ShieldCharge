package me.jetsinsu.shieldcharge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
		Player p = e.getPlayer();
		int speed = plugin.getConfig().getInt("shieldcharge.speed");
		int delay = plugin.getConfig().getInt("shieldcharge.delay");
		if(plugin.getConfig().getBoolean("shieldcharge.enable") == false){
			return;
		}
		
		if(!(p.hasPermission("sc.usage"))){
			return ;
		}
		
		if(plugin.getConfig().getBoolean("shieldcharge.enable") == true){
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.SHIELD){
					if(delaytime.contains(p.getName())){
						return;
					}
					
					list.add(p.getName());
					p.setWalkSpeed((float) speed/5);
					shieldTimeLimit(p);
					shieldCharge(p);
					p.sendMessage(plugin.sc + "Cooldown for " + delay + " seconds");
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void shieldTimeLimit(Player p){
		int timelimit = plugin.getConfig().getInt("shieldcharge.timelimit");
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new BukkitRunnable(){
			
			@Override
			public void run() {
				list.remove(p.getName());
				p.setWalkSpeed((float) 0.2);
				delaytime.add(p.getName());
				delayCoolDown(p);
			}
		}, timelimit * 20);
	}
	
	@SuppressWarnings("deprecation")
	public void shieldCharge(Player p){
		int radius = plugin.getConfig().getInt("shieldcharge.radius");
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new BukkitRunnable(){

			@Override
			public void run() {
				if(!(list.contains(p.getName()))){
					return;
				}
				
				List<Entity> damaged = p.getNearbyEntities(radius, radius, radius);
				for(Entity e: damaged){
					if(e instanceof LivingEntity){
						e.setVelocity(p.getLocation().getDirection().setY(p.getLocation().getDirection().multiply(0.5).getY() + 0.5));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1, 1);
					}
				}
			}
		}, 0L, 20L);
	}
	
	@SuppressWarnings("deprecation")
	private void delayCoolDown(Player p) {
		int delay = plugin.getConfig().getInt("shieldcharge.delay");
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new BukkitRunnable(){

			@Override
			public void run() {
				delaytime.remove(p.getName());
				p.sendMessage(plugin.sc + "Cooldown ended");
			}
		}, delay * 20);
	}
}
