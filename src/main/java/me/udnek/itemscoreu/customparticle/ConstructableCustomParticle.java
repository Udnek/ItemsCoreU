package me.udnek.itemscoreu.customparticle;

import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableCustomParticle<EntityT extends Entity> implements CustomParticle {
    public int frameNumber;
    protected EntityT display;
    protected Location location;

    abstract public @Positive int getFramesAmount();
    abstract public @NotNull EntityType getType();

    public @Positive int getFrameTime() {return 1;}

    @Override
    public void play(@NotNull Location location) {
        this.location = location;
        frameNumber = 0;
        spawn();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (frameNumber == getFramesAmount()) {
                    display.remove();
                    cancel();
                }
                ConstructableCustomParticle.this.nextFrame();
                frameNumber++;
            }
        }.runTaskTimer(ItemsCoreU.getInstance(), 0, getFrameTime());
    }

    abstract protected void nextFrame();
    protected void spawn(){
        display = (EntityT) location.getWorld().spawnEntity(location, getType());
    }
}
