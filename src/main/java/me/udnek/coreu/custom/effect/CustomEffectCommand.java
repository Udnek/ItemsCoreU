package me.udnek.coreu.custom.effect;

import me.udnek.coreu.custom.registry.CustomRegistries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomEffectCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof LivingEntity living)) return false;
        if (args.length < 1) return false;
        CustomEffect customEffect = CustomRegistries.EFFECT.get(args[0]);
        if (customEffect == null) return false;
        int duration = 10*20;
        if (args.length > 1) duration = Integer.parseInt(args[1]);
        int lvl = 0;
        if (args.length > 2) lvl = Integer.parseInt(args[2]);
        customEffect.apply(living, duration, lvl);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length > 2) return List.of("level");
        if (args.length > 1) return List.of("duration");
        List<String> ids = new ArrayList<>(CustomRegistries.EFFECT.getIds());
        if (args.length > 0) ids.removeIf(id -> !id.contains(args[0]));
        return ids;
    }
}
