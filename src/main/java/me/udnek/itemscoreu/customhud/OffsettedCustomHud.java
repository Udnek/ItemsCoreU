package me.udnek.itemscoreu.customhud;

import me.udnek.itemscoreu.utils.ComponentU;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class OffsettedCustomHud implements CustomHud{

    protected Component text;

    public OffsettedCustomHud(int offset, Component text, int size){
        this.text = ComponentU.space(offset).append(text).append(ComponentU.space(-size-offset));
    }


    @Override
    public Component getText(Player player) {return text;}
}
