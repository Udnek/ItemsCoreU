package me.udnek.itemscoreu.customitem;

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
import java.util.Set;

public class CustomItemCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        if (args.length != 1){
            return false;
        }
        String id = args[0];

        if (!CustomItem.IdExists(id)){
            return false;
        }

        ItemStack itemStack = CustomItem.get(id).getItem();

        ((Player) commandSender).getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 1) return new ArrayList<>();

        Set<String> allIds = CustomItem.getAllIds();
        ArrayList<String> ids = new ArrayList<>();

        for (String id : allIds) {
            if (id.contains(args[0])){
                ids.add(id);
            }
        }

        return ids;
    }
}
