package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.nms.ConsumableComponent;
import me.udnek.itemscoreu.util.LoreBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface CustomItemProperties {
    @NotNull Material getMaterial();
    // OPTIONAL

    default @Nullable LoreBuilder getLoreBuilder(){
        ArrayList<Component> lore = new ArrayList<>();
        getLore(lore::add);
        if (lore.isEmpty()) return null;
        LoreBuilder builder = new LoreBuilder();
        lore.forEach(component -> builder.add(LoreBuilder.Position.VANILLA_LORE, component));
        return builder;
    }
    default boolean getAddDefaultAttributes(){return false;}

    /////////////////////////
    // COMPONENTS
    /////////////////////////
    @Nullable default Map<Attribute, List<AttributeModifier>> getAttributes(){return null;}
    // 2.2 banner_patterns
    // 2.3 base_color
    // 2.4 bees
    // 2.5 block_entity_data
    @Nullable default BlockData getBlockData(){return null;} // 2.6 block_state
    // 2.7 bucket_entity_data
    // 2.8 bundle_contents
    // 2.9 can_break
    // 2.10 can_place_on
    // 2.11 charged_projectiles
    @Nullable default ConsumableComponent getConsumable(){return null;} // 2.12 consumable todo replace when bukkit ready
    // 2.13 container
    // 2.14 container_loot
    // 2.15 custom_data
    @Nullable default Integer getCustomModelData(){return null;} // 2.16 custom_model_data
    @Nullable default Component getDisplayName(){return null;} // 2.17 custom_name
    // 2.18 damage
    @Nullable default Tag<DamageType> getDamageResistant(){return null;} // 2.19 damage_resistant
    // 2.20 debug_stick_state
    // todo 2.21 death_protection
    // todo 2.22 dyed_color
    // todo 2.23 enchantable
    @Nullable default Boolean getEnchantmentGlintOverride(){return null;} // 2.24 enchantment_glint_override
    // 2.25 enchantments
    // 2.26 entity_data
    @Nullable default EquippableComponent getEquippable(){return null;} // 2.27 equippable
    // 2.28 fire_resistant
    // 2.29 firework_explosion
    // 2.30 fireworks
    @Nullable default FoodComponent getFood(){return null;} // 2.31 food
    @Nullable default Boolean getGlider(){return null;} // 2.32 glider
    @Nullable default ItemFlag[] getTooltipHides(){return null;} // 2.33 hide_additional_tooltip
    default boolean getHideTooltip(){return false;} // 2.34 hide_tooltip
    @Nullable default MusicInstrument getMusicInstrument(){return null;} // 2.35 instrument
    // 2.36 intangible_projectile
    @Nullable default NamespacedKey getItemModel(){return null;} // 2.37 item_model
    @Nullable default Component getItemName(){return null;}// 2.38 item_name
    // todo 2.39 jukebox_playable
    // 2.40 lock
    // 2.41 lodestone_tracker
    default void getLore(@NotNull Consumer<Component> consumer){} // 2.42 lore
    // 2.43 map_color
    // 2.44 map_decorations
    // 2.45 map_id
    @Nullable default Integer getMaxDamage(){return null;} // 2.46 max_damage
    @Nullable default Integer getMaxStackSize(){return null;} // 2.47 max_stack_size
    // 2.48 note_block_sound
    // 2.49 ominous_bottle_amplifier
    // 2.50 pot_decorations
    // 2.51 potion_contents
    // 2.52 profile
    @Nullable default ItemRarity getRarity(){return null;} // 2.53 rarity
    // 2.54 recipes
    // todo 2.55 repairable
    // 2.56 repair_cost
    // 2.57 stored_enchantments
    // 2.58 suspicious_stew_effects
    @Nullable default ToolComponent getTool(){return null;} // 2.59 tool
    // todo 2.60 tooltip_style
    @Nullable default ArmorTrim getTrim(){return null;} // 2.61 trim
    default boolean getUnbreakable(){return false;} // 2.62 unbreakable
    @Nullable default UseCooldownComponent getUseCooldown(){return null;} // 2.63 use_cooldown
    @Nullable default ItemStack getUseRemainder(){return null;} // 2.64 use_remainder
    @Nullable default CustomItem getUseRemainderCustom(){return null;} // 2.64 use_remainder
    // 2.65 writable_book_content
    // 2.66 written_book_content























    // CONSUMABLE
    //  death_protection
    // EQUIPPABLE
    //JUKEBOX
    // COLDOWN


}
