package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customequipmentslot.SingleSlot;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAttributeUtils {

    private final CustomAttribute attribute;
    private final Collection<CustomEquipmentSlot> searchTroughSlots;
    private final LivingEntity entity;
    private double amount;

    private double multiplyBase = 1;
    private double multiply = 1;

    public CustomAttributeUtils(@NotNull CustomAttribute attribute, @NotNull Collection<CustomEquipmentSlot> searchTroughSlots, @NotNull LivingEntity entity, double base){
        this.attribute = attribute;
        this.searchTroughSlots = searchTroughSlots;
        this.entity = entity;
        this.amount = base;
    }
    protected void calculate(){

        Map<@NotNull Integer, @NotNull ItemStack> slots = new HashMap<>();

        if (entity instanceof InventoryHolder inventoryHolder){
            Inventory inventory = inventoryHolder.getInventory();
            for (CustomEquipmentSlot equipmentSlot : searchTroughSlots) {
                equipmentSlot.getAllSlots(entity, integer ->
                {
                    ItemStack item = inventory.getItem(integer);
                    if (item == null) return;
                    slots.put(integer, item);
                });
            }
        } else if (entity instanceof Mob mob){
            EntityEquipment equipment = mob.getEquipment();
            for (CustomEquipmentSlot equipmentSlot : searchTroughSlots) {
                if (!(equipmentSlot instanceof SingleSlot singleSlot)) continue;
                EquipmentSlot vanillaSlot = singleSlot.getVanillaSlot();
                if (vanillaSlot == null) continue;
                ItemStack item = equipment.getItem(vanillaSlot);
                Integer slot = singleSlot.getSlot(entity);
                if (slot == null || item.getType() == Material.AIR) continue;
                slots.put(slot, item);
                // TODO REPLACE WHEN API IS READY
            }
        }


        for (Map.Entry<@NotNull Integer, @NotNull ItemStack> slotEntry : slots.entrySet()) {
            CustomItem customItem = CustomItem.get(slotEntry.getValue());
            if (customItem == null) continue;
            CustomAttributesContainer container = customItem.getComponentOrDefault(CustomComponentType.CUSTOM_ITEM_ATTRIBUTES).getAttributes(customItem);
            if (container.isEmpty()) continue;

            for (Map.Entry<CustomAttribute, List<CustomAttributeModifier>> entry : container.getAll().entrySet()) {
                if (entry.getKey() != attribute) continue;

                for (CustomAttributeModifier modifier : entry.getValue()) {
                    if (!modifier.getEquipmentSlot().isAppropriateSlot(entity, slotEntry.getKey())) continue;

                    switch (modifier.getOperation()){
                        case ADD_NUMBER -> amount += modifier.getAmount();
                        case ADD_SCALAR -> multiplyBase += modifier.getAmount();
                        default -> multiply *= (1 + modifier.getAmount());
                    }
                }
            }
        }

        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
            CustomEffect custom = CustomEffect.get(potionEffect.getType());
            if (custom == null) return;
            custom.getCustomAttributes(potionEffect, (thisAttr, thisAmount, operation) -> {
                if (thisAttr != attribute) return;
                switch (operation){
                    case ADD_NUMBER -> amount += thisAmount;
                    case ADD_SCALAR -> multiplyBase += thisAmount;
                    default -> multiply *= (1 + thisAmount);
                }
            });
        }


        amount *= multiplyBase;
        amount *= multiply;

        amount = Math.clamp(amount, attribute.getMinimum(), attribute.getMaximum());
    }

    public static double calculate(@NotNull CustomAttribute attribute, @NotNull Collection<CustomEquipmentSlot> slots, @NotNull LivingEntity entity, double base){
        CustomAttributeUtils attributeUtils = new CustomAttributeUtils(attribute, slots, entity, base);
        attributeUtils.calculate();
        return attributeUtils.amount;
    }

    public static double calculate(@NotNull CustomAttribute attribute, @NotNull LivingEntity entity, double base){
        return calculate(attribute, CustomRegistries.EQUIPMENT_SLOT.getAll(), entity, base);
    }
}













