package me.udnek.coreu.nms.loot.util;

import org.jetbrains.annotations.NotNull;

public interface NmsConditioned {
    @NotNull NmsLootConditionsContainer getConditions();
    void setConditions(@NotNull NmsLootConditionsContainer conditions);
}
