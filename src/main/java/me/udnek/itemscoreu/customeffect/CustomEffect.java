package me.udnek.itemscoreu.customeffect;

import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public interface CustomEffect extends Registrable {
    void apply(@NotNull LivingEntity entity, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon);
    default void apply(@NotNull LivingEntity entity, @NotNull PotionEffect effect){
        apply(entity, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), true, effect.hasIcon());
    }
}
