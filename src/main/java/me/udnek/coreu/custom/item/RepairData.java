package me.udnek.coreu.custom.item;

import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistrySet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepairData {

    public static final RepairData EMPTY = new RepairData(Set.of(), Set.of());

    // TODO VANILLA REPLACER
    protected Set<CustomItem> customs;
    protected Set<Material> materials;

    public RepairData(@NotNull Set<@NotNull CustomItem> customs, @NotNull Set<@NotNull Material> materials){
        this.customs = new HashSet<>(customs);
        this.materials = new HashSet<>(materials);
    }
    public RepairData(@NotNull CustomItem ...customs){
        this(Set.of(customs), Set.of());
    }
    public RepairData(@NotNull Material ...materials){
        this(Set.of(), Set.of(materials));
    }

    public boolean isEmpty(){
        return customs.isEmpty() && materials.isEmpty();
    }

    public @NotNull List<ItemStack> getStacks(){
        List<ItemStack> stacks = new ArrayList<>();
        for (CustomItem custom : customs) stacks.add(custom.getItem());
        for (Material material : materials) stacks.add(new ItemStack(material));
        return stacks;
    }

    public @NotNull Set<@NotNull CustomItem> getCustomItems() {
        return customs;
    }
    public @NotNull Set<@NotNull Material> getMaterials() {
        return materials;
    }

    public @NotNull Repairable getSuitableVanillaRepairable(){
        Set<ItemType> materials = new HashSet<>();
        getCustomItems().forEach(customItem -> materials.add(customItem.getItem().getType().asItemType()));
        getMaterials().forEach(material -> materials.add(material.asItemType()));
        return Repairable.repairable(RegistrySet.keySetFromValues(RegistryKey.ITEM, materials));
    }

    public boolean contains(@NotNull ItemStack itemStack){
        return ItemUtils.containsSame(itemStack, materials, customs);
    }

}
