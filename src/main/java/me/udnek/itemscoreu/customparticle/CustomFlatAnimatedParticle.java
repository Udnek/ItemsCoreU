package me.udnek.itemscoreu.customparticle;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomFlatAnimatedParticle extends CustomFlatParticle{

    protected ItemStack icon = this.getItemStack();

    public CustomFlatAnimatedParticle(@NotNull Location location) {
        super(location);
    }

    protected abstract @NotNull NamespacedKey getCurrentModelPath();

    protected void updateModel(){
        icon.editMeta(itemMeta -> itemMeta.setItemModel(getCurrentModelPath()));
    }

    protected void nextFrame(){
        updateModel();
        display.setItemStack(icon);
    }
}
