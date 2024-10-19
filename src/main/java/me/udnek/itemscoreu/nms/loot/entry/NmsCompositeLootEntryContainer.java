package me.udnek.itemscoreu.nms.loot.entry;

import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.utils.NMS.Reflex;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class NmsCompositeLootEntryContainer implements NmsLootEntryContainer {

    @NotNull CompositeEntryBase entry;

    public NmsCompositeLootEntryContainer(@NotNull CompositeEntryBase entry){
        this.entry = entry;
    }

    public static @Nullable NmsCompositeLootEntryContainer fromVanilla(@NotNull LootTable lootTable, int n){
        int i = -1;
        for (LootPool pool : NmsUtils.getPools(NmsUtils.toNmsLootTable(lootTable))) {
            for (LootPoolEntryContainer entry : NmsUtils.getEntries(pool)) {
                if (entry instanceof CompositeEntryBase compositeEntryBase){
                    i ++;
                    if (i == n) return new NmsCompositeLootEntryContainer(compositeEntryBase);
                    if (i > n) return null;
                }
            }
        }
        return null;
    }

    public @NotNull NmsCompositeLootEntryContainer copy(){
        Constructor<AlternativesEntry> constructor = Reflex.getFirstConstructor(AlternativesEntry.class);
        AlternativesEntry newEntry = Reflex.construct(constructor, getChildren(), Reflex.getFieldValue(entry, "conditions"));
        return new NmsCompositeLootEntryContainer(newEntry);
    }

    public void addChild(@NotNull NmsLootEntryContainer container){
        List<LootPoolEntryContainer> newChildren = new ArrayList<>(getChildren());
        newChildren.add(container.get());
        setChildren(newChildren);
    }

    public void setChildren(@NotNull List<LootPoolEntryContainer> newChildren){

        Method method = Reflex.getMethod(
                CompositeEntryBase.class,
                "compose"
        );
        System.out.println(method);
        Object compose = Reflex.invokeMethod(
                entry,
                method,
                newChildren
        );
        System.out.println(compose);

        Reflex.setFieldValue(entry, "children", newChildren);
        Reflex.setFieldValue(entry, "composedChildren", compose);
    }

    public List<LootPoolEntryContainer> getChildren(){
        return  (List<LootPoolEntryContainer>) Reflex.getFieldValue(entry, "children");
    }

    public boolean removeChild(@NotNull Predicate<ItemStack> predicate){
        List<LootPoolEntryContainer> children = getChildren();
        LootPoolEntryContainer toRemove = null;
        AtomicBoolean found = new AtomicBoolean(false);
        for (LootPoolEntryContainer child : children) {
            if (child instanceof LootPoolSingletonContainer singleton){
                NmsUtils.getPossibleLoot(singleton, itemStack -> {
                    if (found.get()) return;
                    found.set(predicate.test(NmsUtils.toBukkitItemStack(itemStack)));
                });
                if (found.get()) {
                    toRemove=singleton;
                    break;
                }
            }
        }
        if (toRemove == null) return false;
        List<LootPoolEntryContainer> newChildren = new ArrayList<>(children);
        newChildren.remove(toRemove);
        setChildren(newChildren);
        return true;
    }

    public boolean replaceChild(@NotNull Predicate<ItemStack> predicate, @NotNull NmsLootEntryContainer entry){
        boolean removed = removeChild(predicate);
        if (!removed) return false;
        addChild(entry);
        return true;
    }

    @Override
    public @NotNull LootPoolEntryContainer get() {
        return entry;
    }
}
