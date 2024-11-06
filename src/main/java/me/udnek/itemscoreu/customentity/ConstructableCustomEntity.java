package me.udnek.itemscoreu.customentity;

import net.minecraft.advancements.critereon.InputPredicate;
import net.minecraft.world.entity.player.Player;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomEntity<VanillaEntity extends Entity> implements CustomEntity {

    protected VanillaEntity entity;

    public abstract EntityType getVanillaEntityType();

    @Override
    public @NotNull Entity spawnNewEntity(Location location){
        this.entity = (VanillaEntity) location.getWorld().spawnEntity(location, getVanillaEntityType());
        return entity;
    }

    @Override
    public void load(@NotNull Entity entity) {
        this.entity = (VanillaEntity) entity;
    }

    @Override
    public void remove() {
        entity.remove();
        CustomEntityManager.getInstance().remove(entity);
    }
}
