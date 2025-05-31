package me.udnek.coreu.rpgu.component.ability.property;

import me.udnek.coreu.rpgu.component.ability.property.function.AttributeFunction;
import me.udnek.coreu.rpgu.component.ability.property.type.AttributeBasedPropertyType;
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AttributeBasedProperty extends AbstractAbilityProperty<LivingEntity, Double> {

    protected @NotNull AttributeBasedPropertyType type;

    public AttributeBasedProperty(@NotNull AttributeFunction function, @NotNull AttributeBasedPropertyType type) {
        super(function);
        this.type = type;
    }

    public AttributeBasedProperty(double base, @NotNull AttributeBasedPropertyType type) {
        this(new AttributeFunction(type.getAttribute(), base), type);
    }

    @Override
    public @NotNull AttributeBasedPropertyType getType() {return type;}

    @Override
    public @NotNull Double get(@NotNull LivingEntity livingEntity) {
        if (getFunction().getBase() < getType().getAttribute().getMin()) {
            return getType().getAttribute().getMin();
        }
        return getFunction().apply(livingEntity);
    }

    @Override
    public void describe(@NotNull AbilityLorePart componentable) {
        getType().describe(this, componentable);
    }
}
