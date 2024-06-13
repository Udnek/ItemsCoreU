package me.udnek.itemscoreu.customblock;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.util.Set;

public abstract class CustomBlock {

    public static final NamespacedKey PERSISTENT_DATA_CONTAINER_NAMESPACE = new NamespacedKey(ItemsCoreU.getInstance(), "block");
    private String id;
    protected EntitySnapshot visualRepresentation;
    protected CustomBlock(){}
    protected void initialize(JavaPlugin javaPlugin){
        this.id = new NamespacedKey(javaPlugin, getRawId()).asString();
    }
    public void place(Location location){
        CreatureSpawner blockState = (CreatureSpawner) Material.SPAWNER.createBlockData().createBlockState().copy(location);
        blockState.getLocation(location);
        blockState.setRequiredPlayerRange(0);
        blockState.setSpawnedEntity(getVisualRepresentation());
        blockState.getPersistentDataContainer().set(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING, id);
        blockState.update(true, false);
    }

    public void onDestroy(BlockDestroyEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    public void onDestroy(BlockBreakEvent event){
        event.setExpToDrop(0);
        onDestroy(event.getBlock());
    }
    public void onDestroy(Block block){}
    public final String getId() {return id;}

    ///////////////////////////////////////////////////////////////////////////
    // STATIC
    ///////////////////////////////////////////////////////////////////////////
    public static String getId(Block block){
        if (block == null) return null;
        if (block.getBlockData().getMaterial() != Material.SPAWNER) return null;
        CreatureSpawner state = (CreatureSpawner) block.getState();
        return state.getPersistentDataContainer().get(PERSISTENT_DATA_CONTAINER_NAMESPACE, PersistentDataType.STRING);
    }
    public static String getId(Location location){
        if (location == null) return null;
        return getId(location.getBlock());
    }

    public static CustomBlock get(Block block){
        return CustomBlockManager.get(getId(block));
    }
    public static CustomBlock get(Location location){
        return CustomBlockManager.get(getId(location));
    }
    public static CustomBlock get(String id){
        return CustomBlockManager.get(id);
    }

    public static Set<String> getAllIds(){return CustomBlockManager.getAllIds();}

    ///////////////////////////////////////////////////////////////////////////
    // PROPERTIES
    ///////////////////////////////////////////////////////////////////////////
    public abstract String getRawId();
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
