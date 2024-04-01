package me.udnek.itemscoreu.customentity;

import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.RestartCommand;

import java.util.Arrays;
import java.util.Iterator;

public class CustomEntityListener extends SelfRegisteringListener {
    public CustomEntityListener(JavaPlugin plugin) {
        super(plugin);
    }

    public void loadEntitiesInChunk(Chunk chunk){

        @NotNull Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
            if (!dataContainer.has(CustomDumbTickingEntity.namespacedKey)) continue;

            String id = dataContainer.get(CustomDumbTickingEntity.namespacedKey, PersistentDataType.STRING);
            CustomDumbTickingEntity customDumbTickingEntity = CustomEntityManager.get(id);
            ItemsCoreU.getCustomEntityTicker().addEntity(entity, customDumbTickingEntity);
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

        Iterator<World> worldIterator = Bukkit.getWorlds().iterator();
        if (worldIterator.hasNext()) {
            World world = worldIterator.next();
            for (Chunk loadedChunk : world.getLoadedChunks()) {
                loadEntitiesInChunk(loadedChunk);
            }
        }

    }
}





















