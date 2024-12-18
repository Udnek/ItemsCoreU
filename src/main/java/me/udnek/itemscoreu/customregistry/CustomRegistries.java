package me.udnek.itemscoreu.customregistry;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customenchantment.CustomEnchantment;
import me.udnek.itemscoreu.customentity.CustomEntityType;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableRegistry;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomRegistries {

    private static final HashMap<NamespacedKey, CustomRegistry<?>> registries = new HashMap<>();

    public static final CustomRegistry<CustomItem> ITEM = addRegistry("item", new MappedCustomRegistry<>("Item"));
    public static final CustomRegistry<CustomBlock> BLOCK = addRegistry("bloc", new MappedCustomRegistry<>("Block"));
    public static final CustomRegistry<CustomAttribute> ATTRIBUTE = addRegistry("attribute", new MappedCustomRegistry<>("Attribute"));
    public static final CustomRegistry<CustomEquipmentSlot> EQUIPMENT_SLOT = addRegistry("equipment_slot", new MappedCustomRegistry<>("EquipmentSlot"));
    public static final CustomRegistry<CustomComponentType<?, ?>> COMPONENT_TYPE = addRegistry("component_type", new MappedCustomRegistry<>("ComponentType"));
    public static final CustomRegistry<CustomEntityType<?>> ENTITY_TYPE = addRegistry("entity_type", new MappedCustomRegistry<>("EntityType"));
    public static final LootTableRegistry LOOT_TABLE = (LootTableRegistry) addRegistry("loot_table", new LootTableRegistry());
    public static final CustomRegistry<CustomEffect> EFFECT = addRegistry("effect", new MappedCustomRegistry<>("Effect"));
    public static final CustomRegistry<CustomEnchantment> ENCHANTMENT = addRegistry("enchantment", new MappedCustomRegistry<>("Enchantment"));


    public static @NotNull <T extends Registrable> CustomRegistry<T> addRegistry(@NotNull NamespacedKey key, @NotNull CustomRegistry<T> registry){
        Preconditions.checkArgument(!registries.containsKey(key), "Registry already exists: " + key.asString());
        registries.put(key, registry);
        return registry;
    }

    public static @Nullable CustomRegistry<?> getRegistry(@NotNull NamespacedKey key){
        return registries.get(key);
    }

    public static @NotNull <T extends Registrable> CustomRegistry<T> addRegistry(@NotNull String name, @NotNull CustomRegistry<T> registry){
        return addRegistry(new NamespacedKey(ItemsCoreU.getInstance(), name), registry);
    }

    public static @NotNull Map<@NotNull NamespacedKey, @NotNull CustomRegistry<?>> getRegistries() {
        return new HashMap<>(registries);
    }
}
