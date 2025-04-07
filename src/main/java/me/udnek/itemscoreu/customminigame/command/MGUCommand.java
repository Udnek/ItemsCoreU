package me.udnek.itemscoreu.customminigame.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MGUCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MGUCommandType commandType = MGUCommandType.getType(args);
        if (commandType == null) return false;
        if (commandType.playerOnly && !(commandSender instanceof Player)) return false;
        if (!commandType.testArgs(commandSender, args)) return false;

        return commandType.execute(commandSender, args) == MGUCommandType.ExecutionResult.SUCCESS;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1){
            List<String> option = new ArrayList<>();
            for (MGUCommandType value : MGUCommandType.values()) option.add(value.name);
            return option;
        }
        MGUCommandType type = MGUCommandType.getType(args);
        if (type == null) return List.of();
        return type.getOptions(commandSender, args);
    }


}
