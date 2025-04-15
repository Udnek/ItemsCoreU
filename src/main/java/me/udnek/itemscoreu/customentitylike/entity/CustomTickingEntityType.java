package me.udnek.itemscoreu.customentitylike.entity;

import me.udnek.itemscoreu.customentitylike.EntityLikeTickingType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomTickingEntityType<CEntity extends CustomEntity> extends EntityLikeTickingType<Entity, CEntity>, CustomEntityType {

    default @NotNull CEntity spawnAndGet(@NotNull Location location){
        Entity spawned = spawn(location);
        return (CEntity) CustomEntityManager.getInstance().getTicking(spawned);
    }

    default @Nullable CEntity getIsThis(@NotNull Entity entity) {
        CustomEntity customEntity = CustomEntityType.getTicking(entity);
        return customEntity == null || customEntity.getType() != this ? null : (CEntity) customEntity;
    }

}
