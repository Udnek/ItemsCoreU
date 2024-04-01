package me.udnek.itemscoreu.customitem;

import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomModelDataItem extends CustomItem{

    public abstract int getCustomModelData();

    @Override
    protected void modifyFinalItemMeta(ItemMeta itemMeta) {
        super.modifyFinalItemMeta(itemMeta);
        itemMeta.setCustomModelData(this.getCustomModelData());
    }
}
