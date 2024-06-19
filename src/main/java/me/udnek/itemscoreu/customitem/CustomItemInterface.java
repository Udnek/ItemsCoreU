package me.udnek.itemscoreu.customitem;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import java.util.List;
import java.util.Map;

public interface CustomItemInterface {
    boolean isThisItem(ItemStack itemStack);

    ///////////////////////////////////////////////////////////////////////////
    // UTILS
    ///////////////////////////////////////////////////////////////////////////
    boolean isSameId(CustomItem customItem);
    boolean isSameId(ItemStack itemStack);

    ///////////////////////////////////////////////////////////////////////////
    // INITIAL
    ///////////////////////////////////////////////////////////////////////////
    String getId();

    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////
    String getRawId();
    Material getMaterial();
    String getRawItemName();

    // OPTIONAL
    default Component getItemName(){return Component.translatable(this.getRawItemName());}
    default Component getCustomDisplayName(){return null;}
    default List<Component> getLore(){return null;}
    default ItemRarity getItemRarity(){return null;}
    default Integer getCustomModelData(){return null;}
    default boolean getHideTooltip(){return false;}
    default FoodComponent getFoodComponent(){return null;}
    default ItemFlag[] getTooltipHides(){return new ItemFlag[0];}
    default Integer getMaxStackSize(){return null;}
    default Integer getMaxDamage(){return null;}
    default boolean getUnbreakable(){return false;}
    default Boolean getFireResistant(){return null;}
    default Boolean getEnchantmentGlintOverride(){return null;}
    default ArmorTrim getArmorTrim(){return null;}
    default MusicInstrument getMusicInstrument(){return null;}
    default boolean getAddDefaultAttributes(){return false;}
    default Map<Attribute, List<AttributeModifier>> getAttributes(){return null;}
    default BlockData getBlockData(){return null;}

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////
    ItemStack getItem();

    ///////////////////////////////////////////////////////////////////////////
    // RECIPES
    ///////////////////////////////////////////////////////////////////////////

    List<Recipe> getRecipes();


    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////
    ItemStack onPrepareCraft(PrepareItemCraftEvent event);
    ItemStack getItemFromCraftingMatrix(ItemStack result, ItemStack[] matrix, Recipe recipe);

}
