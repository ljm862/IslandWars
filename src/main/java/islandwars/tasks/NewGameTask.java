package islandwars.tasks;

import islandwars.IslandWars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class NewGameTask extends BukkitRunnable {

    @Override
    public void run(){
        Bukkit.broadcastMessage(ChatColor.BLUE + "Registration for upcoming war game has ended.");
        IslandWars.getInstance().getGameManager().InitialiseGame();
    }
}
