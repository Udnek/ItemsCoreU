package me.udnek.coreu.mgu.player;

import me.udnek.coreu.mgu.MGUManager;
import me.udnek.coreu.mgu.game.MGUGameInstance;
import me.udnek.coreu.custom.component.ComponentHolder;
import me.udnek.coreu.custom.component.CustomComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MGUPlayer extends ComponentHolder<MGUPlayer, CustomComponent<MGUPlayer>> {

    @NotNull MGUGameInstance getGame();
    @NotNull Player getPlayer();
    default void unregister(){
        MGUManager.get().unregisterPlayer(this);
    }
}
