package me.udnek.itemscoreu.customsound;

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

public class CustomSoundCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) return false;

        if (args.length != 1) return false;
        CustomSound customSound = CustomRegistries.SOUND.get(args[0]);
        if (customSound == null) return false;

        customSound.play(player);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length > 1) return List.of();
        List<String> ids = new ArrayList<>(CustomRegistries.SOUND.getIds());
        ids.removeIf(id -> !id.contains(args[0]));
        return ids;
    }
}
