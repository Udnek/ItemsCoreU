package me.udnek.itemscoreu.customentitylike.block.constructabletype;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class SpawnerBasedConstructableBlockType extends AbstactCustomBlockType {

    protected EntitySnapshot visualRepresentation;

    @Override
    public final @NotNull Material getMaterial() {return Material.SPAWNER;}

    @Override
    @OverridingMethodsMustInvokeSuper
    public void place(@NotNull Location location){
        super.place(location);
/*        CreatureSpawner blockState = (CreatureSpawner) location.getBlock().getState();
        blockState.setRequiredPlayerRange(0);
        blockState.setSpawnedEntity(getVisualRepresentation());
        blockState.update(true, false);*/
    }

    public abstract @NotNull ItemStack getVisualItem();

    public @NotNull EntitySnapshot getVisualRepresentation(){
        if (visualRepresentation == null){
            Location location = new Location(Bukkit.getWorld("world"), 0, 0, 0);
            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setHeadPose(new EulerAngle(Math.PI/6, 0, 0));
            armorStand.getEquipment().setHelmet(getVisualItem());
            armorStand.setVisible(false);
            visualRepresentation = armorStand.createSnapshot();
            armorStand.remove();
        }
        return visualRepresentation;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onDestroy(@NotNull Block block) {
        super.onDestroy(block);
        Location centerLocation = block.getLocation().toCenterLocation();
        // TODO: 7/22/2024 FIX PARTICLES
        ParticleBuilder builder = new ParticleBuilder(Particle.ITEM);
        builder.location(centerLocation);
        builder.data(getVisualItem());
        builder.count(10);
        builder.offset(0.5, 0.5, 0.5);
        builder.extra(0);
        builder.spawn();
    }
}
