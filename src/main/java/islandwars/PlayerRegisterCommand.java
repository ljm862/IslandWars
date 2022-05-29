package islandwars;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRegisterCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player p){
            IslandWars.getInstance().getGameManager().RegisterPlayer(p);
            p.sendMessage(ChatColor.BLUE + "You have registered for the upcoming match!");
        }
        return false;
    }
}
