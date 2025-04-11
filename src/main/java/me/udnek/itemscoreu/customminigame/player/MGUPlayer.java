package me.udnek.itemscoreu.customminigame.player;

import me.udnek.itemscoreu.customcomponent.ComponentHolder;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customminigame.MGUManager;
import me.udnek.itemscoreu.customminigame.game.MGUGameInstance;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MGUPlayer extends ComponentHolder<MGUPlayer, CustomComponent<MGUPlayer>> {

    @NotNull MGUGameInstance getGame();
    @NotNull Player getPlayer();
    default void unregister(){
        MGUManager.get().unregisterPlayer(this);
    }
}
