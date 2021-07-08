package me.tomerdad.MyFirstPlugin;

//imports
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	//GUI variable (method Inventory)
	public Inventory gui;
	
	//when plugin turn on or server startup
	@Override
	public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
		
		createGUI();
	}
	
	//When plugin turn off or server shutdown
	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
    public void onClick(InventoryClickEvent e){
         
        //Check to see if its the GUI menu
        if (gui.equals(e.getClickedInventory())) {
            //check if not air or null. (not must here)
        	if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) return;
            //disables changes in the gui
            e.setCancelled(true);
            //gets the player object
            Player player = (Player) e.getWhoClicked();
            
            //switch between the blocks functions, also can be with that 
            //if (e.getCurrentItem().getType() == Material.NETHER_BRICK) { }
            // and includes in the if the name if there are 2 of same item.
            	
            switch(e.getCurrentItem().getType()){
                case NETHER_BRICKS:
                    player.closeInventory();
                    // get the nether world by the nether folder's name
                    World nether = Bukkit.getWorld("world_nether");
                    player.teleport(nether.getSpawnLocation());
                    break;
                case DIRT:
                    player.closeInventory();
                    TeleportTo(player, 0, player.getWorld());
                    break;
                case GRASS_BLOCK:
                    player.closeInventory();
                    TeleportTo(player, 1000, player.getWorld());
                    break;
                case COBBLESTONE:
                    player.closeInventory();
                    TeleportTo(player, 2000, player.getWorld());
                    break;
                case STONE:
                    player.closeInventory();
                    TeleportTo(player, 5000, player.getWorld());
                    break;
                case BARRIER:
                    player.closeInventory();
                    break;
            }
 
        }
 
    }
	
	public void TeleportTo(Player player, Integer radius, World world) {
        //get random
        Random random = new Random();
		
        Integer xLoc = random.nextInt(radius*2+1)-radius;
        Integer yLoc = random.nextInt(radius*2+1)-radius;
        Location tpLocation = new Location(world, xLoc, 100, yLoc);
        player.teleport(tpLocation);
        player.sendMessage(ChatColor.YELLOW + "You have teleported to " + xLoc +"," + yLoc + " on world '" + world.getName() + "'");
	}
	
	//onCommand, when player send a command changelocation
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("changelocation")) {
			//check if a player send the command
			if (!(sender instanceof Player)) {
				sender.sendMessage("You cannot do that");
				return true;
			}
			Player player = (Player) sender;
			// open the GUI
			player.openInventory(gui);
			return true;
		}
		return false;
	}
	
	//Creates the GUI function
	public void createGUI() {
		//creates GUI that have 3 rows of 9 with the name `Teleport GUI` and have color red in bold
		gui = Bukkit.createInventory(null, 9*3, ChatColor.BOLD + "" + ChatColor.RED +"Teleport GUI");
		
		//create lore (Lines under the item name)
		//List because can to be more then 1 line
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "Click Me To Teleport!");
		
		//create Items
		addItemToGui(gui, Material.NETHER_BRICKS, "NETHER", ChatColor.YELLOW, lore, 11);
		addItemToGui(gui, Material.DIRT, "0,0", ChatColor.RED, lore, 12);
		addItemToGui(gui, Material.GRASS_BLOCK, "1000X1000", ChatColor.DARK_GREEN, lore, 13);
		addItemToGui(gui, Material.COBBLESTONE, "2000X2000", ChatColor.GRAY, lore, 14);
		addItemToGui(gui, Material.STONE, "5000X5000", ChatColor.DARK_GRAY, lore, 15);
		addItemToGui(gui, Material.BARRIER, "Exit", ChatColor.DARK_RED, null, 26);
	}
	
	//gui is public.
	public void addItemToGui(Inventory gui, Material itemtype, String Name, ChatColor color, List<String> lore ,Integer place) {
		//create Items
		ItemStack item = new ItemStack(itemtype);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(color + Name);
		
		//sets the lore
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		gui.setItem(place, item);
	}
	
}
