package me.udnek.itemscoreu.customattribute;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomAttributeUtils {

    private final CustomAttribute attribute;
    private final Collection<CustomEquipmentSlot> searchTroughSlots;
    private final Entity entity;
    private double amount;

    private CustomAttributeUtils(CustomAttribute attribute, Collection<CustomEquipmentSlot> searchTroughSlots, Entity entity){
        this.attribute = attribute;
        this.searchTroughSlots = searchTroughSlots;
        this.amount = attribute.getDefaultValue();
        this.entity = entity;
    }

    private void calculate(){
        if (!(entity instanceof InventoryHolder inventoryHolder)) return;
        Inventory inventory = inventoryHolder.getInventory();

        double multiplyBase = 1;
        double multiply = 1;

        Set<Integer> allSlots = new IntArraySet();
        for (CustomEquipmentSlot equipmentSlot : searchTroughSlots) {
            equipmentSlot.getAllSlots(entity, allSlots::add);
        }

        for (int slot : allSlots) {
            CustomItem customItem = CustomItem.get(inventory.getItem(slot));
            if (customItem == null) continue;
            CustomAttributesContainer container = customItem.getComponentOrDefault(CustomComponentType.CUSTOM_ITEM_ATTRIBUTES).getAttributes(customItem);
            if (container.isEmpty()) continue;

            for (Map.Entry<CustomAttribute, List<CustomAttributeModifier>> entry : container.getAll().entrySet()) {
                if (entry.getKey() != attribute) continue;

                for (CustomAttributeModifier modifier : entry.getValue()) {
                    if (!modifier.getEquipmentSlot().isAppropriateSlot(entity, slot)) continue;

                    switch (modifier.getOperation()){
                        case ADD_NUMBER: amount += modifier.getAmount(); break;
                        case ADD_SCALAR: multiplyBase += modifier.getAmount(); break;
                        default: multiply *= (1 + modifier.getAmount()); break;
                    }
                }
            }
        }

        amount *= multiplyBase;
        amount *= multiply;

        amount = Math.max(amount, attribute.getMinimum());
        amount = Math.min(amount, attribute.getMaximum());
    }

    public static double calculate(CustomAttribute attribute, Collection<CustomEquipmentSlot> slots, Entity entity){
        CustomAttributeUtils attributeUtils = new CustomAttributeUtils(attribute, slots, entity);
        attributeUtils.calculate();
        return attributeUtils.amount;
    }

    public static double calculate(CustomAttribute attribute, Entity entity){
        return calculate(attribute, CustomRegistries.EQUIPMENT_SLOT.getAll(), entity);
    }
}













