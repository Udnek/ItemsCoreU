package me.udnek.itemscoreu.customitem;

import me.udnek.itemscoreu.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class RepairData {


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

    public @NotNull Set<@NotNull CustomItem> getCustomItems() {
        return customs;
    }
    public @NotNull Set<@NotNull Material> getMaterials() {
        return materials;
    }

    public boolean contains(@NotNull ItemStack itemStack){
        return ItemUtils.containsSame(itemStack, materials, customs);
    }

}
