package me.udnek.coreu.custom.hud;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CustomHud {
    @NotNull Component getText(@NotNull Player player);
}
