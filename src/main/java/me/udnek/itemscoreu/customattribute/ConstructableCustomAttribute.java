package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customregistry.AbstractRegistrable;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ConstructableCustomAttribute extends AbstractRegistrable implements CustomAttribute{

    protected final String rawId;
    protected final String translation;
    protected final double defaultValue;
    protected final double minValue;
    protected final double maxValue;

    public ConstructableCustomAttribute(@NotNull String rawId, @NotNull String translation, double defaultValue, double min, double max){
        this.rawId = rawId;
        this.translation = translation;
        this.defaultValue = defaultValue;
        this.minValue = min;
        this.maxValue = max;
    }
    public ConstructableCustomAttribute(@NotNull String rawId, @NotNull String translation, double min, double max){
        this(rawId, translation, 0, min, max);
    }

    @Override
    public double getDefaultValue() {return defaultValue;}
    @Override
    public double getMinimum() {return minValue;}
    @Override
    public double getMaximum() {return maxValue;}

    @Override
    public double calculate(@NotNull LivingEntity entity){
        return CustomAttributeUtils.calculate(this, entity);
    }

    @Override
    public @NotNull String getRawId() {
        return rawId;
    }

    @Override
    public @NotNull String translationKey() {
        return translation;
    }
}
