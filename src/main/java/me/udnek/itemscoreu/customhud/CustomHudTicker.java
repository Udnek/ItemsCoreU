package me.udnek.itemscoreu.customhud;

import me.udnek.itemscoreu.utils.TickingTask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomHudTicker extends TickingTask {
    @Override
    public int getDelay() {return 1;}

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            Component text = Component.empty();

            for (CustomHud customHud : CustomHudManager.getAllTickets().values()) {
                text =  text.append(customHud.getText(player));
            }

            player.sendActionBar(text);
        }
    }
}
