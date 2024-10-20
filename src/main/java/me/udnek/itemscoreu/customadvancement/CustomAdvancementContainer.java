package me.udnek.itemscoreu.customadvancement;

import net.minecraft.advancements.AdvancementHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomAdvancementContainer {
    @NotNull AdvancementHolder get();
    @Nullable CustomAdvancementDisplayBuilder getDisplay();
}
