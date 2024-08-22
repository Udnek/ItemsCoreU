package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.ItemConsumer;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LootEntryCreateItemEvent extends CancellableCustomEvent{

    private static final HandlerList HANDLER_LIST = CustomEvent.HANDLER_LIST;

    protected final LootTable nmsLootTable;
    protected final ItemConsumer consumer;
    public LootEntryCreateItemEvent(LootTable nmsLootTable, ItemConsumer itemConsumer){
        this.nmsLootTable = nmsLootTable;
        this.consumer = itemConsumer;
    }
    public boolean isSameLootTable(@NotNull org.bukkit.loot.LootTable other){
        return nmsLootTable.craftLootTable.hashCode() == other.hashCode();
    }

    public @NotNull org.bukkit.loot.LootTable getLootTable(){
        return nmsLootTable.craftLootTable;
    }
    public boolean contains(@NotNull Material material){
        Item nmsMaterial = NmsUtils.toNmsMaterial(material);
        for (net.minecraft.world.item.ItemStack itemStack : consumer.get()) {
            if (itemStack.getItem() == nmsMaterial) return true;
        }
        return false;
    }
    public @NotNull List<ItemStack> getAll(){
        return consumer.get().stream().map(NmsUtils::toBukkitItemStack).collect(Collectors.toList());
    }

    public void addItem(@NotNull ItemStack itemStack){
        consumer.accept(NmsUtils.toNmsItemStack(itemStack));
    }

    public void setAll(@NotNull Collection<ItemStack> stacks){
        consumer.clear();
        stacks.stream().map(NmsUtils::toNmsItemStack).forEach(consumer::accept);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
