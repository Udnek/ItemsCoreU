package me.udnek.itemscoreu.customattribute;

import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class AbstractAttributeContainer<Attribute, Modifier, ExactType extends AbstractAttributeContainer<Attribute, Modifier, ?>> {

    protected final HashMap<Attribute, List<Modifier>> attributes = new HashMap<>();

    public @NotNull List<@NotNull Modifier> get(@NotNull Attribute attribute){return new ArrayList<>(attributes.get(attribute));}
    public @NotNull Map<@NotNull Attribute, @NotNull List<@NotNull Modifier>> getAll(){return new HashMap<>(attributes);}
    public abstract @NotNull ExactType get(@NotNull Predicate<@NotNull CustomEquipmentSlot> predicate);
    public @NotNull ExactType get(@NotNull CustomEquipmentSlot targetSlot){
        return get(slot -> slot.test(targetSlot));
    }
    public @NotNull ExactType getExact(@NotNull CustomEquipmentSlot targetSlot){
        return get(slot -> targetSlot == slot);
    }
    public boolean isEmpty(){
        return attributes.isEmpty();
    }
    public boolean contains(Attribute customAttribute){
        return attributes.containsKey(customAttribute);
    }
    protected void add(@NotNull Attribute attribute, @NotNull Modifier modifier){
        List<Modifier> modifiers = attributes.get(attribute);
        if (modifiers == null){
            modifiers = new ArrayList<>();
            attributes.put(attribute, modifiers);
        }
        modifiers.add(modifier);
    }

}
