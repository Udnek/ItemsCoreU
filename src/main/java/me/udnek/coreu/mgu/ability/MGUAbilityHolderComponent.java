package me.udnek.coreu.mgu.ability;

import me.udnek.coreu.mgu.player.MGUPlayer;
import me.udnek.coreu.custom.component.AbstractComponentHolder;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentMap;
import me.udnek.coreu.custom.component.CustomComponentType;
import org.jetbrains.annotations.NotNull;

public class MGUAbilityHolderComponent extends AbstractComponentHolder<MGUAbilityHolderComponent, MGUAbilityInstance> implements CustomComponent<MGUPlayer>{

    public static final MGUAbilityHolderComponent DEFAULT = new MGUAbilityHolderComponent(){
        @Override
        public @NotNull CustomComponentMap<MGUAbilityHolderComponent, MGUAbilityInstance> getComponents() {
            return CustomComponentMap.immutableAlwaysEmpty();
        }
    };

    public MGUAbilityHolderComponent(){}

    @Override
    public @NotNull CustomComponentType<? extends MGUPlayer, ? extends CustomComponent<MGUPlayer>> getType() {
        return CustomComponentType.MGU_ABILITY_HOLDER;
    }
}
