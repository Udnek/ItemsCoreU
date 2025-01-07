package me.udnek.itemscoreu.customblock.type;

import me.udnek.itemscoreu.customblock.block.CustomBlockEntity;
import org.jetbrains.annotations.NotNull;

public interface CustomBlockEntityType<Block extends CustomBlockEntity> extends CustomBlockType {
     @NotNull Block createNewBlockClass();
}
