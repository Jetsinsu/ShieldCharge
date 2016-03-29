package me.jetsinsu.shieldcharge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ShieldCharge extends JavaPlugin{

	String sc = (ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "]" + ChatColor.GREEN + " ");
	String sc2 = (ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " ");
	
	public void onEnable(){
		Bukkit.getServer().getPluginManager().registerEvents(new ShieldChargeListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new CustomDeathMessage(), this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void onDisabled(){
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("shieldcharge")){
			if(args.length == 0){
				sender.sendMessage(sc + "/shieldcharge reload");
				sender.sendMessage(sc + "/shieldcharge save");
				return true;
			}
			
			else if (args.length >= 1){
				if(args[0].equalsIgnoreCase("reload")){
					if(!(sender.hasPermission("sc.reload"))){
						sender.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					reloadConfig();
					sender.sendMessage(sc + "ShieldCharge has been reloaded!");
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("save")){
					if(!(sender.hasPermission("sc.save"))){
						sender.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					saveConfig();
					sender.sendMessage(sc + "ShieldCharge has been saved!");
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("enable")){
					if(getConfig().getBoolean("shieldcharge.enable") == true){
						sender.sendMessage(sc2 + "The plugin is already enabled!");
						return;
					}
					else getConfig().setBoolean("shieldcharge.enable", true);
					sender.sendMessage(sc + "Plugin is now enabled!");
					return;
				}
				
				else if(args[0].equalsIgnoreCase("disable")){
					if(getConfig().getBoolean("shieldcharge.enable") == false){
						sender.sendMessage(sc2 + "The plugin is already disabled!");
						return;
					}
					
					else getConfig().setBoolean("shieldcharge", false);
					sender.sendMessage(sc + "Plugin is now disabled!");
					return;
				}
			}
		}
		return true;
	}
}
