package me.udnek.itemscoreu.customentity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomEntity<VanillaEntity extends Entity> implements CustomEntity {

    protected VanillaEntity entity;

    public abstract @NotNull EntityType getVanillaEntityType();

    @Override
    @MustBeInvokedByOverriders
    public @NotNull VanillaEntity spawn(@NotNull Location location){
        this.entity = (VanillaEntity) location.getWorld().spawnEntity(location, getVanillaEntityType());
        return entity;
    }

    @Override
    public @NotNull VanillaEntity getRealEntity() {
        return entity;
    }

    @Override
    @MustBeInvokedByOverriders
    public void load(@NotNull Entity entity) {
        this.entity = (VanillaEntity) entity;
    }

    @Override
    public final void tick() {
        if (Bukkit.getCurrentTick() % getTickDelay() == 0) delayedTick();
    }
    public int getTickDelay(){return 5;}

    public abstract void delayedTick();

    @Override
    @MustBeInvokedByOverriders
    public void remove() {
        entity.remove();
        CustomEntityManager.getInstance().unload(entity);
    }
}
