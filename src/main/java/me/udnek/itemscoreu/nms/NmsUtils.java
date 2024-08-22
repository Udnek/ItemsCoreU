package me.udnek.itemscoreu.nms;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;

public class NmsUtils {

    // ITEM
    public static net.minecraft.world.item.ItemStack toNmsItemStack(ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }
    public static Item toNmsMaterial(Material material){
        return CraftMagicNumbers.getItem(material);
    }
    public static ItemStack toBukkitItemStack(net.minecraft.world.item.ItemStack itemStack){
        return CraftItemStack.asBukkitCopy(itemStack);
    }
    // ENTITY
    public static Entity toNmsEntity(org.bukkit.entity.Entity entity){
        return ((CraftEntity) entity).getHandle();
    }
    public static LivingEntity toNmsEntity(org.bukkit.entity.LivingEntity entity){
        return ((CraftLivingEntity) entity).getHandle();
    }
    // WORLD
    public static ServerLevel toNmsWorld(World world){
        return ((CraftWorld) world).getHandle();
    }

}
