package me.udnek.itemscoreu.nms;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConsumableComponent {
    protected float consumeSeconds;
    protected ItemUseAnimation animation;
    protected Holder<SoundEvent> sound;
    protected boolean hasConsumeParticles;
    protected List<ConsumeEffect> onConsumeEffects;

    public ConsumableComponent(float consumeSeconds, ItemUseAnimation animation, Holder<SoundEvent> sound, boolean hasConsumeParticles, List<ConsumeEffect> onConsumeEffects) {
        this.consumeSeconds = consumeSeconds;
        this.animation = animation;
        this.sound = sound;
        this.hasConsumeParticles = hasConsumeParticles;
        this.onConsumeEffects = onConsumeEffects;
    }
    public ConsumableComponent(@NotNull Consumable nms){
        this(nms.consumeSeconds(), nms.animation(), nms.sound(), nms.hasConsumeParticles(), nms.onConsumeEffects());
    }
    public ConsumableComponent(){
        this(Consumable.builder().build());
    }

    public void setAnimation(@NotNull ConsumableAnimation animation){this.animation = animation.nms;}
    public void setConsumeSeconds(float seconds){this.consumeSeconds = seconds;}
    public void setConsumeTicks(int ticks){this.consumeSeconds = ticks/20f;}
    public void setHasConsumeParticles(boolean hasConsumeParticles){this.hasConsumeParticles = hasConsumeParticles;}
    public void clearConsumeEffects(){this.onConsumeEffects = new ArrayList<>();}
    public void setSound(@Nullable Sound newSound){
        if (newSound == null) newSound = Sound.INTENTIONALLY_EMPTY;
        sound = NmsUtils.getRegistry(Registries.SOUND_EVENT).get(NmsUtils.getResourceLocation(newSound.key())).get();
    }

    public @NotNull Consumable getNms(){
        return new Consumable(consumeSeconds, animation, sound, hasConsumeParticles, onConsumeEffects);
    }
}














