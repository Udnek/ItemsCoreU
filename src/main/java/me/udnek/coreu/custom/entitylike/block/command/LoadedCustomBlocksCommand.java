package me.udnek.coreu.custom.entitylike.block.command;

import me.udnek.coreu.custom.entitylike.block.CustomBlockEntity;
import me.udnek.coreu.custom.entitylike.block.CustomBlockManager;
import me.udnek.coreu.util.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoadedCustomBlocksCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage("Loaded blocks:");
        for (CustomBlockEntity block : CustomBlockManager.getInstance().getAllLoaded()) {
            Location location = block.getReal().getLocation();
            commandSender.sendMessage(block +" " +block.getType().getId() + " " +
                    Utils.roundToTwoDigits(location.x()) + " " + Utils.roundToTwoDigits(location.y()) + Utils.roundToTwoDigits(location.z()) + " " + location.getWorld().getName());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
