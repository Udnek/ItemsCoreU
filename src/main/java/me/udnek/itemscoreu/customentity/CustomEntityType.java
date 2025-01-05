package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomEntityType<CEntity extends CustomEntity> extends AbstractRegistrable {
    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey(ItemsCoreU.getInstance(), "custom_entity_type");
    protected final String rawId;


    public static @Nullable CustomEntityType<?> get(@NotNull String id){
        return CustomRegistries.ENTITY_TYPE.get(id);
    }

    public CustomEntityType(@NotNull String rawId) {
        this.rawId = rawId;
    }

    public @NotNull String getRawId() {return rawId;}

    public boolean isThisEntity(@NotNull Entity entity){
        return CustomEntity.getType(entity) == this;
    }

    protected abstract @NotNull CEntity getNewCustomEntityClass();

    public @NotNull CEntity spawn(@NotNull Location location) {
        CEntity customEntity = getNewCustomEntityClass();
        Entity entity = customEntity.spawn(location);

        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(NAMESPACED_KEY, PersistentDataType.STRING, getId());

        CustomEntityManager.getInstance().load(entity, customEntity);
        customEntity.afterSpawn();

        return customEntity;
    }
    public void load(@NotNull Entity entity) {
        CustomEntity customEntity = getNewCustomEntityClass();
        CustomEntityManager.getInstance().load(entity, customEntity);
        customEntity.load(entity);
    }
}
