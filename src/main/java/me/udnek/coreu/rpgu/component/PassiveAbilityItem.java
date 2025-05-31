package me.udnek.coreu.rpgu.component;

import me.udnek.coreu.custom.component.AbstractComponentHolder;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentMap;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import me.udnek.coreu.rpgu.component.ability.passive.PassiveAbility;
import me.udnek.coreu.util.LoreBuilder;
import org.jetbrains.annotations.NotNull;

public class PassiveAbilityItem extends AbstractComponentHolder<PassiveAbilityItem, PassiveAbility<?>> implements CustomItemComponent {

    public static final PassiveAbilityItem DEFAULT = new PassiveAbilityItem(){
        @Override
        public @NotNull CustomComponentMap<PassiveAbilityItem, PassiveAbility<?>> getComponents() {
            return CustomComponentMap.immutableAlwaysEmpty();
        }
    };

    @Override
    public @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return RPGUComponentTypes.PASSIVE_ABILITY_ITEM;
    }

    @Override
    public void getLore(@NotNull CustomItem customItem, @NotNull LoreBuilder loreBuilder) {
        for (PassiveAbility<?> component : getComponents()) component.getLore(loreBuilder);
    }
}
