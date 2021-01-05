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

public class OnBlockBreak implements Listener {

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){

        Player p = e.getPlayer();

        if(e.getBlock().getType() == Material.MOB_SPAWNER){
            // Allow creative
            if(p.getGameMode() == GameMode.CREATIVE)
                return;

            // Get spawner-source (unknown, player, admin)
            Location loc = e.getBlock().getLocation();
            BlockSource bSource;
            Boolean placedByAdmin = Main.data.getBoolean(Main.serialiseLocation(loc));
            if(placedByAdmin == null)
                bSource = BlockSource.UNKNOWN;
            else
                if(!placedByAdmin)
                    bSource = BlockSource.PLAYER;
                else
                    bSource = BlockSource.ADMIN;

            // # Break-Prerequisite
            ConfigurationSection breakPreConfig = Main.config.getConfigurationSection("break.break-prerequisite."+ bSource);
            // Silktouch
            if(breakPreConfig.getBoolean("silktouch")){
                if(!p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
                    e.setCancelled(true);
            }
            // Send msg
            String msg = breakPreConfig.getString("msg."+ (e.isCancelled()?"fail":"success"));
            if(msg != null && !msg.equals("")) p.sendMessage(msg);


            if(e.isCancelled()) return;

            // # Drop-Prerequisite
            ConfigurationSection dropPreConfig = Main.config.getConfigurationSection("break.drop-prerequisite."+ bSource);
            // Silktouch
            if(breakPreConfig.getBoolean("silktouch")){
                if(!p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH))
                    e.setDropItems(false);
            }
            // Send msg
            msg = dropPreConfig.getString("msg."+ (e.isDropItems()?"success":"fail"));
            if(msg != null && !msg.equals("")) p.sendMessage(msg);
        }
    }
}
