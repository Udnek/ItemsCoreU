package me.udnek.itemscoreu.customentity;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.PluginInitializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


// TODO: 7/18/2024 MAKE IMPLEMENTS CUSTOM ENTITY
// TODO: 7/18/2024 GET RAW ID
public abstract class CustomDumbTickingEntity implements PluginInitializable {

    public static final NamespacedKey namespacedKey = new NamespacedKey(ItemsCoreU.getInstance(), "custom_dumb_entity");
    protected String id;

    @Override
    public void initialize(@NotNull JavaPlugin javaPlugin){
        id = new NamespacedKey(javaPlugin, getName()).asString();
    }

    public Entity spawn(Location location){
        Entity entity = location.getWorld().spawnEntity(location, getEntityType());
        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, getId());
        ItemsCoreU.getCustomEntityTicker().addEntity(entity, this);

        extraAdjustAfterSpawn(entity);

        return entity;
    }

    public static long getFixedCurrentTick(){
        return Bukkit.getCurrentTick() / ItemsCoreU.getCustomEntityTicker().getDelay();
    }

    protected void extraAdjustAfterSpawn(Entity entity){}

    public abstract EntityType getEntityType();
    protected abstract String getName();

    public String getId() {
        return id;
    }

    public abstract void tick(Entity entity);
    public void onLoad(Entity entity){}
    public void onUnload(Entity entity){}

}
