package islandwars.managers;

import com.onarandombox.MultiverseCore.MultiverseCore;
import islandwars.Island;
import islandwars.IslandWars;
import islandwars.tasks.GameEndTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager implements IManager{
    public final List<Player> playerList;
    private final MultiverseCore multiverseInstance;
    public final HashMap<Block, Island> beaconMap;
    public final HashMap<Player, Location> originalLocationMap;

    public boolean isGameInProgress;

    public GameManager(MultiverseCore multiverseCore){
        playerList = new ArrayList<>();
        multiverseInstance = multiverseCore;
        beaconMap = new HashMap<>();
        originalLocationMap = new HashMap<>();
    }

    public void InitialiseGame(){

        IslandWars.getInstance().getIslandManager().AssignPlayersToIslands(playerList);
        for (Player p: playerList) {
            originalLocationMap.put(p, p.getLocation());
        }
        TeleportPlayers();
    }

    public void RegisterPlayer(Player p){
        playerList.add(p);
    }

    private void TeleportPlayers(){
        HashMap<Player, Island> cachedMap = IslandWars.getInstance().getIslandManager().playerIslandHashMap;

        for (Player p: playerList){
            multiverseInstance.teleportPlayer(p, p, cachedMap.get(p).spawnLocation);
            p.sendMessage("Teleporting to: " + cachedMap.get(p).spawnLocation);
        }
    }

    public void removePlayer(Player p){
        this.playerList.remove(p);
        if(this.playerList.size() < 2){
            EndGame();
        }
    }

    public void addBeacon(Block b, Island i){
        beaconMap.put(b, i);
    }

    private void EndGame(){
        new GameEndTask(playerList.get(0), multiverseInstance).runTaskLater(IslandWars.getInstance(), 300L);

    }
}
