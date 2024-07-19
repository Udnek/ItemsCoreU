package me.udnek.itemscoreu.customentity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface CustomEntity {

    static @Nullable CustomEntityType getType(Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        if (!dataContainer.has(CustomEntityType.NAMESPACED_KEY)) return null;
        String id = dataContainer.get(CustomEntityType.NAMESPACED_KEY, PersistentDataType.STRING);
        return getType(id);
    }
    static @Nullable CustomEntityType getType(String id){
        return CustomEntityTypeRegistry.getInstance().get(id);
    }
    static @Nullable CustomEntity get(Entity entity){
        return CustomEntityManager.getInstance().get(entity);
    }
    static boolean isCustom(Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        return dataContainer.has(CustomEntityType.NAMESPACED_KEY);
    }
    static List<String> getAllIds(){
        return new ArrayList<>(CustomEntityTypeRegistry.getInstance().getAllIds());
    }
    static boolean idExists(String id){
        return getType(id) != null;
    }


    @NotNull Entity getNewEntity(Location location);
    void onSpawn();
    void load(Entity entity);
    void unload();
    void tick();
}
