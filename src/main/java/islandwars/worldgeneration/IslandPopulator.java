package islandwars.worldgeneration;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class IslandPopulator implements CommandExecutor {

    private final JavaPlugin javaPlugin;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {

            this.PasteSchematic(p.getLocation());
        }
        return true;
    }

    public IslandPopulator(JavaPlugin javaPlugin){
        this.javaPlugin = javaPlugin;
    }


    private void PasteSchematic(Location location) {

        File f = new File(javaPlugin.getDataFolder(), "schematics\\test.schem");
        Bukkit.getLogger().info(f.getAbsolutePath());
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(f);
            ClipboardReader reader = format.getReader(new FileInputStream(f));
            Clipboard clipboard = reader.read();
            int width = clipboard.getDimensions().getBlockX();
            int height = clipboard.getDimensions().getBlockY();
            int length = clipboard.getDimensions().getBlockZ();
            location.subtract(width / 2.00, height / 2.00, length / 2.00);
            clipboard.setOrigin(clipboard.getRegion().getMinimumPoint());
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(location.getWorld()))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                        .copyEntities(true)
                        .ignoreAirBlocks(true)
                        .build();
                Operations.complete(operation);
                Operations.complete(editSession.commit());
            }
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();

        }
    }
}
