package me.udnek.coreu.custom.entitylike;

import org.jetbrains.annotations.NotNull;

public interface EntityLikeTickingType<Real, Entity extends EntityLike<?, ?>> extends EntityLikeType<Real> {
    @NotNull Entity createNewClass();
}
