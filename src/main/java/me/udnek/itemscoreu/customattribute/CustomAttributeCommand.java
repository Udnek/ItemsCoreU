package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customregistry.CustomRegistries;
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
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof LivingEntity living)) return false;
        if (args.length == 0 || args[0].equals("all")){
            CustomRegistries.ATTRIBUTE.getAll(customAttribute ->
                    commandSender.sendMessage(customAttribute.getId() +": "+  customAttribute.calculate(living)));
        } else {
            CustomAttribute attribute = CustomRegistries.ATTRIBUTE.get(args[0]);
            if (attribute == null) return false;
            commandSender.sendMessage(attribute.getId() +": "+  attribute.calculate(living));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> ids = new ArrayList<>(CustomRegistries.ATTRIBUTE.getIds());
        if (args.length > 0) ids.removeIf(id -> !id.contains(args[0]));
        ids.add("all");
        return ids;
    }
}
