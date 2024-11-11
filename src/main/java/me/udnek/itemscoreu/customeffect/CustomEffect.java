package me.udnek.itemscoreu.customeffect;

import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomEffect extends Registrable {
    void apply(@NotNull LivingEntity entity, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon);
    default void apply(@NotNull LivingEntity entity, @NotNull PotionEffect effect){
        apply(entity, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), effect.hasIcon());
    }
    default void apply(@NotNull LivingEntity entity, int duration, int amplifier){
        apply(entity, duration, amplifier, false, true, true);
    }
    boolean has(@NotNull LivingEntity entity);
    @Nullable PotionEffect get(@NotNull LivingEntity entity);
    default int getAppliedLevel(@NotNull LivingEntity entity){
        PotionEffect effect = get(entity);
        if (effect == null) return -1;
        return effect.getAmplifier();
    }
}
