package me.udnek.itemscoreu.customentitylike.entity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customentitylike.EntityLikeType;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomEntityType extends EntityLikeType<Entity> {

    NamespacedKey PDC_NAMESPACE = new NamespacedKey(ItemsCoreU.getInstance(), "custom_entity_type");


    static @Nullable CustomEntityType get(@NotNull String id){
        return CustomRegistries.ENTITY_TYPE.get(id);
    }

    static @Nullable CustomEntityType get(@NotNull Entity entity) {
        String id = entity.getPersistentDataContainer().get(CustomEntityType.PDC_NAMESPACE, PersistentDataType.STRING);
        return id == null ? null : CustomEntityType.get(id);
    }


    static @Nullable CustomEntity getTicking(@NotNull Entity entity){
        return CustomEntityManager.getInstance().getTicking(entity);
    }

    static boolean isCustom(@NotNull Entity entity) {
        PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
        return dataContainer.has(CustomEntityType.PDC_NAMESPACE);
    }


    @NotNull Entity spawn(@NotNull Location location);

}
