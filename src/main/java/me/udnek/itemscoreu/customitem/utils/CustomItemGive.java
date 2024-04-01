package me.udnek.itemscoreu.customitem.utils;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomItemGive implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        if (args.length != 1){
            return false;
        }
        String id = args[0];

        if (!CustomItemUtils.isIdExists(id)){
            return false;
        }

        ItemStack itemStack = CustomItemUtils.getFromId(id).getItem();

        ((Player) commandSender).getInventory().addItem(itemStack);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 1) return new ArrayList<>();

        ArrayList<String> allIds = new ArrayList<>(CustomItemUtils.getAllIds());
        ArrayList<String> ids = new ArrayList<>();

        for (String id : allIds) {
            if (id.contains(args[0])){
                ids.add(id);
            }
        }

        return ids;
    }
}
