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

    private final List<Holder> loaded = new ArrayList<>();
    private final List<Holder> toRemove = new ArrayList<>();

    public static @NotNull CustomEntityManager getInstance() {
        if (instance == null) {
            instance = new CustomEntityManager();
            Bukkit.getPluginManager().registerEvents(instance, ItemsCoreU.getInstance());
        }
        return instance;
    }

    private CustomEntityManager(){}

    @NotNull List<Holder> getAllLoaded() {
        return loaded;
    }

    public void load(@NotNull Entity entity, @NotNull CustomEntity customEntity){
        if (isLoaded(entity)) return;
        loaded.add(new Holder(entity, customEntity));
    }

    public void unload(@NotNull Entity entity){
        Holder holder = getHolder(entity);
        if (holder == null) return;
        loaded.remove(holder);
    }

    protected @Nullable Holder getHolder(@NotNull Entity entity){
        UUID uniqueId = entity.getUniqueId();
        for (Holder loadedEntity : loaded) {
            if (loadedEntity.realEntity.getUniqueId().equals(uniqueId)){
                return loadedEntity;
            }
        }
        return null;
    }

    public @Nullable CustomEntity getCustom(@NotNull Entity entity){
        Holder holder = getHolder(entity);
        return holder == null ? null : holder.customEntity;
    }

    public boolean isLoaded(@NotNull Entity entity){
        return getCustom(entity) != null;
    }

    @Override
    public int getDelay() {return 1;}
    @Override
    public void run() {
        for (Holder loadedEntity : loaded) {
            if (loadedEntity.realEntity.isValid() && loadedEntity.realEntity.getChunk().isEntitiesLoaded()){
                loadedEntity.customEntity.tick();
            } else {
                toRemove.add(loadedEntity);
            }
        }
        toRemove.forEach(holder -> holder.customEntity.unload());
        loaded.removeAll(toRemove);
        toRemove.clear();
    }

    private void loadEntities(@NotNull List<Entity> entities){
        for (Entity entity : entities) {
            CustomEntityType<?> entityType = CustomEntity.getType(entity);
            if (entityType != null) entityType.load(entity);
        }
    }

    @EventHandler
    public void onEntitiesUnload(EntitiesUnloadEvent event){
        for (Entity entity : event.getEntities()) {
            Holder holder = getHolder(entity);
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

    public record Holder(@NotNull Entity realEntity, @NotNull CustomEntity customEntity){
        @Override
        public @NotNull Entity realEntity() {return realEntity;}

        @Override
        public @NotNull CustomEntity customEntity() {return customEntity;}
    }
}
