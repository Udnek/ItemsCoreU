package me.udnek.itemscoreu.customenchantment;

import me.udnek.itemscoreu.customattribute.CustomAttributeConsumer;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomEnchantment extends Registrable {

    static @Nullable CustomEnchantment get(@NotNull Enchantment enchantment){
        return CustomRegistries.ENCHANTMENT.get(enchantment.getKey().toString());
    }

    static boolean isCustom(@NotNull Enchantment enchantment){
        return get(enchantment) != null;
    }

    @NotNull ItemStack createEnchantedBook(int level);
    void enchant(@NotNull ItemStack itemStack, int level);
    void enchantBook(@NotNull ItemStack itemStack, int level);
    @NotNull Enchantment getBukkit();

    void getCustomAttributes(int level, @NotNull CustomAttributeConsumer consumer);

}
