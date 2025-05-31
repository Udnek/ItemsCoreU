package me.udnek.coreu.rpgu.component.ability.active;

import me.udnek.coreu.rpgu.component.ability.AbstractAbility;
import me.udnek.coreu.rpgu.lore.ability.ActiveAbilityLorePart;
import me.udnek.coreu.util.LoreBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructableActiveAbility<ActivationContext> extends AbstractAbility<ActivationContext> implements ActiveAbility<ActivationContext>{

    public void addLoreLines(@NotNull ActiveAbilityLorePart componentable){
        getComponents().forEach(c -> c.describe(componentable));
    }


    @Override
    public void getLore(@NotNull LoreBuilder loreBuilder){
        ActiveAbilityLorePart lorePart = new ActiveAbilityLorePart();
        loreBuilder.set(55, lorePart);
        lorePart.setHeader(Component.translatable("active_ability.rpgu.title"));
        addLoreLines(lorePart);
    }
}
