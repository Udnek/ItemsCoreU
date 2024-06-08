package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customattribute.equipmentslot.CustomEquipmentSlot;
import org.apache.maven.model.Build;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomAttributesContainer{

    private final HashMap<CustomAttribute, CustomAttributeModifier> attributes = new HashMap<>();

    private CustomAttributesContainer(){}

    public static CustomAttributesContainer empty(){
        return new CustomAttributesContainer();
    }
    public CustomAttributeModifier get(CustomAttribute customAttribute){
        return attributes.get(customAttribute);
    }
    public HashMap<CustomAttribute, CustomAttributeModifier> getAll(){return new HashMap<>(attributes);}
    public CustomAttributesContainer getBySlot(CustomEquipmentSlot slot){
        CustomAttributesContainer newContainer = new CustomAttributesContainer();
        for (Map.Entry<CustomAttribute, CustomAttributeModifier> entry : attributes.entrySet()) {
            CustomAttribute attribute = entry.getKey();
            CustomAttributeModifier modifier = entry.getValue();
            if (modifier.getEquipmentSlot() != slot) continue;
            newContainer.set(attribute, modifier);
        }
        return newContainer;
    }
    public boolean contains(CustomAttribute customAttribute){
        return attributes.containsKey(customAttribute);
    }
    private void set(CustomAttribute customAttribute, CustomAttributeModifier attributeModifier){
        attributes.put(customAttribute, attributeModifier);
    }

    public static class Builder{

        private CustomAttributesContainer container;
        public Builder(){
            container = new CustomAttributesContainer();
        }
        public Builder set(CustomAttribute customAttribute, double amount, AttributeModifier.Operation operation, CustomEquipmentSlot slot){
            CustomAttributeModifier attributeModifier = new CustomAttributeModifier(amount, operation, slot);
            return set(customAttribute, attributeModifier);
        }

        public Builder set(CustomAttribute customAttribute, CustomAttributeModifier attributeModifier){
            container.set(customAttribute, attributeModifier);
            return this;
        }

        public CustomAttributesContainer build(){
            return container;
        }

    }

}
