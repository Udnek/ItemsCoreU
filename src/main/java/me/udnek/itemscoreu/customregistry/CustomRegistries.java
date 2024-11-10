package me.udnek.itemscoreu.customregistry;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customentity.CustomEntityType;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomRegistries {

    private static final List<CustomRegistry<?>> registries = new ArrayList<>();

    public static final CustomRegistry<CustomItem> ITEM = addRegistry(new MappedCustomRegistry<>("Item"));
    public static final CustomRegistry<CustomBlock> BLOCK = addRegistry(new MappedCustomRegistry<>("Block"));
    public static final CustomRegistry<CustomAttribute> ATTRIBUTE = addRegistry(new MappedCustomRegistry<>("Attribute"));
    public static final CustomRegistry<CustomEquipmentSlot> EQUIPMENT_SLOT = addRegistry(new MappedCustomRegistry<>("EquipmentSlot"));
    public static final CustomRegistry<CustomComponentType<?, ?>> COMPONENT_TYPE = addRegistry(new MappedCustomRegistry<>("ComponentType"));
    public static final CustomRegistry<CustomEntityType<?>> ENTITY_TYPE = addRegistry(new MappedCustomRegistry<>("EntityType"));
    public static final LootTableRegistry LOOT_TABLE = (LootTableRegistry) addRegistry(new LootTableRegistry());
    public static final CustomRegistry<CustomEffect> EFFECT = addRegistry(new MappedCustomRegistry<>("Effect"));

    private static <T extends Registrable> CustomRegistry<T> addRegistry(@NotNull CustomRegistry<T> registry){
        registries.add(registry);
        return registry;
    }

    public static @NotNull List<@NotNull CustomRegistry<?>> getRegistries() {
        return new ArrayList<>(registries);
    }
}
