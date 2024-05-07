package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public abstract class CustomItem {
    private JavaPlugin plugin;
    private List<Recipe> recipes;
    private String id;
    protected ItemStack itemStack;

    protected void initialize(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
        this.id = new NamespacedKey(javaPlugin, getItemName()).asString();
        this.recipes = new ArrayList<>();
    }

    protected CustomItem(){}

    public boolean isAllowedInMaterialRecipes(){return false;}
    public abstract Material getMaterial();
    protected abstract String getRawDisplayName();
    public List<Component> getLore(){
        return null;
    }
    protected abstract String getItemName();
    public Component getDisplayName(){
        return Component.translatable(this.getRawDisplayName()).decoration(TextDecoration.ITALIC, false);
    }
    public final String getId(){
        return this.id;
    }

    public final boolean isSameIds(CustomItem customItem){
        return customItem.getId().equals(id);
    }

    protected void setPersistentData(ItemMeta itemMeta){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(CustomItemUtils.namespacedKey, PersistentDataType.STRING, this.getId());
    }

    protected ItemMeta getMainItemMeta(){
        ItemMeta itemMeta = new ItemStack(this.getMaterial()).getItemMeta();
        this.setPersistentData(itemMeta);
        itemMeta.displayName(this.getDisplayName());
        itemMeta.lore(this.getLore());
        return itemMeta;
    }

    protected void modifyFinalItemMeta(ItemMeta itemMeta){}


    protected ItemStack getMainItemStack(){
        ItemMeta itemMeta = this.getMainItemMeta();
        this.modifyFinalItemMeta(itemMeta);
        ItemStack itemStack = new ItemStack(this.getMaterial());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    protected void modifyFinalItemStack(ItemStack itemStack){
    }


    public ItemStack getItem(){
        if (this.itemStack != null){
            return this.itemStack.clone();
        }
        ItemStack itemStack = this.getMainItemStack();
        this.modifyFinalItemStack(itemStack);
        this.itemStack = itemStack;
        return this.itemStack.clone();
    }

    // RECIPES
    protected List<Recipe> generateRecipes(){return new ArrayList<Recipe>();}
    protected final void registerRecipes(){
        List<Recipe> recipes = this.generateRecipes();
        for (Recipe recipe : recipes) {
            Bukkit.addRecipe(recipe);
        }
        this.recipes = recipes;
    }
    protected final void registerAdditionalRecipes(Recipe ...recipes){
        for (Recipe recipe : recipes) {
            Bukkit.addRecipe(recipe);
            this.recipes.add(recipe);
        }
    }
    public List<Recipe> getRecipes(){
        return this.recipes;
    }

    protected NamespacedKey getRecipeNamespace(int n){
        return new NamespacedKey(this.plugin, this.getItemName()+ "_" + Integer.toString(n));
    }
    protected NamespacedKey getRecipeNamespace(){
        return this.getRecipeNamespace(this.recipes.size());
    }

    // EVENTS
    public ItemStack onPrepareCraft(PrepareItemCraftEvent event){
        return this.itemFromMatrix(event.getRecipe().getResult(), event.getInventory().getMatrix(), event.getRecipe());
    }
    public ItemStack itemFromMatrix(ItemStack result, ItemStack[] matrix, Recipe recipe){
        return result;
    }

    public void onConsumes(PlayerItemConsumeEvent event, ItemStack itemStack){}
    public void onThrowableProjectileHits(ProjectileHitEvent event, ItemStack itemStack){}
    public void onShoots(EntityShootBowEvent event, ItemStack itemStack){}
    public void onBeingShot(EntityShootBowEvent event, ItemStack itemStack){}

}
