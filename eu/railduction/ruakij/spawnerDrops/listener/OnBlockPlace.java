package eu.railduction.ruakij.spawnerDrops.listener;

import eu.railduction.ruakij.spawnerDrops.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {

    public void onBlockPlaceEvent(BlockPlaceEvent e){

        Player p = e.getPlayer();

        if(e.getBlock().getType() == Material.MOB_SPAWNER){
            Location loc = e.getBlock().getLocation();

            // Can break?
            boolean isAdmin = p.hasPermission("spawnerDrops.admin");
            if(!isAdmin && !Main.config.getBoolean("place.player.canPlace")){

                // Cancel place-event
                e.setCancelled(true);

                // Send msg
                String msg = Main.config.getString("place.player.msg.fail");
                if(msg != null && !msg.equals("")) p.sendMessage(msg);
            }
            else{
                // Write place-data
                Main.data.set(Main.serialiseLocation(loc), isAdmin);

                // Send msg
                String msg = Main.config.getString("place.player.msg.success");
                if(msg != null && !msg.equals("")) p.sendMessage(msg);
            }
        }
    }
}
