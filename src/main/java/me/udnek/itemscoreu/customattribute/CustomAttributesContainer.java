package me.udnek.itemscoreu.customattribute;

import org.apache.maven.model.Build;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomAttributesContainer{

    private final HashMap<CustomAttribute, AttributeModifier> attributes = new HashMap<>();

    private CustomAttributesContainer(){}

    public static CustomAttributesContainer empty(){
        return new CustomAttributesContainer();
    }
    public AttributeModifier get(CustomAttribute customAttribute){
        return attributes.get(customAttribute);
    }

    public boolean contains(CustomAttribute customAttribute){
        return attributes.containsKey(customAttribute);
    }

    private void set(CustomAttribute customAttribute, AttributeModifier attributeModifier){
        attributes.put(customAttribute, attributeModifier);
    }

    public static class Builder{

        private CustomAttributesContainer container;
        public Builder(){
            container = new CustomAttributesContainer();
        }
        public Builder set(CustomAttribute customAttribute, UUID uuid, double amount, AttributeModifier.Operation operation, EquipmentSlot slot){
            AttributeModifier attributeModifier = new AttributeModifier(uuid, "CustomAttributesContainer", amount, operation, slot);
            return set(customAttribute, attributeModifier);
        }

        public Builder set(CustomAttribute customAttribute, AttributeModifier attributeModifier){
            container.set(customAttribute, attributeModifier);
            return this;
        }

        public CustomAttributesContainer build(){
            return container;
        }

    }

}
