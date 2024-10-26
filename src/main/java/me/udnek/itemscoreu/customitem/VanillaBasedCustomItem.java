package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customcomponent.AbstractComponentHolder;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.util.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VanillaBasedCustomItem extends AbstractComponentHolder<CustomItem> implements CustomItem{

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
    public @NotNull ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(itemMeta -> itemMeta.lore(List.of(Component.text("REPLACED"))));
        CustomItemGeneratedEvent event = new CustomItemGeneratedEvent(this, itemStack, null);
        event.callEvent();
        event.getLoreBuilder().buildAndApply(event.getItemStack());
        return event.getItemStack();
    }
    public ItemStack getFrom(@NotNull ItemStack itemStack){
        // TODO: 8/19/2024 MAKE ITEM MODIFICATORS
        Preconditions.checkArgument(itemStack.getType() == material, "Can not create from different material!");
        return getItem();
    }

    @Override
    public @NotNull NamespacedKey getNewRecipeKey() {
        List<Recipe> recipes = new ArrayList<>();
        getRecipes(recipes::add);
        return NamespacedKey.fromString(getId() + "_" + recipes.size());
    }

    @Override
    public void getRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        RecipeManager.getInstance().getRecipesAsResult(this.getItem(), consumer);
    }

    @Override
    public void registerRecipe(@NotNull Recipe recipe) {
        RecipeManager.getInstance().register(recipe);
    }

    @Override
    public void initialize(@NotNull Plugin plugin) {
        Preconditions.checkArgument(id == null, "Item already initialized!");
        id = new NamespacedKey(plugin, getRawId()).asString();
    }
}
