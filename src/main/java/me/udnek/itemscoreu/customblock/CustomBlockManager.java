package me.udnek.itemscoreu.customblock;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customblock.block.CustomBlockEntity;
import me.udnek.itemscoreu.customblock.type.CustomBlockEntityType;
import me.udnek.itemscoreu.customblock.type.CustomBlockType;
import me.udnek.itemscoreu.util.TickingTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockManager extends TickingTask implements Listener {

    private static CustomBlockManager instance;

    private final List<CustomBlockEntity> loaded = new ArrayList<>();
    private final List<CustomBlockEntity> removeTickets = new ArrayList<>();

    public static @NotNull CustomBlockManager getInstance() {
        if (instance == null) {
            instance = new CustomBlockManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    private CustomBlockManager(){}


    private void load(@NotNull CustomBlockEntityType<?> type, @NotNull TileState blockState){
        CustomBlockEntity block = type.createNewBlockClass();
        loaded.add(block);
        block.load(blockState);
    }

    private void unload(@NotNull TileState block){
        for (CustomBlockEntity customBlock : loaded) {
            if (customBlock.getState() != block) continue;
            removeTickets.add(customBlock);
            return;
        }
        throw new RuntimeException("Trying to unload not loaded block entity: " + block);
    }


    @Override
    public void run() {
        removeTickets.forEach(CustomBlockEntity::unload);
        loaded.removeAll(removeTickets);
        removeTickets.clear();
        loaded.forEach(CustomBlockEntity::tick);
    }


    public void loadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get(blockState);
            if (customBlockType instanceof CustomBlockEntityType<?> blockEntityType) {
                CustomBlockManager.getInstance().load(blockEntityType, (TileState) blockState);
            }
        }
    }
    public void unloadChunk(@NotNull Chunk chunk){
        for (BlockState blockState : chunk.getTileEntities()) {
            CustomBlockType customBlockType = CustomBlockType.get(blockState);
            if (customBlockType instanceof CustomBlockEntityType<?>) {
                CustomBlockManager.getInstance().unload((TileState) blockState);
            }
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

    @Override
    public int getDelay() {return 1;}

}













