package me.udnek.itemscoreu.utils.NMS;

import me.udnek.itemscoreu.nms.NMSHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;

public class TestEntity extends Display.BlockDisplay {

    public TestEntity(EntityType<?> entitytypes, Level world) {
        super(EntityType.BLOCK_DISPLAY, world);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:base_villager"); // <= this line of code
    }

    @Override
    public void tick() {
        if (tickCount % 40 != 0) return;
        BlockState blockState = level().getBlockState(blockPosition());
        Bukkit.getLogger().info(blockState.getBlock().toString());
        setBlockState(blockState);
    }
}
