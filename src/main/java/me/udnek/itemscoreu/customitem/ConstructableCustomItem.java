package me.udnek.itemscoreu.customitem;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customattribute.AttributeUtils;
import me.udnek.itemscoreu.customcomponent.AbstractComponentHolder;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class ConstructableCustomItem extends AbstractComponentHolder<CustomItem> implements CustomItem, CustomItemProperties {
    private JavaPlugin plugin;
    private List<Recipe> recipes;
    private String id;
    protected ItemStack itemStack;

    ///////////////////////////////////////////////////////////////////////////
    // INITIAL
    ///////////////////////////////////////////////////////////////////////////
    protected ConstructableCustomItem(){}

    @Override
    public final @NotNull String getId(){return this.id;}

    @Override
    public void initialize(@NotNull JavaPlugin javaPlugin){
        Preconditions.checkArgument(plugin == null, "Item already initialized!");
        this.plugin = javaPlugin;
        this.id = new NamespacedKey(javaPlugin, getRawId()).asString();
        this.recipes = new ArrayList<>();
    }

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////

    protected void setPersistentData(ItemMeta itemMeta){
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, id);
    }

    protected ItemMeta getMainItemMeta(){
        ItemMeta itemMeta = new ItemStack(this.getMaterial()).getItemMeta();
        this.setPersistentData(itemMeta);

        itemMeta.itemName(this.getItemName());
        itemMeta.lore(this.getLore());
        itemMeta.setRarity(getItemRarity());
        itemMeta.setHideTooltip(getHideTooltip());
        itemMeta.setFood(getFoodComponent());
        itemMeta.setTool(getToolComponent());
        itemMeta.displayName(getCustomDisplayName());
        itemMeta.setUnbreakable(getUnbreakable());
        itemMeta.setEnchantmentGlintOverride(getEnchantmentGlintOverride());
        itemMeta.setCustomModelData(getCustomModelData());
        if (getFireResistant() != null) itemMeta.setFireResistant(getFireResistant());
        itemMeta.setMaxStackSize(getMaxStackSize());
        if (itemMeta instanceof Damageable damageable){
            if (getMaxDamage() != null){
                damageable.setMaxStackSize(1);
                damageable.setMaxDamage(getMaxDamage());
            }
        }
        if (itemMeta instanceof ArmorMeta armorMeta) armorMeta.setTrim(getArmorTrim());
        if (itemMeta instanceof MusicInstrumentMeta instrumentMeta) instrumentMeta.setInstrument(getMusicInstrument());
        if (getAddDefaultAttributes()) AttributeUtils.addDefaultAttributes(itemMeta, getMaterial());
        if (getAttributes() != null){
            for (Map.Entry<Attribute, List<AttributeModifier>> entry : getAttributes().entrySet()) {
                Attribute attribute = entry.getKey();
                for (AttributeModifier modifier : entry.getValue()) {
                    itemMeta.addAttributeModifier(attribute, modifier);
                }
            }
        }
        if (getBlockData() != null && itemMeta instanceof BlockDataMeta blockDataMeta){
            blockDataMeta.setBlockData(getBlockData());
        }
        itemMeta.addItemFlags(getTooltipHides());

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

    @Override
    public ItemStack getItem(){
        if (this.itemStack == null){
            ItemStack itemStack = this.getMainItemStack();
            this.modifyFinalItemStack(itemStack);
            this.itemStack = itemStack;
        }
        return this.itemStack.clone();
    }

    ///////////////////////////////////////////////////////////////////////////
    // RECIPES
    ///////////////////////////////////////////////////////////////////////////

    protected List<Recipe> generateRecipes(){return new ArrayList<>();}
    @Override
    public final void registerRecipes(){
        List<Recipe> recipes = generateRecipes();
        this.recipes = recipes;
        RecipeManager recipeManager = RecipeManager.getInstance();
        for (Recipe recipe : recipes) {
            recipeManager.register(recipe);
        }
    }

    @Override
    public final @NotNull List<Recipe> getRecipes(){
        return this.recipes;
    }

    protected NamespacedKey getRecipeNamespace(int recipeNumber){
        return new NamespacedKey(this.plugin, this.getRawId()+ "_" + recipeNumber);
    }


}
