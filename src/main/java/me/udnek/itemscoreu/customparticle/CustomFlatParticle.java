package me.udnek.itemscoreu.customparticle;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

public abstract class CustomFlatParticle extends CustomParticle{

    public CustomFlatParticle(Location location) {
        super(location);
    }

    @Override
    public Transformation getTransformation() {
        Transformation transformation = entity.getTransformation();
        transformation.getScale().set(getXScale(), getYScale(), 0);
        return transformation;
    }

    abstract public double getXScale();
    abstract public double getYScale();

    protected abstract ItemStack getItemStack();

    @Override
    protected void afterSpawned() {
        entity.setBillboard(Display.Billboard.CENTER);
        entity.setItemStack(getItemStack());
    }
}
