package me.jetsinsu.shieldcharge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import CommandManager.Commands;

public class ShieldCharge extends JavaPlugin{
	
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(new ShieldChargeListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomDeathMessage(), this);
		getCommand("shieldcharge").setExecutor(new Commands(this));
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void onDisabled(){
		saveConfig();
	}
}
