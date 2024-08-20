package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VanillaBasedCustomItem implements CustomItem{

    protected final Material material;
    protected String id;
    public VanillaBasedCustomItem(@NotNull Material material){
        Preconditions.checkArgument(material != null, "Material can not be null!");
        this.material = material;
    }

    @Override
    public @NotNull String getRawId() {return material.name().toLowerCase();}
    @Override
    public @NotNull String getId() {return id;}
    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(itemMeta -> itemMeta.lore(List.of(Component.text("REPLACED"))));
        return itemStack;
    }
    public ItemStack getFrom(@NotNull ItemStack itemStack){
        // TODO: 8/19/2024 MAKE ITEM MODIFICATORS
        Preconditions.checkArgument(itemStack.getType() == material, "Can not create from different material!");
        return getItem();
    }
    @Override
    public @NotNull List<Recipe> getRecipes() {return List.of();}
    @Override
    public void registerRecipes() {}
    @Override
    public void initialize(@NotNull JavaPlugin plugin) {
        Preconditions.checkArgument(id == null, "Item already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }
}
