package islandwars.worldgeneration;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import islandwars.Island;
import islandwars.IslandWars;
import org.bukkit.*;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class EmptyChunkGenerator extends ChunkGenerator {

    protected final JavaPlugin javaPlugin;


    private static final HashMap<File, ClipboardFormat> clipboardCache = new HashMap<>();
    private Clipboard clipboard;

    private int spawnedIslands = 0;
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {

        if (random.nextInt(11) < 1) {
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            if(this.spawnedIslands > 20 || x > 10 || z > 10 || x < 0 || z < 0) return;
            Location location = new Location(Bukkit.getWorld(worldInfo.getName()), x*16, 1, z*16);
            IslandWars.getInstance().getIslandManager().AddIsland(location);
            Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () -> this.PasteSchematic(location.clone(), completableFuture));
            this.spawnedIslands +=1;
            return;
        }
        chunkData.setRegion(0, chunkData.getMinHeight(), 0, 16, chunkData.getMaxHeight(), 16, Material.VOID_AIR);

    }

    private void PasteSchematic(Location location, CompletableFuture completableFuture) {

        File f = new File(javaPlugin.getDataFolder(), "schematics\\test.schem");
        Bukkit.getLogger().info(f.getAbsolutePath());
        try {
            ClipboardFormat format = clipboardCache.getOrDefault(f, ClipboardFormats.findByFile(f));
            ClipboardReader reader = format.getReader(new FileInputStream(f));
            Clipboard clipboard = reader.read();
            Island island = IslandWars.getInstance().getIslandManager().getIslandByLocation(location);
            int width = clipboard.getDimensions().getBlockX();
            int height = clipboard.getDimensions().getBlockY();
            int length = clipboard.getDimensions().getBlockZ();
            location.subtract(width / 2.00, height / 2.00, length / 2.00);
            island.spawnLocation = location;
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
                clipboardCache.putIfAbsent(f, format);
                completableFuture.complete(null);
            }
        } catch (IOException | WorldEditException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "PASTING ERROR");
            e.printStackTrace();

        }
    }

    public EmptyChunkGenerator(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;


    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0d, 64d, 0d);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    protected void placeBedrock(ChunkData paramChunkData, int paramChunkX, int paramChunkZ) {
        // Bedrock block position
        int x = 0, y = 64, z = 0;

        if ((x >= paramChunkX * 16) && (x < (paramChunkX + 1) * 16)) {
            if ((z >= paramChunkZ * 16) && (z < (paramChunkZ + 1) * 16)) {
                paramChunkData.setBlock(x, y, z, Material.BEDROCK);
            }
        }
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock block position
        final int x = 0, y = 64, z = 0;

        if ((x >= chunkX * 16) && (x < (chunkX + 1) * 16)) {
            if ((z >= chunkZ * 16) && (z < (chunkZ + 1) * 16)) {
                chunkData.setBlock(x, y, z, Material.BEDROCK);
            }
        }
    }

}
