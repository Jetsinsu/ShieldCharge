package me.jetsinsu.shieldcharge.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.jetsinsu.shieldcharge.ShieldCharge;

public class Commands implements CommandExecutor{

	private static final String sc = (ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "]" + ChatColor.GREEN + " ");
	private static final String sc2 = (ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "ShieldCharge" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + " ");
	
	private ShieldCharge plugin;
	public Commands(ShieldCharge plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("shieldcharge")){
			if(args.length == 0){
				sender.sendMessage(sc + "/shieldcharge enable");
				sender.sendMessage(sc + "/shieldcharge disable");
				sender.sendMessage(sc + "/shieldcharge reload");
				sender.sendMessage(sc + "/shieldcharge save");
				sender.sendMessage(sc + "/shieldcharge set <speed,timelimit,radius,delay,damage>");
				return true;
			}
			
			else if (args.length == 1){
				if(args[0].equalsIgnoreCase("reload")){
					if(!(sender.hasPermission("sc.reload"))){
						sender.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					plugin.reloadConfig();
					plugin.getChargeListener().reloadValues();
					sender.sendMessage(sc + "ShieldCharge has been reloaded!");
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("save")){
					if(!(sender.hasPermission("sc.save"))){
						sender.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					plugin.saveConfig();
					sender.sendMessage(sc + "ShieldCharge has been saved!");
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("enable")){
					if(plugin.getConfig().getBoolean("shieldcharge.enable") == true){
						sender.sendMessage(sc2 + "The plugin is already enabled!");
						return true;
					}
					
					plugin.getConfig().set("shieldcharge.enable", true);
					sender.sendMessage(sc + "Plugin is now enabled!");
					plugin.saveConfig();
					plugin.reloadConfig();
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("disable")){
					if(plugin.getConfig().getBoolean("shieldcharge.enable") == false){
						sender.sendMessage(sc2 + "The plugin is already disabled!");
						return true;
					}
					
					plugin.getConfig().set("shieldcharge.enable", false);
					sender.sendMessage(sc + "Plugin is now disabled!");
					plugin.saveConfig();
					plugin.reloadConfig();
					return true;
				}
			}
		}
		return true;
	}
}
