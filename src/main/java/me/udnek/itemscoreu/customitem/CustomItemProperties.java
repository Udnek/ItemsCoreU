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
import org.bukkit.inventory.meta.trim.ArmorTrim;

import java.util.List;
import java.util.Map;

public interface CustomItemProperties {
    Material getMaterial();

    // OPTIONAL
    default String getRawItemName(){return null;};
    default Component getItemName(){
        if (getRawItemName() == null) return null;
        return Component.translatable(getRawItemName());
    }
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

}
