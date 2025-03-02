package me.udnek.itemscoreu.nms.loot.entry;

import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.util.NmsFields;
import me.udnek.itemscoreu.nms.loot.util.NmsLootConditionsContainer;
import me.udnek.itemscoreu.util.Reflex;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NmsCompositeEntryContainer implements NmsLootEntryContainer {

    CompositeEntryBase supply;

    public NmsCompositeEntryContainer(@NotNull CompositeEntryBase supply) {this.supply = supply;}

    public static @Nullable NmsCompositeEntryContainer fromVanilla(@NotNull LootTable lootTable, int n){
        int i = -1;
        for (LootPool pool : NmsUtils.getPools(NmsUtils.toNmsLootTable(lootTable))) {
            for (LootPoolEntryContainer entry : NmsUtils.getEntries(pool)) {
                if (entry instanceof CompositeEntryBase compositeEntryBase){
                    i ++;
                    if (i == n) return new NmsCompositeEntryContainer(compositeEntryBase);
                    if (i > n) return null;
                }
            }
        }
        return null;
    }

    public @NotNull NmsCompositeEntryContainer copy(){
        Constructor<AlternativesEntry> constructor = Reflex.getFirstConstructor(AlternativesEntry.class);
        AlternativesEntry newEntry = Reflex.construct(constructor, getChildren(), Reflex.getFieldValue(supply, NmsFields.CONDITIONS));
        return new NmsCompositeEntryContainer(newEntry);
    }

    public void addChild(@NotNull NmsLootEntryContainer container){
        List<LootPoolEntryContainer> newChildren = new ArrayList<>(getChildren());
        newChildren.add(container.get());
        setChildren(newChildren);
    }
    public void addChild(int n, @NotNull NmsLootEntryContainer container){
        List<LootPoolEntryContainer> newChildren = new ArrayList<>(getChildren());
        newChildren.add(n, container.get());
        setChildren(newChildren);
    }
    public @NotNull NmsLootEntryContainer getChild(int n){
        return NmsLootEntryContainer.from(getChildren().get(n));
    }
    public void removeChild(int n){
        List<LootPoolEntryContainer> children = new ArrayList<>(getChildren());
        children.remove(n);
        setChildren(children);
    }

    public void setChildren(@NotNull List<LootPoolEntryContainer> newChildren){

        Method method = Reflex.getMethod(
                CompositeEntryBase.class,
                "compose"
        );
        Object compose = Reflex.invokeMethod(
                supply,
                method,
                newChildren
        );

        Reflex.setFieldValue(supply, NmsFields.CHILDREN, newChildren);
        Reflex.setFieldValue(supply, NmsFields.COMPOSED_CHILDREN, compose);
    }

    public List<LootPoolEntryContainer> getChildren(){
        return  (List<LootPoolEntryContainer>) Reflex.getFieldValue(supply, NmsFields.CHILDREN);
    }
/*

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

*/

    @Override
    public @NotNull NmsLootConditionsContainer getConditions() {
        return null;
    }

    @Override
    public void setConditions(@NotNull NmsLootConditionsContainer conditions) {

    }

    @Override
    public CompositeEntryBase get() {
        return supply;
    }
}
