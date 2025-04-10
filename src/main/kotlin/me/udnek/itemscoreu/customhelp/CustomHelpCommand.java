package me.udnek.itemscoreu.customhelp;

import me.udnek.itemscoreu.ItemsCoreU;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomHelpCommand extends SelfRegisteringListener implements CommandExecutor {

    private final List<Component> lines = new ArrayList<>();

    private static CustomHelpCommand instance;
    private CustomHelpCommand(){
        super(ItemsCoreU.getInstance());
        String l = "-------------";
        lines.add(
                Component.text(l).color(NamedTextColor.YELLOW)
                        .append(Component.text(" Help ").color(NamedTextColor.WHITE))
                        .append(Component.text(l).color(NamedTextColor.YELLOW))
        );

    }
    public static CustomHelpCommand getInstance() {
        if (instance == null) instance = new CustomHelpCommand();
        return instance;
    }

    public void addLine(@Nullable Component component){
        if (component == null) component = Component.empty();
        lines.add(component);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        trigger(commandSender);
        return true;
    }

    public void trigger(CommandSender commandSender){
        lines.forEach(commandSender::sendMessage);
    }
}














