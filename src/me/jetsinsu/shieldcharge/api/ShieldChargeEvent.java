package me.jetsinsu.shieldcharge.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShieldChargeEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private Player player;
	private ItemStack shield;
	private LivingEntity damaged;
	public ShieldChargeEvent(Player player, ItemStack shield, LivingEntity damaged){
		this.player = player;
		this.shield = shield;
		this.damaged = damaged;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ItemStack getShield(){
		return shield;
	}
	
	public LivingEntity getDamaged(){
		return damaged;
	}
	
	@Override
	public HandlerList getHandlers(){
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