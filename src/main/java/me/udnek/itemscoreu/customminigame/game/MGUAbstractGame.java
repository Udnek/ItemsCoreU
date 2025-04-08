package me.udnek.itemscoreu.customminigame.game;

import me.udnek.itemscoreu.customminigame.MGUId;
import me.udnek.itemscoreu.customminigame.player.MGUPlayer;
import me.udnek.itemscoreu.customminigame.command.MGUCommandContext;
import me.udnek.itemscoreu.customminigame.command.MGUCommandType;
import net.kyori.adventure.text.Component;
import net.minecraft.stats.Stat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MGUAbstractGame implements MGUGameInstance{

    private final MGUId id;
    protected @NotNull State state = State.WAITING;

    public MGUAbstractGame(){
        this.id = MGUId.generateNew(this);
    }

    @Override
    public final @NotNull MGUId getId() {return id;}

    @Override
    public @NotNull MGUCommandType.ExecutionResult executeCommand(@NotNull MGUCommandContext context) {
        return switch (context.commandType()) {
            case START -> {
                if (getState() != State.WAITING) yield MGUCommandType.ExecutionResult.FAILED;
                yield start(context);
            }
            case STOP -> {
                if (getState() != State.RUNNING) yield MGUCommandType.ExecutionResult.FAILED;
                yield stop(context);
            }
            case JOIN -> join(Objects.requireNonNull(context.player()), context);
            case LEAVE -> leave(Objects.requireNonNull(context.mguPlayer()), context);
            case DEBUG -> {
                getDebug().forEach(component -> context.sender().sendMessage(component));
                yield MGUCommandType.ExecutionResult.SUCCESS;
            }
        };
    }


    @Override
    public @NotNull List<String> getCommandOptions(@NotNull MGUCommandContext context) {
        return List.of();
    }

    @Override
    public boolean testCommandArgs(@NotNull MGUCommandContext context) {
        return true;
    }

    public @NotNull List<Component> getDebug(){
        List<Component> list = new ArrayList<>();
        list.add(Component.text("Game debug data: " + getId()));
        list.add(Component.text("state: " + getState()));
        list.add(Component.text("players: " + getPlayers()));
        return list;
    }

    public @NotNull State getState(){
        return State.WAITING;
    }

    public abstract @NotNull List<MGUPlayer> getPlayers();
    public abstract @NotNull MGUCommandType.ExecutionResult start(@NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult stop(@NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult join(@NotNull Player player, @NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult leave(@NotNull MGUPlayer player, @NotNull MGUCommandContext context);

    public enum State{
        WAITING,
        RUNNING
    }
}







