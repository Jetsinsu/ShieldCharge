package me.jetsinsu.shieldcharge.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/** Called when an entity is damaged by a shield charge
 * This event may be called multiple times for one action, depending on how many entities are to be attacked
 */
public class ShieldChargeEvent extends Event implements Cancellable{
	public static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private final Player player;
	private final ItemStack shield;
	private final LivingEntity damaged;
	private double damage;
	public ShieldChargeEvent(final Player player, final ItemStack shield, final LivingEntity damaged, double damage){
		this.player = player;
		this.shield = shield;
		this.damaged = damaged;
		this.damage = damage;
	}
	
	/** Get the player that used the shield charge
	 * @return the player that used it
	 */
	public Player getPlayer(){
		return player;
	}
	
	/** Get the shield used to complete the shield charge
	 * @return the shield
	 */
	public ItemStack getShield(){
		return shield;
	}
	
	/** Get the living entity that was damaged in this event
	 * @return the entity damaged. Null if no entity was affected
	 */
	public LivingEntity getDamaged(){
		return damaged;
	}
	
	/** Set the damage to be inflicted for this event
	 * @param damage - The damage to inflict
	 */
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	/** Get the damage to be inflicted for this event
	 * @return - The damage to inflict
	 */
	public double getDamage() {
		return damage;
	}
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}