package me.udnek.itemscoreu.custom.minigame.player;

import me.udnek.itemscoreu.custom.minigame.MGUManager;
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityHolderComponent;
import me.udnek.itemscoreu.custom.minigame.ability.MGUAbilityInstance;
import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance;
import me.udnek.itemscoreu.customcomponent.AbstractComponentHolder;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentMap;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
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
