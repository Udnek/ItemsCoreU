package me.udnek.itemscoreu.customentity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomEntity {

    static @Nullable CustomEntityType<?> getType(@NotNull Entity entity) {
        CustomEntity customEntity = get(entity);
        return customEntity == null ? null : customEntity.getType();
    }
    static @Nullable CustomEntity get(@NotNull Entity entity){
        return CustomEntityManager.getInstance().getCustom(entity);
    }
    static boolean isCustom(@NotNull Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        return dataContainer.has(CustomEntityType.NAMESPACED_KEY);
    }

    @NotNull Entity getRealEntity();
    @NotNull Entity spawn(@NotNull Location location);
    void afterSpawn();
    void load(@NotNull Entity entity);
    void unload();
    void tick();
    void remove();
    @NotNull CustomEntityType<?> getType();
}
