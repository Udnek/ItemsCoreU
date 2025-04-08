package me.udnek.itemscoreu.customminigame.event;

import me.udnek.itemscoreu.customevent.CustomEvent;
import me.udnek.itemscoreu.customminigame.game.MGUGameInstance;
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
