package me.udnek.itemscoreu.customentitylike.entity;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomEntityType<T extends Entity> extends AbstractRegistrable implements CustomEntityType {

    public abstract @NotNull EntityType getVanillaType();

    @MustBeInvokedByOverriders
    public @NotNull T spawnNewEntity(@NotNull Location location){
        return (T) location.getWorld().spawnEntity(location, getVanillaType());
    }

    public final @NotNull T spawn(@NotNull Location location) {
        T entity = spawnNewEntity(location);
        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(PDC_NAMESPACE, PersistentDataType.STRING, getId());
        CustomEntityManager.getInstance().loadAny(this, entity);
        return entity;
    }
}
