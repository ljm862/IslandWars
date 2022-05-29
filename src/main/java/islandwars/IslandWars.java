package islandwars;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import islandwars.listeners.BeaconBreakListener;
import islandwars.listeners.PlayerDeathListener;
import islandwars.managers.GameManager;
import islandwars.managers.IslandManager;
import islandwars.worldgeneration.EmptyChunkGenerator;
import islandwars.worldgeneration.IslandPopulator;
import islandwars.worldgeneration.WorldGenCommand;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class IslandWars extends JavaPlugin {

    private WorldEditPlugin worldEditPlugin;
    private MultiverseCore multiversePlugin;

    private IslandManager islandManager;
    private GameManager gameManager;
    File schemsDir;

    private static IslandWars instance;
    public static IslandWars getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Starting IslandWars");
        instance = this;

        /*for (Plugin plugin:Bukkit.getPluginManager().getPlugins()
             ) {
            Bukkit.getLogger().info(plugin.getName());
        }*/

        this.islandManager = new IslandManager();
        this.gameManager = new GameManager(this.multiversePlugin);

        worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        multiversePlugin = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");

        this.copySchematics();

        getCommand("newworld").setExecutor(new WorldGenCommand(
                worldEditPlugin.getWorldEdit().getInstance(),
                multiversePlugin
        ));


        getCommand("joinwar").setExecutor(new PlayerRegisterCommand());

        getServer().getPluginManager().registerEvents(new BeaconBreakListener(multiversePlugin), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(multiversePlugin), this);

    }

    public IslandManager getIslandManager(){
        return islandManager;
    }

    public GameManager getGameManager(){
        return gameManager;
    }


    public GameManager newGameManager(){
        gameManager = new GameManager(this.multiversePlugin);
        return gameManager;
    }

    private void copySchematics() {
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdirs();

        schemsDir = new File(this.getDataFolder(), "schematics");
        if (!this.schemsDir.exists()) {
            schemsDir.mkdir();
        }

        this.copySchematic("test.schem");
    }

    private void copySchematic(String name) {
        File file = new File(this.getDataFolder(), "schematics\\" + name);
        if (!file.exists()) {
            try {
                InputStream i = getResource(name);
                Path target = file.toPath();

                Files.copy(i, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new EmptyChunkGenerator(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Ending IslandWars");
        multiversePlugin.getMVWorldManager().deleteWorld("voidworld");
    }

}
