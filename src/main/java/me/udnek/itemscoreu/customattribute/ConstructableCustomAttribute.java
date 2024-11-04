package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import me.udnek.itemscoreu.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public class ConstructableCustomAttribute extends AbstractRegistrable implements CustomAttribute{

    protected final String rawId;
    protected final double defaultValue;
    protected final double minValue;
    protected final double maxValue;
    protected final boolean beneficial;

    public ConstructableCustomAttribute(@NotNull String rawId, double defaultValue, double min, double max, boolean beneficial){
        this.rawId = rawId;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;
        this.beneficial = beneficial;
    }
    public ConstructableCustomAttribute(@NotNull String rawId, double defaultValue, double min, double max){
        this(rawId,defaultValue, min, max, true);
    }

    public ConstructableCustomAttribute(@NotNull String rawId, double min, double max){
        this(rawId,0, min, max);
    }

    @Override
    public double getDefaultValue() {return defaultValue;}
    @Override
    public double getMinimum() {return minValue;}
    @Override
    public double getMaximum() {return maxValue;}

    @Override
    public double calculateWithBase(@NotNull LivingEntity entity, double base) {
        return CustomAttributeUtils.calculate(this, entity, base);
    }


    public @NotNull Component getLoreLine(double amount, @NotNull AttributeModifier.Operation operation) {
        TextColor color;
        String key;
        if (amount >= 0) {
            key = "attribute.modifier.plus.";
            if (beneficial) color = PLUS_COLOR;
            else color = TAKE_COLOR;
        } else {
            key = "attribute.modifier.take.";
            if (beneficial) color = TAKE_COLOR;
            else color = PLUS_COLOR;
        }

        key += switch (operation){
            case ADD_NUMBER -> "0";
            case ADD_SCALAR -> "1";
            case MULTIPLY_SCALAR_1 -> "2";
        };

        if (operation != AttributeModifier.Operation.ADD_NUMBER) amount*=100d;

        return Component.translatable(key, Component.text(Utils.roundToTwoDigits(Math.abs(amount))), Component.translatable(translationKey())).color(color);
    }

    @Override
    public @NotNull Component getLoreLineWithBase(double base) {
        return Component.translatable("attribute.modifier.equals.0", Component.text(Utils.roundToTwoDigits(base)), Component.translatable(translationKey())).color(EQUALS_COLOR);
    }

    @Override
    public @NotNull String getRawId() {return rawId;}

    @Override
    public @NotNull String translationKey() {
        return "attribute."+ NamespacedKey.fromString(getId()).getNamespace() +".name."+getRawId();
    }
}
