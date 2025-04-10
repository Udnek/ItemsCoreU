package me.udnek.itemscoreu.customminigame.game;

import me.udnek.itemscoreu.customminigame.MGUManager;
import me.udnek.itemscoreu.customminigame.player.MGUPlayer;
import me.udnek.itemscoreu.customregistry.Registrable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MGUGameType extends Registrable {
    default boolean isPlayerInThisGame(@NotNull Player player){
        MGUPlayer mguPlayer = MGUManager.get().getPlayer(player);
        if (mguPlayer == null) return false;
        return mguPlayer.getGame().getType() == this;
    }
}
