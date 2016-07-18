package me.jetsinsu.shieldcharge;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import me.jetsinsu.shieldcharge.commands.Commands;
import me.jetsinsu.shieldcharge.events.CustomDeathMessage;
import me.jetsinsu.shieldcharge.events.ShieldChargeListener;

public class ShieldCharge extends JavaPlugin{
	
	private boolean worldGuardEnabled = false;
	private ShieldChargeListener chargeListener;
	
	public void onEnable(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		this.chargeListener = new ShieldChargeListener(this);
		Bukkit.getServer().getPluginManager().registerEvents(chargeListener, this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomDeathMessage(), this);
		
		getCommand("shieldcharge").setExecutor(new Commands(this));
		
		worldGuardEnabled = (Bukkit.getPluginManager().getPlugin("WorldGuard") != null);
	}
	
	public void onDisabled(){
		saveConfig();
	}
	
	public ShieldChargeListener getChargeListener() {
		return chargeListener;
	}
	
	public boolean isWorldGuardEnabled() {
		return worldGuardEnabled;
	}
	
	public boolean hasWorldGuardPermission(Player player){
		if (!worldGuardEnabled) return true;
		
		ApplicableRegionSet damagedRegions = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
		if (damagedRegions.queryState(null, DefaultFlag.PVP) == null
				|| damagedRegions.queryState(null, DefaultFlag.PVP).equals(State.ALLOW)){
			return true;
		}else{ return false; }
	}
}