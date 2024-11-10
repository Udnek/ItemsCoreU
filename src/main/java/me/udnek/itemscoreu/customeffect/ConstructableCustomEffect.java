package me.udnek.itemscoreu.customeffect;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.util.Reflex;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.lang.reflect.Constructor;

public abstract class ConstructableCustomEffect extends AbstractRegistrable implements CustomEffect{
    protected Holder<MobEffect> nmsEffect;


    public abstract @NotNull PotionEffectTypeCategory getCategory();
    public @NotNull Color getColor(){return Color.WHITE;}
    public @Nullable Particle getParticle(){return null;}
    public void addAttributes(@NotNull AttributeConsumer consumer){}

    @Nullable
    public abstract PotionEffectType getVanillaDisguise();

    @Override
    public void initialize(@NotNull Plugin plugin) {
        super.initialize(plugin);
        MobEffectCategory category = switch (getCategory()){
            case HARMFUL -> MobEffectCategory.HARMFUL;
            case BENEFICIAL -> MobEffectCategory.BENEFICIAL;
            case NEUTRAL -> MobEffectCategory.NEUTRAL;
        };
        MobEffect mobEffect;
        if (getParticle() == null){
            Constructor<MobEffect> constructor = Reflex.getConstructor(MobEffect.class, MobEffectCategory.class, int.class);
            mobEffect = Reflex.construct(constructor, category, getColor().getRGB());
        } else {
            Constructor<MobEffect> constructor = Reflex.getConstructor(MobEffect.class, MobEffectCategory.class, int.class, ParticleOptions.class);
            ParticleType<?> particleType = NmsUtils.getRegistry(Registries.PARTICLE_TYPE).getValue(NmsUtils.getResourceLocation(getParticle().getKey()));
            mobEffect = Reflex.construct(constructor, category, getColor().getRGB(), particleType);
        }

        addAttributes((attribute, key, amount, operation) ->
                        mobEffect.addAttributeModifier(
                                NmsUtils.toNms(Registries.ATTRIBUTE, attribute),
                                NmsUtils.getResourceLocation(key),
                                amount,
                                NmsUtils.toNmsOperation(operation)
                        )
        );


        nmsEffect = Nms.get().registerEffect(mobEffect, new NamespacedKey(plugin, getRawId()));
    }

    @Override
    public void apply(@NotNull LivingEntity bukkit, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        net.minecraft.world.entity.LivingEntity entity = NmsUtils.toNmsEntity(bukkit);
        entity.addEffect(new MobEffectInstance(nmsEffect, duration, amplifier, ambient, visible, showIcon));
    }

    public interface AttributeConsumer{
        void accept(@NotNull Attribute attribute, @NotNull Key key, double amount, @NotNull org.bukkit.attribute.AttributeModifier.Operation operation);
    }
}
