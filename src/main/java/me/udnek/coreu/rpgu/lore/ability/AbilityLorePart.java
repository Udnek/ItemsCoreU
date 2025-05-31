package me.udnek.coreu.rpgu.lore.ability;

import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.util.LoreBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public interface AbilityLorePart extends LoreBuilder.Componentable{

    TextColor ACTIVE_HEADER_COLOR = NamedTextColor.GREEN;
    TextColor PASSIVE_HEADER_COLOR = NamedTextColor.YELLOW;

    TextColor ACTIVE_STATS_COLOR = NamedTextColor.GRAY;
    TextColor PASSIVE_STATS_COLOR = NamedTextColor.GRAY;

    TextColor ACTIVE_DESCRIPTION_COLOR = NamedTextColor.BLUE;
    TextColor PASSIVE_DESCRIPTION_COLOR = NamedTextColor.BLUE;


    void setHeader(@NotNull Component component);

    void addAbilityStat(@NotNull Component component);
    void addAbilityStatDoubleTab(@NotNull Component component);

    default void addFullAbilityDescription(@NotNull CustomItem customItem, int linesAmount){
        for (int i = 0; i < linesAmount; i++) addAbilityDescription(customItem, i);
    }
    default void addAbilityDescription(@NotNull CustomItem customItem, int line){
        addAbilityDescription(customItem.translationKey(), line);
    }
    void addAbilityDescription(@NotNull String rawItemName, int line);
    void addAbilityDescription(@NotNull Component component);

    void addWithAbilityFormat(@NotNull Component component);
}
