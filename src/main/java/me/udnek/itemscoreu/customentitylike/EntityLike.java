package me.udnek.itemscoreu.customentitylike;

import org.jetbrains.annotations.NotNull;

public interface EntityLike<Real, Type extends EntityLikeTickingType<?, ?>> {
    void load(@NotNull Real real);
    void unload();
    void tick();
    void remove();
    boolean isValid();
    @NotNull Real getReal();
    @NotNull Type getType();
}
