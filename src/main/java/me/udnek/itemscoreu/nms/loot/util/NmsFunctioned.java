package me.udnek.itemscoreu.nms.loot.util;

import org.jetbrains.annotations.NotNull;

public interface NmsFunctioned {
    @NotNull NmsLootFunctionsContainer getFunctions();
    void setFunctions(@NotNull NmsLootFunctionsContainer functions);
}
