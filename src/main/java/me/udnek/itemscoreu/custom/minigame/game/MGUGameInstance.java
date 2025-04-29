package me.udnek.itemscoreu.custom.minigame.game;

import me.udnek.itemscoreu.custom.minigame.MGUId;
import me.udnek.itemscoreu.custom.minigame.command.MGUCommandContext;
import me.udnek.itemscoreu.custom.minigame.command.MGUCommandType;
import me.udnek.itemscoreu.custom.minigame.map.MGUMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MGUGameInstance {

    @NotNull MGUGameType getType();

    @NotNull MGUMap getMap();

    @NotNull MGUId getId();

    boolean isRunning();

    @NotNull MGUCommandType.ExecutionResult executeCommand(@NotNull MGUCommandContext context);
    @NotNull List<String> getCommandOptions(@NotNull MGUCommandContext context);
    boolean testCommandArgs(@NotNull MGUCommandContext context);
}
