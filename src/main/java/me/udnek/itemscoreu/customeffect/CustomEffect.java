package me.udnek.itemscoreu.customeffect;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import net.minecraft.core.registries.Registries;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomEffect extends Registrable {

    static boolean isCustom(@NotNull PotionEffectType bukkit){
        return CustomRegistries.EFFECT.contains(bukkit.key().asString());
    }
    static @Nullable CustomEffect get(@NotNull PotionEffectType bukkit){
        return CustomRegistries.EFFECT.get(bukkit.key().asString());
    }


    void apply(@NotNull LivingEntity entity, int duration, int amplifier, boolean ambient, boolean showParticles, boolean showIcon);
    default void apply(@NotNull LivingEntity entity, @NotNull PotionEffect effect){
        apply(entity, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), effect.hasIcon());
    }
    default void apply(@NotNull LivingEntity entity, int duration, int amplifier){
        apply(entity, duration, amplifier, false, true, true);
    }
    default void applyInvisible(@NotNull LivingEntity entity, int duration, int amplifier){
        apply(entity, duration, amplifier, false, false, false);
    }
    boolean has(@NotNull LivingEntity entity);
    @Nullable PotionEffect get(@NotNull LivingEntity entity);
    default int getAppliedLevel(@NotNull LivingEntity entity){
        PotionEffect effect = get(entity);
        if (effect == null) return -1;
        return effect.getAmplifier();
    }
    @NotNull CustomAttributesContainer getCustomAttributes(@NotNull PotionEffect context, @NotNull CustomAttributeConsumer consumer);

    interface CustomAttributeConsumer{
        void consume(@NotNull CustomAttribute attribute, double amount, @NotNull AttributeModifier.Operation operation);
    }
}
