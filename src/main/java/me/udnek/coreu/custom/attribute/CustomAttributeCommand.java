package me.udnek.coreu.custom.attribute;

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

public class CustomAttributeCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof LivingEntity living)) return false;
        if (args.length == 0 || args[0].equals("all")){
            for (CustomAttribute attribute : CustomRegistries.ATTRIBUTE.getAll()) {
                commandSender.sendMessage(attribute.getId() +": "+ attribute.calculate(living));
            }
        } else {
            CustomAttribute attribute = CustomRegistries.ATTRIBUTE.get(args[0]);
            if (attribute == null) return false;
            commandSender.sendMessage(attribute.getId() +": "+  attribute.calculate(living));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> ids = new ArrayList<>(CustomRegistries.ATTRIBUTE.getIds());
        if (args.length > 0) ids.removeIf(id -> !id.contains(args[0]));
        ids.add("all");
        return ids;
    }
}
