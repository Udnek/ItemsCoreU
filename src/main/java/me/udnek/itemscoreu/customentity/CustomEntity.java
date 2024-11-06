package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface CustomEntity {

    static @Nullable CustomEntityType<?> getType(@NotNull Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        if (!dataContainer.has(CustomEntityType.NAMESPACED_KEY)) return null;
        String id = dataContainer.get(CustomEntityType.NAMESPACED_KEY, PersistentDataType.STRING);
        if (id == null) return null;
        return getType(id);
    }
    static @Nullable CustomEntityType<?> getType(@NotNull String id){
        return CustomRegistries.ENTITY_TYPE.get(id);
    }
    static @Nullable CustomEntity get(@NotNull Entity entity){
        return CustomEntityManager.getInstance().get(entity);
    }
    static boolean isCustom(@NotNull Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        return dataContainer.has(CustomEntityType.NAMESPACED_KEY);
    }
    static boolean idExists(String id){
        return getType(id) != null;
    }


    @NotNull Entity spawnNewEntity(Location location);
    void onSpawn();
    void load(@NotNull Entity entity);
    void unload();
    void tick();
    void remove();
    @NotNull CustomEntityType<?> getType();
}
