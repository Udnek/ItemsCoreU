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
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CustomEntityManager extends TickingTask implements Listener {
    private static CustomEntityManager instance;
    protected final List<CustomEntityHolder> loadedEntities = new ArrayList<>();
    protected final List<CustomEntityHolder> toRemove = new ArrayList<>();
    private CustomEntityManager(){}
    public static CustomEntityManager getInstance() {
        if (instance == null) {
            instance = new CustomEntityManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    public void add(@NotNull Entity entity, @NotNull CustomEntity customEntity){
        if (isLoaded(entity)) return;
        loadedEntities.add(new CustomEntityHolder(entity, customEntity));
    }

    public void remove(@NotNull Entity entity){
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
    public boolean isLoaded(@NotNull Entity entity){
        return get(entity) != null;
    }

    @Override
    public int getDelay() {return 1;}
    @Override
    public void run() {
        for (CustomEntityHolder loadedEntity : loadedEntities) {
            if (loadedEntity.realEntity.isValid() && loadedEntity.realEntity.getChunk().isEntitiesLoaded()){
                loadedEntity.customEntity.tick();
            } else {
                toRemove.add(loadedEntity);
            }
        }
        for (CustomEntityHolder holder : toRemove) {
            holder.customEntity.unload();
        }
        loadedEntities.removeAll(toRemove);
        toRemove.clear();
    }

    public void loadEntities(@NotNull List<Entity> entities){
        for (Entity entity : entities) {
            CustomEntityType<?> entityType = CustomEntity.getType(entity);
            if (entityType != null) entityType.load(entity);
        }
    }

    @EventHandler
    public void onEntitiesUnload(@NotNull EntitiesUnloadEvent event){
        for (Entity entity : event.getEntities()) {
            CustomEntityHolder holder = getHolder(entity);
            if (holder != null) toRemove.add(holder);
        }
    }
    @EventHandler
    public void onEntitiesLoaded(EntitiesLoadEvent event) {
        loadEntities(event.getEntities());
    }
    @EventHandler
    public void onServerRestart(ServerLoadEvent event){
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) return;

        for (World world : Bukkit.getWorlds()) {
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadEntities(Arrays.asList(loadedChunk.getEntities()));
            }
        }
    }
}
