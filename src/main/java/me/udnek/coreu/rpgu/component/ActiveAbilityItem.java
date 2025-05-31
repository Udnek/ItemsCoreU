package me.udnek.coreu.rpgu.component;

import me.udnek.coreu.custom.component.AbstractComponentHolder;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentMap;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import me.udnek.coreu.rpgu.component.ability.active.ActiveAbility;
import me.udnek.coreu.util.LoreBuilder;
import org.jetbrains.annotations.NotNull;

public class ActiveAbilityItem extends AbstractComponentHolder<ActiveAbilityItem, ActiveAbility<?>> implements CustomItemComponent {

    public static final ActiveAbilityItem DEFAULT = new ActiveAbilityItem(){
        @Override
        public @NotNull CustomComponentMap<ActiveAbilityItem, ActiveAbility<?>> getComponents() {
            return CustomComponentMap.immutableAlwaysEmpty();
        }
    };

    @Override
    public @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return RPGUComponentTypes.ACTIVE_ABILITY_ITEM;
    }

    @Override
    public void getLore(@NotNull CustomItem customItem, @NotNull LoreBuilder loreBuilder) {
        for (ActiveAbility<?> component : getComponents()) component.getLore(loreBuilder);
    }
}
