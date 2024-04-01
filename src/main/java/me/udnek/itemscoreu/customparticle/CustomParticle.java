package me.udnek.itemscoreu.customparticle;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;

public abstract class CustomParticle {

    Location location;
    public final int framesAmount = this.getDuration() / this.frameTime();
    public int frameNumber = 0;
    protected ItemDisplay entity;

    public CustomParticle(Location location){
        this.location = location;
    }

    public Location getLocation() {return location;}

    abstract public int getDuration();
    public int frameTime() {return 1;}
    abstract public Transformation getTransformation();

    public void play(){
        this.spawn();
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.remove();
            }
        }.runTaskLater(ItemsCoreU.getInstance(), this.getDuration());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (frameNumber == framesAmount) {
                    entity.remove();
                    this.cancel();
                }
                CustomParticle.this.nextFrame();
                frameNumber++;
            }
        }.runTaskTimer(ItemsCoreU.getInstance(), 0, frameTime());
    }

    abstract protected void nextFrame();
    protected void afterSpawned(){}

    protected void spawn(){
        ItemDisplay entity = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        this.entity = entity;
        entity.setItemStack(new ItemStack(Material.BARRIER));
        entity.setTransformation(this.getTransformation());
        this.afterSpawned();
    }
}
