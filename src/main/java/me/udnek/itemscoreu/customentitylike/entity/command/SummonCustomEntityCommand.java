package me.udnek.itemscoreu.customentitylike.entity.command;

import me.udnek.itemscoreu.customentitylike.entity.CustomEntityType;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SummonCustomEntityCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) return false;
        if (args.length != 1) return false;

        CustomEntityType entityType = CustomEntityType.get(args[0]);
        if (entityType == null) return false;

        entityType.spawn(player.getLocation());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) return new ArrayList<>();
        return new ArrayList<>(CustomRegistries.ENTITY_TYPE.getIds());
    }
}
