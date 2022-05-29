package islandwars.tasks;

import com.onarandombox.MultiverseCore.MultiverseCore;
import islandwars.IslandWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndTask extends BukkitRunnable {
    private final Player player;
    private final MultiverseCore multiverseInstance;

    public GameEndTask(Player p, MultiverseCore mvc) {
        this.player = p;
        this.multiverseInstance = mvc;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatColor.BLUE + player.getName() + " has won the IslandWar!");

        multiverseInstance.teleportPlayer(player, player, IslandWars.getInstance().getGameManager().originalLocationMap.get(player));
        multiverseInstance.getMVWorldManager().unloadWorld("voidworld");
        multiverseInstance.getMVWorldManager().deleteWorld("voidworld");
        IslandWars.getInstance().getGameManager().isGameInProgress = false;
    }
}
