package me.udnek.itemscoreu.customitem;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface CustomItemProperties {
    @NotNull Material getMaterial();

    // OPTIONAL
    @Nullable default String getRawItemName(){return null;}
    @Nullable default Component getItemName(){
        if (getRawItemName() == null) return null;
        return Component.translatable(getRawItemName());
    }
    @Nullable default Component getCustomDisplayName(){return null;}
    @Nullable default List<Component> getLore(){return null;}
    @Nullable default ItemRarity getItemRarity(){return null;}
    @Nullable default Integer getCustomModelData(){return null;}
    default boolean getHideTooltip(){return false;}
    @Nullable default FoodComponent getFoodComponent(){return null;}
    @Nullable default ToolComponent getToolComponent(){return null;}
    @Nullable default ItemFlag[] getTooltipHides(){return new ItemFlag[0];}
    @Nullable default Integer getMaxStackSize(){return null;}
    @Nullable default Integer getMaxDamage(){return null;}
    @Nullable default boolean getUnbreakable(){return false;}
    @Nullable default Boolean getFireResistant(){return null;}
    @Nullable default Boolean getEnchantmentGlintOverride(){return null;}
    @Nullable default ArmorTrim getArmorTrim(){return null;}
    @Nullable default MusicInstrument getMusicInstrument(){return null;}
    default boolean getAddDefaultAttributes(){return false;}
    @Nullable default Map<Attribute, List<AttributeModifier>> getAttributes(){return null;}
    @Nullable default BlockData getBlockData(){return null;}

}
