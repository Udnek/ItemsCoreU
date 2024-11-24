package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customcomponent.OptimizedComponentHolder;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class VanillaBasedCustomItem extends OptimizedComponentHolder<CustomItem> implements CustomItem{

    protected final Material material;
    private String id;

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
    public @NotNull ItemStack getFrom(@NotNull ItemStack original){
        // TODO: 8/19/2024 MAKE ITEM MODIFICATORS
        Preconditions.checkArgument(original.getType() == material, "Can not create from different material!");
        ItemStack newItem = getItem();
        newItem.setAmount(original.getAmount());
        newItem.editMeta(itemMeta -> {
            for (Map.Entry<Enchantment, Integer> entry : original.getEnchantments().entrySet()) {
                itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        });
        return newItem;
    }

    @Override
    public @NotNull NamespacedKey getNewRecipeKey() {
        List<Recipe> recipes = new ArrayList<>();
        getRecipes(recipes::add);
        return NamespacedKey.fromString(getId() + "_" + recipes.size());
    }

    @Override
    public void setCooldown(@NotNull Player player, int ticks) {
        player.setCooldown(material, ticks);
    }

    @Override
    public int getCooldown(@NotNull Player player) {return player.getCooldown(getItem());}

    @Override
    public boolean isTagged(@NotNull Tag<Material> tag) {return tag.isTagged(material);}

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
