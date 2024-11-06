package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEntityType<CEntity extends CustomEntity> extends AbstractRegistrable {
    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey(ItemsCoreU.getInstance(), "custom_entity_type");
    protected final String rawId;

    public CustomEntityType(@NotNull String rawId) {
        this.rawId = rawId;
    }

    public @NotNull String getRawId() {return rawId;}

    public boolean isThisEntity(@NotNull Entity entity){
        return CustomEntity.getType(entity) == this;
    }


    protected abstract CEntity getNewCustomEntityClass();
    public CEntity spawn(@NotNull Location location) {
        CEntity customEntity = getNewCustomEntityClass();
        Entity entity = customEntity.spawnNewEntity(location);

        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(NAMESPACED_KEY, PersistentDataType.STRING, getId());

        CustomEntityManager.getInstance().add(entity, customEntity);
        customEntity.onSpawn();

        return customEntity;
    }
    public void load(@NotNull Entity entity) {
        CustomEntity customEntity = getNewCustomEntityClass();
        CustomEntityManager.getInstance().add(entity, customEntity);
        customEntity.load(entity);
    }
}
