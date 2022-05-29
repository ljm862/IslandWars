package islandwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Island {
    public Location location;
    public Player owner;
    public String name;
    public Location spawnLocation;
    public Location beaconLocation;
    public Block beacon;
    public boolean isBeaconActive;
    public Island(Location location){
        this.location = location;
        name = location.getX() + " " + location.getY() + " " + location.getZ();
    }

    public void setOwner(Player p){
        owner = p;
    }

    public void setSpawnLocation(Location l){
        spawnLocation = l;
        beaconLocation = l.clone().add(2,0, 1);
    }

    public void giveBeacon(){
        beaconLocation.getBlock().setType(Material.JACK_O_LANTERN);
        beacon = beaconLocation.getBlock();
        this.isBeaconActive = true;
        IslandWars.getInstance().getGameManager().addBeacon(beacon, this);
    }
}
