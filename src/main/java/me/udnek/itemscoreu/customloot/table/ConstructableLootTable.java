package me.udnek.itemscoreu.customloot.table;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ConstructableLootTable extends BasicLootTable {

    protected String rawId;
    protected NamespacedKey id;
    public ConstructableLootTable(@NotNull String rawId){
        this.rawId = rawId;
    }
    @Override
    public void initialize(@NotNull Plugin plugin) {
        id = new NamespacedKey(plugin, rawId);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return id;
    }
}
