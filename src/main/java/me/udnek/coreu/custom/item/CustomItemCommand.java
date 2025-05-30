package me.udnek.coreu.custom.item;

import me.udnek.coreu.custom.registry.CustomRegistries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomItemCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) return false;

        if (args.length != 1) return false;

        String id = args[0];
        if (!CustomItem.idExists(id)) return false;

        ItemStack itemStack = CustomItem.get(id).getItem();

        player.getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args.length > 1) return List.of();

        List<String> ids = new ArrayList<>(CustomRegistries.ITEM.getIds());
        ids.removeIf(id -> !id.contains(args[0]));

        return ids;
    }
}
