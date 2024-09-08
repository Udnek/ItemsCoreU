package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.PluginInitializable;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class CustomEntityType implements PluginInitializable {
    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey(ItemsCoreU.getInstance(), "custom_entity_type");
    protected final String rawId;
    protected String id;

    public CustomEntityType(@NotNull String rawId) {
        this.rawId = rawId;
    }
    @Override
    public void initialize(@NotNull JavaPlugin plugin) {
        this.id = new NamespacedKey(plugin, rawId).asString();
    }

    @NotNull String getRawId() {return rawId;}
    @NotNull String getId() {return id;}
    protected abstract CustomEntity getNewCustomEntityClass();
    public void spawn(Location location) {
        CustomEntity customEntity = getNewCustomEntityClass();
        Entity entity = customEntity.getNewEntity(location);

        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(NAMESPACED_KEY, PersistentDataType.STRING, getId());

        CustomEntityManager.getInstance().add(entity, customEntity);
        customEntity.onSpawn();
    }
    public void load(Entity entity) {
        CustomEntity customEntity = getNewCustomEntityClass();
        CustomEntityManager.getInstance().add(entity, customEntity);
        customEntity.load(entity);
    }
}
