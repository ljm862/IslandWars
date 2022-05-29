package islandwars.worldgeneration;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldedit.WorldEdit;
import islandwars.IslandWars;
import islandwars.tasks.NewGameTask;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldGenCommand implements CommandExecutor {

    private final WorldEdit worldEditInstance;
    private final MultiverseCore multiverseInstance;

    public WorldGenCommand(WorldEdit we, MultiverseCore mvc){
        this.worldEditInstance = we;
        this.multiverseInstance = mvc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(IslandWars.getInstance().getGameManager().isGameInProgress){
            sender.sendMessage(ChatColor.RED + "Game in progress already.");
            return true;
        }
        IslandWars.getInstance().newGameManager();
        IslandWars.getInstance().getGameManager().isGameInProgress = true;
        multiverseInstance.getMVWorldManager().addWorld("voidworld", World.Environment.NORMAL, "", WorldType.FLAT, false, "IslandWars");
        Bukkit.broadcastMessage(ChatColor.BLUE + "A new game has started. Type '/joinwar' to join in!");
        new NewGameTask().runTaskLater(IslandWars.getInstance(), 300L);
        return true;
    }
}
