package me.udnek.itemscoreu.custom.minigame.game;

import me.udnek.itemscoreu.custom.minigame.MGUManager;
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer;
import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MGUGameType extends Registrable {
    default boolean isPlayerInThisGame(@NotNull Player player){
        return getIfPlayerInThisGame(player) != null;
    }
    default <T extends MGUPlayer> @Nullable T getIfPlayerInThisGame(@NotNull Player player){
        MGUPlayer mguPlayer = MGUManager.get().getPlayer(player);
        if (mguPlayer == null) return null;
        if (mguPlayer.getGame().getType() == this) return (T) mguPlayer;
        return null;
    }
}
