package me.udnek.coreu.rpgu.component.ability.passive;

import me.udnek.coreu.rpgu.component.ability.AbstractAbility;
import me.udnek.coreu.rpgu.lore.AttributesLorePart;
import me.udnek.coreu.rpgu.lore.ability.PassiveAbilityLorePart;
import me.udnek.coreu.util.LoreBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class ConstructablePassiveAbility<ActivationContext> extends AbstractAbility<ActivationContext> implements PassiveAbility<ActivationContext> {


    public void addLoreLines(@NotNull PassiveAbilityLorePart componentable){
        getComponents().forEach(c -> c.describe(componentable));
    }

    @Override
    public void getLore(@NotNull LoreBuilder loreBuilder){
        LoreBuilder.Componentable componentable = loreBuilder.get(LoreBuilder.Position.ATTRIBUTES);
        PassiveAbilityLorePart lorePart;
        if (!(componentable instanceof AttributesLorePart attributesLorePart)){
            AttributesLorePart newPart = new AttributesLorePart();
            loreBuilder.set(LoreBuilder.Position.ATTRIBUTES, newPart);
            lorePart = newPart;
        } else {
            lorePart = attributesLorePart;
        }

        lorePart.setEquipmentSlot(getSlot());
        lorePart.setHeader(Component.translatable("passive_ability.rpgu.title"));
        addLoreLines(lorePart);
    }
}
