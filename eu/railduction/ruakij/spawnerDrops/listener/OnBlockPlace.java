package eu.railduction.ruakij.spawnerDrops.listener;

import eu.railduction.ruakij.spawnerDrops.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.IOException;

public class OnBlockPlace implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){

        Player p = e.getPlayer();

        if(e.getBlock().getType() == Material.MOB_SPAWNER){
            Location loc = e.getBlock().getLocation();

            // Can break?
            boolean isAdmin = p.hasPermission("spawnerDrops.admin");
            if(!isAdmin && !Main.config.getBoolean("place.PLAYER.canPlace")){

                // Cancel place-event
                e.setCancelled(true);

                // Send msg
                String msg = Main.config.getString("place.PLAYER.msg.fail");
                if(msg != null && !msg.equals("")) p.sendMessage(msg);

                return;
            }

            // Write place-data
            Main.data.set(Main.serialiseLocation(loc), isAdmin);

            try {
                Main.data.save("data.yml");
            } catch (IOException ex) {
                Main.plugin.getLogger().severe("Cannot save data to data.yml! Data will lost after restart!");
                ex.printStackTrace();
            }

            // Send msg
            String msg = Main.config.getString("place."+ (isAdmin?"ADMIN":"PLAYER") +".msg.success");
            if(msg != null && !msg.equals("")) p.sendMessage(msg);
        }
    }
}
