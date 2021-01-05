package eu.railduction.ruakij.spawnerDrops.listener;

import eu.railduction.ruakij.spawnerDrops.BlockSource;
import eu.railduction.ruakij.spawnerDrops.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class OnBlockBreak implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){

        Player p = e.getPlayer();

        if(e.getBlock().getType() == Material.MOB_SPAWNER){
            Location loc = e.getBlock().getLocation();
            String locSer = Main.serialiseLocation(loc);

            // Get spawner-source (unknown, player, admin)
            BlockSource bSource;
            Object placedByAdmin = Main.data.get(locSer);
            if(placedByAdmin == null)
                bSource = BlockSource.UNKNOWN;
            else
            if(!(boolean)placedByAdmin)
                bSource = BlockSource.PLAYER;
            else
                bSource = BlockSource.ADMIN;

            // Ignore creative
            if(p.getGameMode() != GameMode.CREATIVE){

                // # Break-Prerequisite
                ConfigurationSection breakPreConfig = Main.config.getConfigurationSection("break.break-prerequisite."+ bSource);
                boolean success = false;
                // Silktouch
                if(breakPreConfig.getBoolean("silktouch")){
                    if(p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
                        success = true;
                    else
                        e.setCancelled(true);
                }
                // Send msg
                String msg = breakPreConfig.getString("msg."+ (success?"success":"fail"));
                if(msg != null && !msg.equals("")) p.sendMessage(msg);

                // Stop if breaking wasnt successful
                if(!success) return;
            }


            // Remove block from data
            if(bSource != BlockSource.UNKNOWN){
                Main.data.set(locSer, null);
                try {
                    Main.data.save("data.yml");
                } catch (IOException ex) {
                    Main.plugin.getLogger().severe("Cannot save data to data.yml! Data will lost after restart!");
                    ex.printStackTrace();
                }
            }

            // Ignore creative
            if(p.getGameMode() != GameMode.CREATIVE){

                // # Drop-Prerequisite
                ConfigurationSection dropPreConfig = Main.config.getConfigurationSection("break.drop-prerequisite."+ bSource);
                boolean success = false;
                // Silktouch
                if(dropPreConfig.getBoolean("silktouch")){
                    if(p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
                        success = true;
                    else
                        e.setCancelled(true);
                }

                // Set drop
                if(success){
                    ItemStack item = new ItemStack(Material.MOB_SPAWNER);
                    loc.getWorld().dropItem(loc, item);
                }

                // Send msg
                String msg = dropPreConfig.getString("msg."+ (success?"success":"fail"));
                if(msg != null && !msg.equals("")) p.sendMessage(msg);
            }
        }
    }
}
