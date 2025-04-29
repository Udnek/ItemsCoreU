package me.udnek.itemscoreu.custom.minigame.event;

import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance;
import me.udnek.itemscoreu.customevent.CustomEvent;
import org.jetbrains.annotations.NotNull;

public class MGUEvent extends CustomEvent {

    protected MGUGameInstance game;

    public MGUEvent(@NotNull MGUGameInstance game){
        this.game = game;
    }

    public @NotNull MGUGameInstance getGame() {
        return game;
    }
}
