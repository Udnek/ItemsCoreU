package me.udnek.itemscoreu.customhud;

import me.udnek.itemscoreu.util.ComponentU;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OffsettedCustomHud implements CustomHud{

    protected Component text;

    public OffsettedCustomHud(int offset, @NotNull Component text, int size){
        this.text = ComponentU.space(offset).append(text).append(ComponentU.space(-size-offset));
    }


    @Override
    public @NotNull Component getText(@NotNull Player player) {return text;}
}
