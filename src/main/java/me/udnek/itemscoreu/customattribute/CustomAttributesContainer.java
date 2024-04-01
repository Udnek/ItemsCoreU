package me.udnek.itemscoreu.customattribute;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomAttributesContainer implements Serializable {

    private HashMap<CustomAttributeType, CustomAttributeModifiersContainer> attributes = new HashMap<>();

    public CustomAttributesContainer(){}

    public void add(CustomAttributeType customAttributeType, CustomAttributeModifiersContainer toAddContainer){
        CustomAttributeModifiersContainer container = get(customAttributeType);
        if (container == null) {
            attributes.put(customAttributeType, toAddContainer);
            return;
        }
        container.add(toAddContainer);
    }

    public void add(CustomAttributeType customAttributeType, CustomAttributeModifier customAttributeModifier){
        CustomAttributeModifiersContainer toAddContainer = new CustomAttributeModifiersContainer();
        toAddContainer.add(customAttributeModifier);
        add(customAttributeType, toAddContainer);
    }

    public boolean has(CustomAttributeType customAttributeType){
        return get(customAttributeType) != null;
    }

    public CustomAttributeModifiersContainer get(CustomAttributeType customAttributeType){
        NamespacedKey namespacedKey = customAttributeType.getNamespacedKey();
        for (Map.Entry<CustomAttributeType, CustomAttributeModifiersContainer> entry : attributes.entrySet()) {
            if (entry.getKey().getNamespacedKey().equals(namespacedKey)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static CustomAttributesContainer defaultFrom(CustomAttributeType customAttributeType, double value, EquipmentSlot equipmentSlot, AttributeModifier.Operation operation){
        CustomAttributesContainer container = new CustomAttributesContainer();
        container.add(customAttributeType, new CustomAttributeModifier(value, equipmentSlot, operation));
        return container;
    }
}
