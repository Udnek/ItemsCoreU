package me.udnek.itemscoreu.customentitylike.block.constructabletype;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class DisplayBasedConstructableBlockType extends AbstactCustomBlockType {

    public abstract @NotNull ItemStack getVisualItem();

    @Override
    public void place(@NotNull Location location) {
        super.place(location);
/*        Location displayLocation = location.toCenterLocation();
        displayLocation.setYaw(0);
        displayLocation.setPitch(0);
        ItemDisplay entity = (ItemDisplay) location.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        entity.setItemStack(getVisualItem());
        Transformation transformation = entity.getTransformation();
        transformation.getScale().set(1.0001);
        entity.setTransformation(transformation);*/
    }

    @Override
    public void onDestroy(@NotNull Block block) {
        super.onDestroy(block);
        Collection<Entity> displays = block.getWorld().getNearbyEntities(block.getLocation().toCenterLocation(), 0.05, 0.05, 0.05);
        for (Entity display : displays) {
            if (display.getType() == EntityType.ITEM_DISPLAY) display.remove();
        }
    }
}
