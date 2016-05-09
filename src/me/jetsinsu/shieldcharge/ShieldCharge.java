package me.jetsinsu.shieldcharge;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import me.jetsinsu.shieldcharge.commands.Commands;

public class ShieldCharge extends JavaPlugin{
	
	private boolean worldGuardEnabled = false;
	
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(new ShieldChargeListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomDeathMessage(), this);
		getCommand("shieldcharge").setExecutor(new Commands(this));
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		worldGuardEnabled = (Bukkit.getPluginManager().getPlugin("WorldGuard") != null);
	}
	
	public void onDisabled(){
		saveConfig();
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