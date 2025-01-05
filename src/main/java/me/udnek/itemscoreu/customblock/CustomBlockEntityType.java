package me.udnek.itemscoreu.customblock;

import org.jetbrains.annotations.NotNull;

public interface CustomBlockEntityType<Block extends CustomBlockEntity> extends CustomBlockType{
     @NotNull Block createNewBlockClass();
}
