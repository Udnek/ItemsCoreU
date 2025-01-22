package me.udnek.itemscoreu.customentitylike.entity.command;


import me.udnek.itemscoreu.customentitylike.entity.CustomEntity;
import me.udnek.itemscoreu.customentitylike.entity.CustomEntityManager;
import me.udnek.itemscoreu.util.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoadedCustomEntitiesCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage("Loaded entities:");
        for (CustomEntity customEntity : CustomEntityManager.getInstance().getAllLoaded()) {
            Location location = customEntity.getReal().getLocation();
            commandSender.sendMessage(customEntity.getReal().getType() +" " +customEntity.getType().getId() + " " +
                    Utils.roundToTwoDigits(location.x()) + " " + Utils.roundToTwoDigits(location.y()) + Utils.roundToTwoDigits(location.z()) + " " + location.getWorld().getName());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
