package me.udnek.coreu.rpgu.component;

import me.udnek.coreu.CoreU;
import me.udnek.coreu.custom.component.ConstructableComponentType;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.coreu.rpgu.attribute.PRGUAttributes;
import me.udnek.coreu.rpgu.component.ability.Ability;
import me.udnek.coreu.rpgu.component.ability.property.CastTimeProperty;
import me.udnek.coreu.rpgu.component.ability.property.EffectsProperty;
import me.udnek.coreu.rpgu.component.ability.property.MissUsageCooldownMultiplierProperty;
import me.udnek.coreu.rpgu.component.ability.property.type.AttributeBasedPropertyType;

public class RPGUComponentTypes {

    public static final CustomComponentType<CustomItem, ActiveAbilityItem> ACTIVE_ABILITY_ITEM;
    public static final CustomComponentType<CustomItem, PassiveAbilityItem> PASSIVE_ABILITY_ITEM;

    public static final AttributeBasedPropertyType ABILITY_COOLDOWN;
    public static final AttributeBasedPropertyType ABILITY_CAST_RANGE;
    public static final AttributeBasedPropertyType ABILITY_AREA_OF_EFFECT;
    public static final AttributeBasedPropertyType ABILITY_DURATION;
    public static final CustomComponentType<Ability<?>, CastTimeProperty> ABILITY_CAST_TIME;
    public static final CustomComponentType<Ability<?>, MissUsageCooldownMultiplierProperty> ABILITY_MISS_USAGE_COOLDOWN_MULTIPLIER;
    public static final CustomComponentType<Ability<?>, EffectsProperty> ABILITY_EFFECTS;

    static {
        ACTIVE_ABILITY_ITEM = register(new ConstructableComponentType<>("rpgu_active_ability_item", ActiveAbilityItem.DEFAULT));
        PASSIVE_ABILITY_ITEM = register(new ConstructableComponentType<>("rpgu_passive_ability_item", PassiveAbilityItem.DEFAULT));

        ABILITY_COOLDOWN = register(new AttributeBasedPropertyType("rpgu_ability_cooldown", PRGUAttributes.COOLDOWN_TIME, -1, "ability.rpgu.cooldown", true));
        ABILITY_CAST_RANGE = register(new AttributeBasedPropertyType("rpgu_ability_cast_range", PRGUAttributes.CAST_RANGE, -1, "ability.rpgu.cast_range"));
        ABILITY_AREA_OF_EFFECT = register(new AttributeBasedPropertyType("rpgu_ability_area_of_effect", PRGUAttributes.AREA_OF_EFFECT, -1, "ability.rpgu.area_of_effect"));
        ABILITY_DURATION = register(new AttributeBasedPropertyType("rpgu_ability_duration", PRGUAttributes.ABILITY_DURATION, -1, "ability.rpgu.duration", true));
        ABILITY_CAST_TIME = register(new ConstructableComponentType<>("rpgu_ability_cast_time", new CastTimeProperty(-1)));
        ABILITY_MISS_USAGE_COOLDOWN_MULTIPLIER = register(new ConstructableComponentType<>("rpgu_ability_miss_usage_cooldown_multiplier", new MissUsageCooldownMultiplierProperty(0.3)));
        ABILITY_EFFECTS =  register(new ConstructableComponentType<>("rpgu_ability_effects", EffectsProperty.DEFAULT));
    }

    private static <T extends CustomComponentType<?, ?>> T register(T type){
        return CustomRegistries.COMPONENT_TYPE.register(CoreU.getInstance(), type);
    }
}
