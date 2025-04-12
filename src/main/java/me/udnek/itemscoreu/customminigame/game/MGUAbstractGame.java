package me.udnek.itemscoreu.customminigame.game;

import me.udnek.itemscoreu.customminigame.MGUId;
import me.udnek.itemscoreu.customminigame.player.MGUPlayer;
import me.udnek.itemscoreu.customminigame.command.MGUCommandContext;
import me.udnek.itemscoreu.customminigame.command.MGUCommandType;
import net.kyori.adventure.text.Component;
import net.minecraft.stats.Stat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MGUAbstractGame implements MGUGameInstance{

    private final MGUId id = MGUId.generateNew(this);
    protected boolean isRunning = false;

    @Override
    public final @NotNull MGUId getId() {return id;}

    @Override
    public @NotNull MGUCommandType.ExecutionResult executeCommand(@NotNull MGUCommandContext context) {
        return switch (context.commandType()) {
            case START -> {
                if (isRunning()) yield new MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "game is running");
                yield start(context);
            }
            case STOP -> {
                if (!isRunning()) yield new MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "game is not running");
                yield stop(context);
            }
            case JOIN -> {
                if (isRunning()) yield new MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "game is running");
                yield join(Objects.requireNonNull(context.player()), context);
            }
            case LEAVE -> leave(Objects.requireNonNull(context.mguPlayer()), context);
            case DEBUG -> {
                getDebug().forEach(component -> context.sender().sendMessage(component));
                yield MGUCommandType.ExecutionResult.SUCCESS;
            }
            case COORDINATE_WAND -> {
                Objects.requireNonNull(context.player()).getInventory().addItem(createCoordinateWand());
                yield MGUCommandType.ExecutionResult.SUCCESS;
            }
            default -> MGUCommandType.ExecutionResult.SUCCESS;
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

    public @NotNull ItemStack createCoordinateWand(){
        // TODO COORDINATE WAND
        return new ItemStack(Material.STICK);
    }


    public @NotNull List<Component> getDebug(){
        List<Component> list = new ArrayList<>();
        list.add(Component.text("Game debug data: " + getId()));
        list.add(Component.text("isRunning: " + isRunning));
        list.add(Component.text("players: " + getPlayers()));
        return list;
    }

    @Override
    public boolean isRunning() {return isRunning;}


    public abstract @NotNull List<MGUPlayer> getPlayers();
    public abstract @NotNull MGUCommandType.ExecutionResult start(@NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult stop(@NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult join(@NotNull Player player, @NotNull MGUCommandContext context);
    public abstract @NotNull MGUCommandType.ExecutionResult leave(@NotNull MGUPlayer player, @NotNull MGUCommandContext context);
}







