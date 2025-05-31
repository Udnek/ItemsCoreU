package me.udnek.coreu.custom.entitylike.entity.command;

import me.udnek.coreu.custom.entitylike.entity.CustomEntityType;
import me.udnek.coreu.custom.registry.CustomRegistries;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SummonCustomEntityCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) return false;
        CustomEntityType entityType = CustomEntityType.get(args[0]);
        if (entityType == null) return false;

        Location location;
        if (commandSender instanceof Player player)
            location = player.getLocation();
        else if (commandSender instanceof BlockCommandSender blockCommandSender)
            location = blockCommandSender.getBlock().getLocation().toCenterLocation().add(0, 0.5, 0);
        else return false;

        entityType.spawn(location);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 1) return List.of();
        return new ArrayList<>(CustomRegistries.ENTITY_TYPE.getIds());
    }
}
