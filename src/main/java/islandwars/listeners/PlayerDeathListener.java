package islandwars.listeners;

import com.onarandombox.MultiverseCore.MultiverseCore;
import islandwars.Island;
import islandwars.IslandWars;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final MultiverseCore multiverseCore;

    public PlayerDeathListener(MultiverseCore multiverseCore) {
        this.multiverseCore = multiverseCore;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        if (!p.getLocation().getWorld().getName().equals(multiverseCore.getMVWorldManager().getMVWorld("voidworld").getName())) {
            return;
        }

        //Check their beacon
        Island i = IslandWars.getInstance().getIslandManager().playerIslandHashMap.get(p);
        if (i.isBeaconActive) {
            e.setKeepInventory(true);
            Location l = IslandWars.getInstance().getIslandManager().playerIslandHashMap.get(p).spawnLocation;
            p.setBedSpawnLocation(IslandWars.getInstance().getIslandManager().getSafeLocation(l, i), true);
            return;
        }

        e.setKeepInventory(false);
        p.setBedSpawnLocation(IslandWars.getInstance().getGameManager().originalLocationMap.get(p), true);
    }
}
