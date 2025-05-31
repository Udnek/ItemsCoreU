package me.udnek.coreu.rpgu.component.ability.property;

import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.rpgu.component.RPGUComponentTypes;
import me.udnek.coreu.rpgu.component.ability.Ability;
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart;
import me.udnek.coreu.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CastTimeProperty implements AbilityProperty<Player, Integer> {

    protected int time;

    public CastTimeProperty(int base){
        this.time = base;
    }

    @Override
    public @NotNull Integer getBase() {
        return time;
    }

    @Override
    public @NotNull Integer get(@NotNull Player player) {
        return getBase();
    }

    @Override
    public void describe(@NotNull AbilityLorePart componentable) {
        Utils.consumeIfNotNull(getBase(), value ->
                componentable.addAbilityStat(Component.translatable("ability.rpgu.cast_time", Component.text(Utils.roundToTwoDigits(value/20d)))));
    }

    @Override
    public @NotNull CustomComponentType<Ability<?>, ?> getType() {
        return RPGUComponentTypes.ABILITY_CAST_TIME;
    }
}
