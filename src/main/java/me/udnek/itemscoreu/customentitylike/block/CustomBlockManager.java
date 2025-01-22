package me.udnek.itemscoreu.customentitylike.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customentitylike.EntityLikeManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class CustomBlockManager extends EntityLikeManager<TileState, CustomBlockType, CustomBlockEntity> implements Listener {

    private static CustomBlockManager instance;

    public static CustomBlockManager getInstance() {
        if (instance == null) {
            instance = new CustomBlockManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    private CustomBlockManager(){}

    @Override
    protected boolean equals(@NotNull TileState r1, @NotNull TileState r2) {
        return r1.equals(r2);
    }

    public void loadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get((TileState) blockState);
            if (customBlockType != null) loadAny(customBlockType, (TileState) blockState);
        }
    }

    // TODO FIX SERVER CALLS
    public void unloadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get((TileState) blockState);
            if (customBlockType != null) unloadAny(customBlockType, (TileState) blockState);
        }
    }

    @EventHandler
    public void onDestroy(BlockDestroyEvent event){
        CustomBlockType customBlockType = CustomBlockType.get(event.getBlock());
        if (customBlockType != null) customBlockType.onDestroy(event);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        CustomBlockType customBlockType = CustomBlockType.get(event.getBlock());
        if (customBlockType != null) customBlockType.onDestroy(event);
    }
    @EventHandler
    public void onExplode(BlockExplodeEvent event){
        BlockState blockState = event.getExplodedBlockState();
        if (!(blockState instanceof TileState tileState)) return;
        CustomBlockType customBlockType = CustomBlockType.get(tileState);
        if (customBlockType != null) customBlockType.onDestroy(event);
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event){
        for (Block block : event.blockList()) {
            CustomBlockType customBlockType = CustomBlockType.get(event.getLocation().getBlock());
            if (customBlockType != null) customBlockType.onDestroy(event, block);
        }
    }

    // todo fade
    // todo burn
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getClickedBlock() == null) return;
        CustomBlockType customBlockType = CustomBlockType.get(event.getClickedBlock());
        if (customBlockType == null) return;
        customBlockType.getComponents().getOrDefault(CustomComponentType.RIGHT_CLICKABLE_BLOCK).onRightClick(customBlockType, event);
    }

    @EventHandler
    public void onPlayerLoadChunk(PlayerChunkLoadEvent event){
        for (@NotNull BlockState blockState : event.getChunk().getTileEntities()) {
            CustomBlockType blockType = CustomBlockType.get((TileState) blockState);
            if (blockType != null) blockType.onPlayerLoads(event, (TileState) blockState);
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

}
