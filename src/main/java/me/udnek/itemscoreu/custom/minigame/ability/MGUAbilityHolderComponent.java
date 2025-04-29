package me.udnek.itemscoreu.custom.minigame.ability;

import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer;
import me.udnek.itemscoreu.customcomponent.AbstractComponentHolder;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentMap;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import org.jetbrains.annotations.NotNull;

public class MGUAbilityHolderComponent extends AbstractComponentHolder<MGUAbilityHolderComponent, MGUAbilityInstance> implements CustomComponent<MGUPlayer>{

    public static final MGUAbilityHolderComponent DEFAULT = new MGUAbilityHolderComponent(){
        private static final CustomComponentMap<MGUAbilityHolderComponent, MGUAbilityInstance> EMPTY = CustomComponentMap.newAlwaysEmpty();
        @Override
        public @NotNull CustomComponentMap<MGUAbilityHolderComponent, MGUAbilityInstance> getComponents() {return EMPTY;}
    };

    public MGUAbilityHolderComponent(){}

    @Override
    public @NotNull CustomComponentType<? extends MGUPlayer, ? extends CustomComponent<MGUPlayer>> getType() {
        return CustomComponentType.MGU_ABILITY_HOLDER;
    }
}
