package me.udnek.itemscoreu.customevent;

import me.udnek.itemscoreu.nms.ItemConsumer;
import me.udnek.itemscoreu.nms.NmsUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R1.CraftLootTable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LootTableGenerateEvent extends CancellableCustomEvent{

    private static final HandlerList HANDLER_LIST = CustomEvent.HANDLER_LIST;

    protected final LootTable nmsLootTable;
    protected final ItemConsumer consumer;
    protected final LootContext nmsLootContext;
    public LootTableGenerateEvent(LootTable nmsLootTable, ItemConsumer itemConsumer, LootContext lootContext){
        this.nmsLootTable = nmsLootTable;
        this.consumer = itemConsumer;
        this.nmsLootContext = lootContext;
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
    public @NotNull org.bukkit.loot.LootContext getLootContext(){
        return CraftLootTable.convertContext(nmsLootContext);
    }
    public static HandlerList getHandlerList() {return HANDLER_LIST;}
}
