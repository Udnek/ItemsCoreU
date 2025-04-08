package me.udnek.itemscoreu.customregistry;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customenchantment.CustomEnchantment;
import me.udnek.itemscoreu.customentitylike.block.CustomBlockType;
import me.udnek.itemscoreu.customentitylike.entity.CustomEntityType;
import me.udnek.itemscoreu.customequipmentslot.slot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableRegistry;
import me.udnek.itemscoreu.customsound.CustomSound;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CustomRegistries {


    public static final CustomRegistry<CustomRegistry<?>> REGISTRY;
    public static final CustomRegistry<CustomItem> ITEM;
    public static final CustomRegistry<CustomBlockType> BLOCK_TYPE;
    public static final CustomRegistry<CustomAttribute> ATTRIBUTE;
    public static final CustomRegistry<CustomEquipmentSlot> EQUIPMENT_SLOT;
    public static final CustomRegistry<CustomComponentType<?, ?>> COMPONENT_TYPE;
    public static final CustomRegistry<CustomEntityType> ENTITY_TYPE;
    public static final LootTableRegistry LOOT_TABLE;
    public static final CustomRegistry<CustomEffect> EFFECT;
    public static final CustomRegistry<CustomEnchantment> ENCHANTMENT;
    public static final CustomRegistry<CustomSound> SOUND;

    static {
        REGISTRY = new MappedCustomRegistry<>("registry");
        addRegistry(REGISTRY);

        COMPONENT_TYPE = addRegistry(new MappedCustomRegistry<>("component_type"));
        ENCHANTMENT = addRegistry(new MappedCustomRegistry<>("enchantment"));
        ITEM = addRegistry(new MappedCustomRegistry<>("item"));
        BLOCK_TYPE = addRegistry(new MappedCustomRegistry<>("block_type"));
        ATTRIBUTE = addRegistry(new MappedCustomRegistry<>("attribute"));
        EQUIPMENT_SLOT = addRegistry(new MappedCustomRegistry<>("equipment_slot"));
        ENTITY_TYPE = addRegistry(new MappedCustomRegistry<>("entity_type"));
        LOOT_TABLE = (LootTableRegistry) addRegistry(new LootTableRegistry("loot_table"));
        EFFECT = addRegistry(new MappedCustomRegistry<>("effect"));
        SOUND = addRegistry(new MappedCustomRegistry<>("sound"));
    }

    public static @NotNull <T extends Registrable> CustomRegistry<T> addRegistry(@NotNull Plugin plugin, @NotNull CustomRegistry<T> registry){
        REGISTRY.register(plugin, registry);
        return registry;
    }

    private static @NotNull <T extends Registrable> CustomRegistry<T> addRegistry(@NotNull CustomRegistry<T> registry){
        return addRegistry(ItemsCoreU.getInstance(), registry);
    }
}
