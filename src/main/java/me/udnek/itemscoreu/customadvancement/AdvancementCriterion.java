package me.udnek.itemscoreu.customadvancement;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface AdvancementCriterion extends Supplier<Criterion<?>>{

    AdvancementCriterion TICK = () -> CriteriaTriggers.TICK.createCriterion(PlayerTrigger.TriggerInstance.tick().triggerInstance());
    AdvancementCriterion IMPOSSIBLE = () -> CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance());
    InventoryChange INVENTORY_CHANGE = new InventoryChange();



    class InventoryChange implements AdvancementCriterion{
        private final ItemStack itemStack;

        private InventoryChange(){this(null);}
        private InventoryChange(ItemStack itemStack){
            this.itemStack = itemStack;
        }

        public @NotNull AdvancementCriterion create(@NotNull ItemStack itemStack){
            return new InventoryChange(itemStack);
        }
        @Override
        public Criterion<?> get(){
            Preconditions.checkArgument(itemStack != null, "Create criteria first!");
            net.minecraft.world.item.ItemStack nmsItemStack = NmsUtils.toNmsItemStack(itemStack);

            ItemPredicate.Builder predicate;
            if (CustomItem.isCustom(itemStack)){
                predicate = ItemPredicate.Builder.item()
                        .of(nmsItemStack.getItem())
                        .hasComponents(
                                DataComponentPredicate.builder().expect(
                                        DataComponents.CUSTOM_DATA,
                                        nmsItemStack.getComponents().get(DataComponents.CUSTOM_DATA)
                                ).build()
                        );
            } else {
                predicate = ItemPredicate.Builder.item()
                        .of(nmsItemStack.getItem())
                        .hasComponents(DataComponentPredicate.allOf(nmsItemStack.getComponents()));
            }

            return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(
                    Optional.empty(),
                    InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                    List.of(predicate.build()))
            );
        }
    }

}
