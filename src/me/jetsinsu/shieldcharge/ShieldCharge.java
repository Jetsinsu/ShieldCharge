package me.jetsinsu.shieldcharge;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
		if(!(sender instanceof Player)){
			sender.sendMessage(sc + "Command only for players!");
			return true;
		}
		
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("shieldcharge")){
			if(args.length == 0){
				p.sendMessage(sc + "/shieldcharge reload");
				p.sendMessage(sc + "/shieldcharge save");
				return true;
			}
			
			else if (args.length >= 1){
				if(args[0].equalsIgnoreCase("reload")){
					if(!(p.hasPermission("sc.reload"))){
						p.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					reloadConfig();
					p.sendMessage(sc + "ShieldCharge has been reloaded!");
					return true;
				}
				
				else if(args[0].equalsIgnoreCase("save")){
					if(!(p.hasPermission("sc.save"))){
						p.sendMessage(sc2 + "You do not have permissions to use this command!");
						return true;
					}
					
					saveConfig();
					p.sendMessage(sc + "ShieldCharge has been saved!");
					return true;
				}
			}
		}
		return true;
	}
}
