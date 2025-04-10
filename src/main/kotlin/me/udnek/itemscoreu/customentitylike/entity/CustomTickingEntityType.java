package me.udnek.itemscoreu.customentitylike.entity;

import me.udnek.itemscoreu.customentitylike.EntityLikeTickingType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CustomTickingEntityType<CEntity extends CustomEntity> extends EntityLikeTickingType<Entity, CEntity>, CustomEntityType {

    default @NotNull CEntity spawnAndGet(@NotNull Location location){
        Entity spawned = spawn(location);
        return (CEntity) CustomEntityManager.getInstance().getTicking(spawned);
    }

}
