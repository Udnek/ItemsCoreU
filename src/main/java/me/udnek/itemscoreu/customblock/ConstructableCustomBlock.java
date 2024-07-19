package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ConstructableCustomBlock implements CustomBlock{

    private String id;
    protected EntitySnapshot visualRepresentation;
    protected ConstructableCustomBlock(){}
    @Override
    public void initialize(JavaPlugin javaPlugin){
        this.id = new NamespacedKey(javaPlugin, getRawId()).asString();
    }
    @Override
    public void place(Location location){
        CreatureSpawner blockState = (CreatureSpawner) Material.SPAWNER.createBlockData().createBlockState().copy(location);
        blockState.getLocation(location);
        blockState.setRequiredPlayerRange(0);
        blockState.setSpawnedEntity(getVisualRepresentation());
        blockState.getPersistentDataContainer().set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, id);
        blockState.update(true, false);

    }

    @Override
    public void onLoad(BlockState blockState) {}
    @Override
    public void onUnload(BlockState blockState) {}

    @Override
    public void destroy(Location location) {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        onDestroy(location.getBlock());
    }

    @Override
    public void onDestroy(BlockDestroyEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    @Override
    public void onDestroy(BlockBreakEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    public void onDestroy(Block block){}
    @Override
    public final @NotNull String getId() {return id;}


    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////
    public abstract ItemStack getVisualItem();

    ///////////////////////////////////////////////////////////////////////////
    // CREATING
    ///////////////////////////////////////////////////////////////////////////
    public EntitySnapshot getVisualRepresentation(){
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

}
