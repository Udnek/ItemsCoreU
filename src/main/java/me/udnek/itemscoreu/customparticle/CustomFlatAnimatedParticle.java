package me.udnek.itemscoreu.customparticle;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomFlatAnimatedParticle extends CustomFlatParticle{

    protected ItemStack itemStack = this.getItemStack();

    public CustomFlatAnimatedParticle(Location location) {
        super(location);
    }

    protected void updateCustomModelData(int customModelData){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
    }

    protected abstract int currentModelData();

    protected void nextFrame(){
        updateCustomModelData(currentModelData());
        entity.setItemStack(itemStack);
    }
}
