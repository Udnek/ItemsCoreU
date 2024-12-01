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
    protected BukkitRunnable task;
    protected EntityT display;
    protected Location location;

    abstract public @Positive int getFramesAmount();
    abstract public @NotNull EntityType getType();

    public @Positive int getFrameTime() {return 1;}

    @Override
    public void play(@NotNull Location location) {
        this.location = location.clone();
        frameNumber = 0;
        spawn();
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (frameNumber == getFramesAmount()) {
                    display.remove();
                    cancel();
                }
                ConstructableCustomParticle.this.nextFrame();
                frameNumber++;
            }
        };
        task.runTaskTimer(ItemsCoreU.getInstance(), 0, getFrameTime());
    }


    @Override
    public void stop() {
        if (task != null) task.cancel();
        if (display != null) display.remove();
    }

    abstract protected void nextFrame();

    protected void spawn(){
        display = (EntityT) location.getWorld().spawnEntity(location, getType());
    }
}
