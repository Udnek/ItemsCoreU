package me.udnek.itemscoreu.util;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.VanillaBasedCustomItem;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.customrecipe.CustomRecipe;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.loot.entry.NmsCustomLootEntryBuilder;
import me.udnek.itemscoreu.nms.loot.util.ItemStackCreator;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class VanillaItemManager extends SelfRegisteringListener{

    private static final EquipmentSlot[] ENTITY_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
    private final Set<Material> disabled = new HashSet<>();
    private final EnumMap<Material, VanillaBasedCustomItem> replacedByMaterial = new EnumMap<>(Material.class);
    private final List<VanillaBasedCustomItem> replacedItems = new ArrayList<>();
    private static VanillaItemManager instance;
    public static VanillaItemManager getInstance() {
        if (instance == null) instance = new VanillaItemManager();
        return instance;
    }
    private VanillaItemManager(){
        super(ItemsCoreU.getInstance());
    }

    public void disableVanillaMaterial(@NotNull Material material){
        Preconditions.checkArgument(material != null, "Material can not be null!");
        disabled.add(material);
    }
    public void replaceVanillaMaterial(@NotNull Material material){
        Preconditions.checkArgument(material != null, "Material can not be null!");
        if (isReplaced(material)) return;
        VanillaBasedCustomItem customItem = new VanillaBasedCustomItem(material);
        replacedByMaterial.put(material, customItem);
        replacedItems.add(customItem);
        CustomRegistries.ITEM.register(ItemsCoreU.getInstance(), customItem);
        
    }

    public void start(){
        startDisabler();
        startReplacer();
    }

    private void startDisabler(){
        RecipeManager recipeManager = RecipeManager.getInstance();
        for (Material material : disabled) {

            ItemStack toRemoveItem = new ItemStack(material);

            // recipe removal
            ArrayList<Recipe> recipes = new ArrayList<>();
            recipeManager.getRecipesAsIngredient(toRemoveItem, recipes::add);
            recipeManager.getRecipesAsResult(toRemoveItem, recipes::add);

            for (Recipe recipe : recipes) {
                recipeManager.unregister(recipe);
                //LogUtils.pluginLog("Unregistered recipe: " + (recipe instanceof Keyed keyed ? keyed.key().asString() : recipe));
            }

            // loot table removal
            for (LootTable lootTable : LootTableUtils.getWhereItemOccurs(toRemoveItem)) {
                if (lootTable instanceof CustomLootTable customLootTable) {
                    customLootTable.removeItem(toRemoveItem);
                    //LogUtils.pluginLog("Custom lootTable was edited: " + customLootTable.getKey().asString());
                } else {
                    Nms.get().removeAllEntriesContains(lootTable, VanillaItemManager::isDisabled);
                    //LogUtils.pluginLog("Removed entries from vanilla lootTable: " + lootTable.getKey().asString());
                }
            }

            LogUtils.pluginLog("Disabled: " + material);
        }
    }

    private void startReplacer(){
        RecipeManager recipeManager = RecipeManager.getInstance();
        for (Map.Entry<Material, VanillaBasedCustomItem> entry : replacedByMaterial.entrySet()) {
            VanillaBasedCustomItem vanillaBasedItem = entry.getValue();

            ItemStack oldItem = new ItemStack(entry.getKey());

            // recipe replace
            ArrayList<Recipe> recipes = new ArrayList<>();
            recipeManager.getRecipesAsIngredient(oldItem, recipes::add);
            recipeManager.getRecipesAsResult(oldItem, recipes::add);

            ReplaceHelper replaceHelper = new ReplaceHelper(oldItem, vanillaBasedItem);
            for (Recipe oldRecipe : recipes) {
                if (oldRecipe instanceof CustomRecipe<?> customRecipe){
                    customRecipe.replaceItem(oldItem, vanillaBasedItem.getItem());
                } else {
                    Recipe newRecipe = replaceHelper.copyRecipeWithReplacedIngredient(oldRecipe);
                    recipeManager.unregister(oldRecipe);
                    recipeManager.register(newRecipe);
                }

                //LogUtils.pluginLog("Replaced recipe: " + (newRecipe instanceof Keyed keyed ? keyed.key().asString() : newRecipe));
            }

            // loot table replace
            for (LootTable lootTable : LootTableUtils.getWhereItemOccurs(oldItem)) {
                if (lootTable instanceof CustomLootTable customLootTable) {
                    customLootTable.replaceItem(oldItem, vanillaBasedItem.getItem());
                    //LogUtils.pluginLog("Custom lootTable was edited: " + customLootTable.getKey().asString());
                } else {
                    Predicate<ItemStack> predicate = itemStack -> CustomItem.get(itemStack) == vanillaBasedItem;
                    Pair<Integer, Integer> weightAndQuality = Nms.get().getWeightAndQuality(lootTable, predicate);
                    if (weightAndQuality == null) continue;
                    Nms.get().replaceAllEntriesContains(lootTable, predicate, NmsCustomLootEntryBuilder.fromVanilla(lootTable, predicate, new ItemStackCreator.Custom(vanillaBasedItem)));
                    //LogUtils.pluginLog("Vanilla lootTable was replaced!: " + lootTable.getKey().asString());
                }
            }

            LogUtils.pluginLog("Replaced: " + entry.getKey());
        }
    }

    public static boolean isDisabled(@NotNull ItemStack item){
        if (CustomItem.isCustom(item)) return false;
        return getInstance().disabled.contains(item.getType());
    }
    public static boolean isReplaced(@NotNull ItemStack itemStack){
        CustomItem customItem = CustomItem.get(itemStack);
        if (customItem == null) return false;
        return isReplaced(customItem);
    }
    public static boolean isReplaced(@NotNull CustomItem customItem){
        return getInstance().replacedItems.contains(customItem);
    }
    public static boolean isReplaced(@NotNull Material material){
        return getInstance().replacedByMaterial.containsKey(material);
    }
    public static @NotNull ItemStack replace(@NotNull ItemStack itemStack){
        if (!isReplaced(itemStack)) return itemStack;
        return getInstance().replacedByMaterial.get(itemStack.getType()).getFrom(itemStack);
    }
    public static @Nullable VanillaBasedCustomItem getReplaced(@NotNull ItemStack itemStack){
        if (!isReplaced(itemStack)) return null;
        return getInstance().replacedByMaterial.get(itemStack.getType());
    }
    public static @Nullable VanillaBasedCustomItem getReplaced(@NotNull Material material){
        return getInstance().replacedByMaterial.get(material);
    }

    // EVENTS

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        EntityEquipment equipment = livingEntity.getEquipment();
        if (equipment == null) return;
        for (EquipmentSlot slot : ENTITY_SLOTS) {
            ItemStack item = equipment.getItem(slot);
            if (isDisabled(item)) equipment.setItem(slot, null);
            else if (isReplaced(item)) equipment.setItem(slot, replace(item));
        }
    }

    @EventHandler
    public void onCreative(InventoryCreativeEvent event){
        ItemStack item = event.getCursor();
        if (isDisabled(item)){
            event.setCancelled(true);
            event.getViewers().getFirst().sendMessage(Component.text("Item is disabled!").color(NamedTextColor.RED));
        } else if (isReplaced(item)){
            event.getViewers().getFirst().sendMessage(Component.text("Item is replaced!").color(NamedTextColor.GREEN));
            event.setCursor(replace(item));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNewTrade(VillagerAcquireTradeEvent event){
        MerchantRecipe recipe = event.getRecipe();

        ItemStack result = recipe.getResult();
        if (isDisabled(result)) {event.setCancelled(true); return;}
        if (isReplaced(result)){
            recipe = new MerchantRecipe(
                    replace(result),
                    recipe.getUses(),
                    recipe.getMaxUses(),
                    recipe.hasExperienceReward(),
                    recipe.getVillagerExperience(),
                    recipe.getPriceMultiplier(),
                    recipe.getDemand(),
                    recipe.getSpecialPrice(),
                    recipe.shouldIgnoreDiscounts()
            );
            recipe.setIngredients(event.getRecipe().getIngredients());
        }

        List<ItemStack> newIngredients = new ArrayList<>();
        for (ItemStack ingredient : recipe.getIngredients()) {
            if (isReplaced(ingredient)) newIngredients.add(replace(ingredient));
            else if (!isDisabled(ingredient)) newIngredients.add(ingredient);
        }

        if (newIngredients.isEmpty()){event.setCancelled(true);return;}

        recipe.setIngredients(newIngredients);
        event.setRecipe(recipe);
    }

    public static class ReplaceHelper{

        private final ItemStack oldItem;
        private final VanillaBasedCustomItem newItem;
        public ReplaceHelper(ItemStack oldItem, VanillaBasedCustomItem vanillaBasedCustomItem){
            this.oldItem = oldItem;
            this.newItem = vanillaBasedCustomItem;
        }
        private RecipeChoice replaceChoice(RecipeChoice choice){
            return replaceChoice(choice, oldItem, newItem.getItem());
        }
        private ItemStack replaceResult(ItemStack itemStack){
            if (!ItemUtils.isSameIds(oldItem, itemStack)) return itemStack;
            return newItem.getFrom(itemStack);
        }

        private Recipe copyRecipeWithReplacedIngredient(Recipe abstractRecipe){
            Preconditions.checkArgument(!(abstractRecipe instanceof CustomRecipe), "Custom recipes are not allowed!");
            if (abstractRecipe instanceof BlastingRecipe recipe){
                return new BlastingRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getInputChoice()), recipe.getExperience(), recipe.getCookingTime());
            } else if (abstractRecipe instanceof FurnaceRecipe recipe){
                return new FurnaceRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getInputChoice()), recipe.getExperience(), recipe.getCookingTime());
            } else if (abstractRecipe instanceof SmokingRecipe recipe){
                return new SmokingRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getInputChoice()), recipe.getExperience(), recipe.getCookingTime());
            } else if (abstractRecipe instanceof CampfireRecipe recipe){
                return new CampfireRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getInputChoice()), recipe.getExperience(), recipe.getCookingTime());
            }

            else if (abstractRecipe instanceof ShapedRecipe recipe){
                ShapedRecipe newRecipe = new ShapedRecipe(recipe.getKey(), replaceResult(recipe.getResult()));
                newRecipe.shape(recipe.getShape());

                for (Map.Entry<Character, RecipeChoice> entry : recipe.getChoiceMap().entrySet()) {
                    if (entry.getValue() == null) continue; // BUG WITH LEATHER BOOTS
                    newRecipe.setIngredient(entry.getKey(), replaceChoice(entry.getValue()));
                }
                return newRecipe;
            }
            else if (abstractRecipe instanceof ShapelessRecipe recipe){
                ShapelessRecipe newRecipe = new ShapelessRecipe(recipe.getKey(), replaceResult(recipe.getResult()));
                for (RecipeChoice choice : recipe.getChoiceList()) {
                    newRecipe.addIngredient(replaceChoice(choice));
                }
                return newRecipe;
            }

            else if (abstractRecipe instanceof SmithingTransformRecipe recipe){
                return new SmithingTransformRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getTemplate()), replaceChoice(recipe.getBase()), replaceChoice(recipe.getAddition()), recipe.willCopyDataComponents());
            } else if (abstractRecipe instanceof SmithingTrimRecipe recipe){
                return new SmithingTrimRecipe(recipe.getKey(), replaceChoice(recipe.getTemplate()), replaceChoice(recipe.getBase()), replaceChoice(recipe.getAddition()), recipe.willCopyDataComponents());
            }

            else if (abstractRecipe instanceof StonecuttingRecipe recipe){
                return new StonecuttingRecipe(recipe.getKey(), replaceResult(recipe.getResult()), replaceChoice(recipe.getInputChoice()));
            }

            throw new IllegalArgumentException("Replacer does not support recipe: " + abstractRecipe);
        }


        private static RecipeChoice replaceChoice(RecipeChoice recipeChoice, ItemStack oldItem, ItemStack newItem){
            if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice){
                Material oldMaterial = oldItem.getType();
                Material newMaterial = newItem.getType();
                List<Material> newChoices = new ArrayList<>();
                for (Material choice : materialChoice.getChoices()) {
                    if (choice == oldMaterial){
                        newChoices.add(newMaterial);
                    } else newChoices.add(choice);
                }
                return new RecipeChoice.MaterialChoice(newChoices);
            } else if (recipeChoice instanceof RecipeChoice.ExactChoice exactChoice) {
                List<ItemStack> newChoices = new ArrayList<>();
                for (ItemStack choice : exactChoice.getChoices()) {
                    if (ItemUtils.isSameIds(choice, oldItem)){
                        newChoices.add(newItem);
                    } else newChoices.add(choice);
                }
                return new RecipeChoice.ExactChoice(newChoices);
            }
            LogUtils.pluginWarning("Recipe choice is not Material or Exact or Empty: " + recipeChoice);
            return recipeChoice;
        }
    }
}
