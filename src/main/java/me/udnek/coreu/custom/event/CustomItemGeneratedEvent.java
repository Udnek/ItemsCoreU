package me.udnek.coreu.custom.event;

import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.RepairData;
import me.udnek.coreu.util.LoreBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemGeneratedEvent extends CustomEvent{
    protected @NotNull CustomItem customItem;
    protected @NotNull ItemStack itemStack;
    protected @NotNull LoreBuilder loreBuilder;
    protected @Nullable RepairData repairData;

    public CustomItemGeneratedEvent(@NotNull CustomItem customItem, @NotNull ItemStack itemStack, @Nullable LoreBuilder loreBuilder, @Nullable RepairData repairData){
        this.customItem = customItem;
        this.itemStack = itemStack;
        this.repairData = repairData;
        if (loreBuilder == null) this.loreBuilder = new LoreBuilder();
        else this.loreBuilder = loreBuilder;
    }
    public @NotNull CustomItem getCustomItem() {
        return customItem;
    }
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
    public @NotNull LoreBuilder getLoreBuilder() {
        return loreBuilder;
    }
    public @Nullable RepairData getRepairData(){return repairData;}
    public void setRepairData(@Nullable RepairData repairData) {this.repairData = repairData;}

    public void setItemStack(@NotNull ItemStack itemStack){
        this.itemStack = itemStack;
    }
}
