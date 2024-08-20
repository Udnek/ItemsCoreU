package me.udnek.itemscoreu.customattribute;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class AttributeUtils {
    public static void addDefaultAttributes(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        addDefaultAttributes(itemMeta, itemStack.getType());
        itemStack.setItemMeta(itemMeta);
    }
    public static void addDefaultAttributes(ItemMeta itemMeta, Material material){
        Multimap<Attribute, AttributeModifier> attributeModifiers = material.getDefaultAttributeModifiers();
        itemMeta.setAttributeModifiers(attributeModifiers);
    }

    public static void addAttribute(ItemMeta itemMeta, Attribute attribute, NamespacedKey id, double amount, AttributeModifier.Operation operation, @NotNull EquipmentSlotGroup slot){
        itemMeta.addAttributeModifier(attribute, new AttributeModifier(id, amount, operation, slot));
    }

    public static void appendAttribute(ItemStack itemStack, Attribute attribute, NamespacedKey id, double amount, AttributeModifier.Operation operation, @NotNull EquipmentSlotGroup slot){
        ItemMeta itemMeta = itemStack.getItemMeta();
        appendAttribute(itemMeta, attribute, id, amount, operation, slot);
        itemStack.setItemMeta(itemMeta);
    }

    public static void appendAttribute(ItemMeta itemMeta, Attribute attribute, NamespacedKey id,  double amount, AttributeModifier.Operation operation, @NotNull EquipmentSlotGroup slot) {
        if (!itemMeta.hasAttributeModifiers()) {
            addAttribute(itemMeta, attribute, id, amount, operation, slot);
            return;
        }
        Collection<AttributeModifier> attributeModifiers = itemMeta.getAttributeModifiers(attribute);
        if (attributeModifiers == null || attributeModifiers.isEmpty()){
            addAttribute(itemMeta, attribute, id, amount, operation, slot);
            return;
        }

        ArrayListMultimap<Attribute, AttributeModifier> newAttributeMap = ArrayListMultimap.create();
        boolean wasAdded = false;
        for (Map.Entry<Attribute, AttributeModifier> entry : itemMeta.getAttributeModifiers().entries()) {
            Attribute thisAttribute = entry.getKey();
            AttributeModifier thisModifier = entry.getValue();

            if (
                    !wasAdded &&
                    thisAttribute == attribute &&
                    thisModifier.getSlotGroup() == slot &&
                    thisModifier.getOperation() == operation
            )
            {
                AttributeModifier newAttributeModifier = new AttributeModifier(
                        thisModifier.getKey(),
                        amount + thisModifier.getAmount(),
                        operation,
                        slot);
                newAttributeMap.put(thisAttribute, newAttributeModifier);
                wasAdded = true;
            }
            else newAttributeMap.put(thisAttribute, thisModifier);
        }
        if (!wasAdded) newAttributeMap.put(
                attribute,
                new AttributeModifier(id, amount, operation, slot));
        itemMeta.setAttributeModifiers(newAttributeMap);
    }

    public static Multimap<Attribute, AttributeModifier> getAttributesBySlot(Multimap<Attribute, AttributeModifier> attributes, EquipmentSlotGroup slot){
        ArrayListMultimap<Attribute, AttributeModifier> newAttributes = ArrayListMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
            AttributeModifier modifier = entry.getValue();
            if (modifier.getSlotGroup() != slot) continue;
            newAttributes.put(entry.getKey(), modifier);
        }
        return newAttributes;
    }

}
