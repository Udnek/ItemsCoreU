package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlots;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class CustomAttributeUtils {

    private final CustomAttribute attribute;
    private final List<CustomEquipmentSlot> slots;
    private final Entity entity;
    private double amount;
    private CustomAttributeUtils(CustomAttribute attribute, List<CustomEquipmentSlot> slots, Entity entity){
        this.attribute = attribute;
        this.slots = slots;
        this.amount = attribute.getDefaultValue();
        this.entity = entity;
    }

    private void calculate(){
        if (!(entity instanceof InventoryHolder inventoryHolder)) return;
        Inventory inventory = inventoryHolder.getInventory();

        double multiplyBase = 1;
        double multiply = 1;

        for (CustomEquipmentSlot equipmentSlot : slots) {
            for (int slot : equipmentSlot.getAllSlots(entity)) {
                CustomItem customItem = CustomItem.get(inventory.getItem(slot));
                if (!(customItem instanceof DefaultCustomAttributeHolder attributeHolder)) continue;
                if (attributeHolder.getDefaultCustomAttributes() == null) continue;

                CustomAttributesContainer container = attributeHolder.getDefaultCustomAttributes();

                for (Map.Entry<CustomAttribute, List<CustomAttributeModifier>> entry : container.getAll().entrySet()) {
                    if (entry.getKey() != attribute) continue;

                    for (CustomAttributeModifier modifier : entry.getValue()) {
                        if (!modifier.getEquipmentSlot().isAppropriateSlot(entity, slot)) continue;

                        if (modifier.getOperation() == AttributeModifier.Operation.ADD_NUMBER) amount += modifier.getAmount();
                        else if (modifier.getOperation() == AttributeModifier.Operation.ADD_SCALAR) multiplyBase += modifier.getAmount();
                        else multiply *= (1 + modifier.getAmount());
                    }
                }
            }
        }
        amount *= multiplyBase;
        amount *= multiply;
    }


    public static double calculate(CustomAttribute attribute, List<CustomEquipmentSlot> slots, Entity entity){
        CustomAttributeUtils attributeUtils = new CustomAttributeUtils(attribute, slots, entity);
        attributeUtils.calculate();
        return attributeUtils.amount;
    }

    public static double calculate(CustomAttribute attribute, Entity entity){
        return calculate(attribute, CustomEquipmentSlots.getAllSlots(), entity);
    }
}













