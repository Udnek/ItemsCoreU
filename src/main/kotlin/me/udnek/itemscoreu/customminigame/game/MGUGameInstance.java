package me.udnek.itemscoreu.customminigame.game;

import me.udnek.itemscoreu.customminigame.MGUId;
import me.udnek.itemscoreu.customminigame.command.MGUCommandContext;
import me.udnek.itemscoreu.customminigame.command.MGUCommandType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MGUGameInstance {

    @NotNull MGUGameType getType();

    @NotNull MGUId getId();

    @NotNull MGUCommandType.ExecutionResult executeCommand(@NotNull MGUCommandContext context);
    @NotNull List<String> getCommandOptions(@NotNull MGUCommandContext context);
    boolean testCommandArgs(@NotNull MGUCommandContext context);
}
