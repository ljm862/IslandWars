package islandwars.managers;

import islandwars.Island;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class IslandManager implements IManager {
    public final List<Island> islandList;
    private final Random random;
    public HashMap<Player, Island> playerIslandHashMap;

    public IslandManager() {
        islandList = new ArrayList<>();
        random = new Random();
        playerIslandHashMap = new HashMap<>();
    }

    public void AddIsland(Location location) {
        Island island = new Island(location);
        islandList.add(island);
    }

    public void AssignPlayersToIslands(List<Player> players) {
        for (Player p : players) {
            boolean isSet = true;
            Island island = null;
            while (isSet) {
                island = islandList.get(random.nextInt(islandList.size()));
                isSet = island.owner != null;
            }
            island.setOwner(p);
            island.setSpawnLocation(getSafeLocation(island.spawnLocation, island));
            Bukkit.getLogger().info("Giving beacon");
            island.giveBeacon();
            Bukkit.getLogger().info("Beacon given");
            playerIslandHashMap.put(p, island);
            p.sendMessage(ChatColor.BLUE + "Your island is: " + island.name);
        }
    }

    public Island getIslandByLocation(Location l) {
        for (Island i : islandList) {
            if (i.location.equals(l)) {
                return i;
            }
        }
        return null;
    }

    public Location getSafeLocation(Location l, Island i) {
        if (isSafe(l)) return l;

        Location highest = getHighestLocation(l);
        if (isSafe(highest)) return highest;

        // Need to fix this to get the whole island's blocks.
        Location corner1 = i.location;
        Location corner2 = l.clone();

        int x0 = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int z0 = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int xn = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int zn = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        for (int x = x0; x <= xn; x++) {
            for (int z = z0; z <= zn; z++) {
                Location newLoc = getHighestLocation(x, z, l.getWorld());
                if (isSafe(newLoc)) return newLoc;
            }
        }
        Bukkit.getLogger().info("Couldn't find a safe place to teleport.");
        return i.beaconLocation.add(0, 1, 0);
    }

    public boolean isSafe(Location l) {
        Block block = l.getBlock();
        Block above = l.clone().add(0, 1, 0).getBlock();
        Block below = l.clone().subtract(0, 1, 0).getBlock();
        return block.isPassable() && above.isPassable() && !below.isPassable();
    }

    private Location getHighestLocation(Location l) {
        return this.getHighestLocation(l.getBlockX(), l.getBlockZ(), l.getWorld());

    }


    private Location getHighestLocation(int x, int z, World world) {
        Block block = world.getHighestBlockAt(x, z);
        while (!block.isPassable()) {
            block = block.getLocation().add(0, 1, 0).getBlock();
        }
        return block.getLocation().add(0.5, 0, 0.5);
    }


}
