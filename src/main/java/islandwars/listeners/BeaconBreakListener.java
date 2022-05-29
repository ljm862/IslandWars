package islandwars.listeners;

import com.onarandombox.MultiverseCore.MultiverseCore;
import islandwars.Island;
import islandwars.IslandWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BeaconBreakListener implements Listener {
    private final MultiverseCore multiverseCore;

    public BeaconBreakListener(MultiverseCore multiverseCore){
        this.multiverseCore = multiverseCore;
    }

    @EventHandler
    public void onBeaconBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        if(IslandWars.getInstance().getGameManager().beaconMap.containsKey(block)){
            e.setDropItems(false);
            Island i = IslandWars.getInstance().getGameManager().beaconMap.get(block);
            Player p = i.owner;
            i.isBeaconActive = false;

            IslandWars.getInstance().getGameManager().removePlayer(p);
            Bukkit.broadcastMessage(ChatColor.RED + e.getPlayer().getName() + " has destroyed " + p.getName() + "'s beacon!");
            p.setHealth(0);
          //  multiverseCore.teleportPlayer(p, p, IslandWars.getInstance().getGameManager().originalLocationMap.get(p));
        }
    }

}
