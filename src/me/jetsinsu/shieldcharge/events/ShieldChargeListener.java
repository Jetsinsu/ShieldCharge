package me.jetsinsu.shieldcharge.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.jetsinsu.shieldcharge.ShieldCharge;
import me.jetsinsu.shieldcharge.api.ShieldChargeEvent;

public class ShieldChargeListener implements Listener{
	
	private double radius, speed, delay, limit, damage;
	
	private static final ArrayList<String> limitTime = new ArrayList<String>(), delaytime = new ArrayList<String>();
	private static final String sc = (ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "]" + ChatColor.GREEN + " ");

	private ShieldCharge plugin;
	public ShieldChargeListener(ShieldCharge plugin){
		this.plugin = plugin;
		reloadValues();
	}
	
	public void reloadValues(){
		this.radius = plugin.getConfig().getDouble("shieldcharge.radius");
		this.speed = plugin.getConfig().getDouble("shieldcharge.speed");
		this.delay = plugin.getConfig().getDouble("shieldcharge.delay");
		this.limit = plugin.getConfig().getDouble("shieldcharge.timelimit");
		this.damage = plugin.getConfig().getDouble("shieldcharge.damage");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onUseShield(PlayerInteractEvent e){
		if (!plugin.getConfig().getBoolean("shieldcharge.enable")) return;
		
		Player p = e.getPlayer();
		if(!p.hasPermission("sc.usage")) return;

		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (e.getItem() == null) return;
			if (e.getItem().getType().equals(Material.SHIELD)){
				if(p.getNearbyEntities(radius, radius, radius).isEmpty()) return;
				if(delaytime.contains(p.getName())) return;
				if (!plugin.hasWorldGuardPermission(p)){
					p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "] "
							+ ChatColor.RED + "You cannot use ShieldCharge in a protected region");
					return;
				}
				
				shieldCharge(p, e.getItem());
				shieldTimeLimit(p);
			}
		}
	}
	
	private void shieldTimeLimit(final Player p){
		p.setWalkSpeed((float) speed/5);
		limitTime.add(p.getName());
		new BukkitRunnable(){
			@Override
			public void run(){
				limitTime.remove(p.getName());
				p.setWalkSpeed((float) 0.2);
				if (delay > 0) delayCoolDown(p);
				
				this.cancel();
			}
		}.runTaskLater(plugin, ((long) limit * 20));
	}
	
	private void shieldCharge(final Player p, final ItemStack shield){
		List<Entity> damaged = p.getNearbyEntities(radius, radius, radius);
		for(Entity e: damaged){
			if (delaytime.contains(p.getName())) return;
			if(e instanceof LivingEntity){
				if(e instanceof ArmorStand) continue;
				LivingEntity entity = (LivingEntity) e;
				
				ShieldChargeEvent event = new ShieldChargeEvent(p, shield, entity, damage);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled()) continue;
				
				double newDamage = event.getDamage();
				
				if ((entity.getHealth() - newDamage) <= 0) CustomDeathMessage.bashed = true;
				((Damageable) e).damage(newDamage, p);
				e.setVelocity(p.getLocation().getDirection().setY(p.getLocation().getDirection().multiply(0.5).getY() + 0.5));
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1, 1);
				
				if (plugin.getConfig().getBoolean("shieldcharge.shieldtakesdamage"))
					shield.setDurability(shield.getDurability() <= shield.getType().getMaxDurability() ? (short) (shield.getDurability() + 1) : shield.getDurability());
			}
		}
	}
	
	private void delayCoolDown(final Player p) {
		delaytime.add(p.getName());
		p.sendMessage(sc + "Cooldown for " + delay + " seconds");
		new BukkitRunnable(){
			@Override
			public void run() {
				delaytime.remove(p.getName());
				p.sendMessage(sc + "Cooldown ended");
				
				this.cancel();
			}
		}.runTaskLater(plugin, ((long) delay * 20));
	}
}