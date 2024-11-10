package me.udnek.itemscoreu.customeffect;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestEffect extends ConstructableCustomEffect{
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {
        return PotionEffectTypeCategory.BENEFICIAL;
    }

    @Override
    public void addAttributes(@NotNull AttributeConsumer consumer) {
        consumer.accept(Attribute.SCALE, new NamespacedKey(ItemsCoreU.getInstance(), "test"), 2, AttributeModifier.Operation.ADD_NUMBER);
    }

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {
        return PotionEffectType.UNLUCK;
    }

    @Override
    public @Nullable Particle getParticle() {
        return Particle.SONIC_BOOM;
    }

    @Override
    public @NotNull String getRawId() {
        return "test";
    }
}
