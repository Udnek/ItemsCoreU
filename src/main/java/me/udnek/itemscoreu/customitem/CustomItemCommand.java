package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import net.minecraft.core.registries.Registries;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        if (!CustomItem.idExists(id)){
            return false;
        }

        ItemStack itemStack = CustomItem.get(id).getItem();

        ((Player) commandSender).getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 1) return new ArrayList<>();

        List<String> ids = new ArrayList<>(CustomRegistries.ITEM.getIds());
        ids.removeIf(id -> !id.contains(args[0]));

        return ids;
    }
}
