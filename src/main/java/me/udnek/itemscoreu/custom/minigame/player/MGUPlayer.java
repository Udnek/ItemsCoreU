package me.udnek.itemscoreu.custom.minigame.player;

import me.udnek.itemscoreu.custom.minigame.MGUManager;
import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance;
import me.udnek.itemscoreu.customcomponent.ComponentHolder;
import me.udnek.itemscoreu.customcomponent.CustomComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MGUPlayer extends ComponentHolder<MGUPlayer, CustomComponent<MGUPlayer>> {

    @NotNull MGUGameInstance getGame();
    @NotNull Player getPlayer();
    default void unregister(){
        MGUManager.get().unregisterPlayer(this);
    }
}
