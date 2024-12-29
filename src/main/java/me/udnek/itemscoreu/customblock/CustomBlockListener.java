package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CustomBlockListener extends SelfRegisteringListener {
    public CustomBlockListener(JavaPlugin plugin) {super(plugin);}

    // TODO: 6/13/2024 CATCH MORE EVENTS??
    @EventHandler
    public void onDestroy(BlockDestroyEvent event){
        CustomBlock customBlock = CustomBlock.get(event.getBlock());
        if (customBlock != null) customBlock.onDestroy(event);
    }
    @EventHandler
    public void onPlayerBreak(BlockBreakEvent event){
        CustomBlock customBlock = CustomBlock.get(event.getBlock());
        if (customBlock != null) customBlock.onDestroy(event);
    }

    public void loadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlock customBlock = CustomBlock.get(blockState);
            if (customBlock != null) customBlock.onLoad(blockState);
        }
    }
    public void unloadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlock customBlock = CustomBlock.get(blockState);
            if (customBlock != null) customBlock.onUnload(blockState);
        }
    }
    @EventHandler
    public void onLoad(ChunkLoadEvent event){loadChunk(event.getChunk());}
    @EventHandler
    public void onUnload(ChunkUnloadEvent event){unloadChunk(event.getChunk());}
    @EventHandler
    public void onReload(ServerLoadEvent event){
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) return;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadChunk(loadedChunk);
            }
        }
    }

    // TODO: 2/15/2024 FIX SO TWO ITEMS WONT FIRE AT ONE TICK
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getClickedBlock() == null) return;
        CustomBlock customBlock = CustomBlock.get(event.getClickedBlock());
        if (customBlock == null) return;
        customBlock.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_BLOCK).onRightClick(customBlock, event);
    }
}
