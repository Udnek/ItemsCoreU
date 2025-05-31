package me.udnek.coreu.mgu.player;

import me.udnek.coreu.mgu.MGUManager;
import me.udnek.coreu.mgu.ability.MGUAbilityHolderComponent;
import me.udnek.coreu.mgu.ability.MGUAbilityInstance;
import me.udnek.coreu.mgu.game.MGUGameInstance;
import me.udnek.coreu.custom.component.AbstractComponentHolder;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentMap;
import me.udnek.coreu.custom.component.CustomComponentType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MGUAbstractPlayer extends AbstractComponentHolder<MGUPlayer, CustomComponent<MGUPlayer>> implements MGUPlayer {

    private final MGUGameInstance game;
    private final Player player;

    public MGUAbstractPlayer(@NotNull Player player, @NotNull MGUGameInstance game){
        this.game = game;
        this.player = player;
        MGUManager.get().registerPlayer(this);
    }

    public @NotNull CustomComponentMap<MGUAbilityHolderComponent, MGUAbilityInstance> getAbilities(){
        return getComponents().getOrCreateDefault(CustomComponentType.MGU_ABILITY_HOLDER).getComponents();
    }

    @Override
    public @NotNull MGUGameInstance getGame() {
        return game;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
