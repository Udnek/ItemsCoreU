package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.util.TickingTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomEntityManager extends TickingTask implements Listener {
    private static CustomEntityManager instance;
    private final List<CustomEntityHolder> loadedEntities = new ArrayList<>();
    private CustomEntityManager(){}
    public static CustomEntityManager getInstance() {
        if (instance == null) {
            instance = new CustomEntityManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    public void add(Entity entity, CustomEntity customEntity){
        if (isLoaded(entity)) return;
        //Preconditions.checkArgument(!isLoaded(entity), "Entity " + entity + " is already loaded!");
        loadedEntities.add(new CustomEntityHolder(entity, customEntity));
    }

    public void remove(Entity entity){
        CustomEntityHolder holder = getHolder(entity);
        if (holder == null) return;
        loadedEntities.remove(holder);
    }
    protected @Nullable CustomEntityHolder getHolder(Entity entity){
        UUID uniqueId = entity.getUniqueId();
        for (CustomEntityHolder loadedEntity : loadedEntities) {
            if (loadedEntity.realEntity.getUniqueId().equals(uniqueId)){
                return loadedEntity;
            }
        }
        return null;
    }
    public @Nullable CustomEntity get(Entity entity){
        UUID uniqueId = entity.getUniqueId();
        for (CustomEntityHolder loadedEntity : loadedEntities) {
            if (loadedEntity.realEntity.getUniqueId().equals(uniqueId)){
                return loadedEntity.customEntity;
            }
        }
        return null;
    }
    public boolean isLoaded(Entity entity){
        return get(entity) != null;
    }

    @Override
    public int getDelay() {return 1;}
    @Override
    public void run() {
        List<CustomEntityHolder> toRemoveHolders = new ArrayList<>();
        for (CustomEntityHolder loadedEntity : loadedEntities) {
            if (!loadedEntity.realEntity.isValid()){
                loadedEntity.customEntity.unload();
                toRemoveHolders.add(loadedEntity);
                continue;
            }
            loadedEntity.customEntity.tick();
        }
        loadedEntities.removeAll(toRemoveHolders);
    }

    public void loadEntitiesInChunk(Chunk chunk){
        @NotNull Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            CustomEntityType<?> entityType = CustomEntity.getType(entity);
            if (entityType != null) entityType.load(entity);
        }
    }

    @EventHandler
    public void onChunkLoaded(ChunkLoadEvent event) {
        if (event.isNewChunk()) return;
        loadEntitiesInChunk(event.getChunk());
    }
    @EventHandler
    public void onServerRestart(ServerLoadEvent event){
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) return;

        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadEntitiesInChunk(loadedChunk);
            }
        }
    }
}
