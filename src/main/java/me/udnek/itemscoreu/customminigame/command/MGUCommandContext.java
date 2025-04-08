package me.udnek.itemscoreu.customminigame.command;

import me.udnek.itemscoreu.customminigame.MGUManager;
import me.udnek.itemscoreu.customminigame.player.MGUPlayer;
import me.udnek.itemscoreu.customminigame.game.MGUGameInstance;
import me.udnek.itemscoreu.customminigame.game.MGUGameType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MGUCommandContext(
        @NotNull MGUCommandType commandType,
        @NotNull CommandSender sender,
        @NotNull String[] args,
        @Nullable Player player,
        @Nullable MGUPlayer mguPlayer,
        @Nullable MGUGameInstance gameInstance,
        @Nullable MGUGameType gameType)
{
    public MGUCommandContext(
            @NotNull MGUCommandType type,
            @NotNull CommandSender sender,
            @NotNull String[] args,
            @Nullable MGUGameInstance gameInstance,
            @Nullable MGUGameType gameType)
    {
        this(type, sender, args,
                sender instanceof Player ? ((Player) sender) : null,
                sender instanceof Player ? MGUManager.get().getPlayer(((Player) sender)) : null,
                gameInstance, gameType);
    }

    public MGUCommandContext(
            @NotNull MGUCommandType type,
            @NotNull CommandSender sender,
            @NotNull String[] args
    ){
        this(type, sender, args, null, null);
    }
}
