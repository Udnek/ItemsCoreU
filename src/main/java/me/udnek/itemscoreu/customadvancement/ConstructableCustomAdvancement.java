package me.udnek.itemscoreu.customadvancement;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.minecraft.advancements.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class ConstructableCustomAdvancement implements CustomAdvancementContainer {
    private boolean registered = false;
    @Nullable CustomAdvancementContainer parent;
    private AdvancementHolder itself;
    @NotNull Key key;
    @Nullable CustomAdvancementDisplayBuilder display;

    @NotNull Map<String, Criterion<?>> criteria = new HashMap<>();
    @NotNull AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;

    public ConstructableCustomAdvancement(@NotNull Key key){
        this.key = key;
    }
    public @NotNull ConstructableCustomAdvancement key(@NotNull Key key){
        this.key = key; return this;
    }
    public @NotNull ConstructableCustomAdvancement display(@Nullable CustomAdvancementDisplayBuilder display){
        this.display = display; return this;
    }
    public @NotNull ConstructableCustomAdvancement parent(@Nullable CustomAdvancementContainer parent){
        this.parent = parent; return this;
    }
    public @NotNull ConstructableCustomAdvancement addCriterion(@NotNull AdvancementCriterion criterion){
        return addCriterion(Integer.toString(criteria.size()), criterion);
    }
    public @NotNull ConstructableCustomAdvancement addCriterion(@NotNull String name, @NotNull AdvancementCriterion criterion){
        criteria.put(name, criterion.get()); return this;
    }
    public @NotNull ConstructableCustomAdvancement requirementsStrategy(@NotNull RequirementsStrategy strategy){
        this.requirementsStrategy = strategy.get(); return this;
    }
    @Override
    public @Nullable CustomAdvancementDisplayBuilder getDisplay() {
        return display;
    }

    @Override
    public @NotNull AdvancementHolder get(){
        if (itself == null){
            Advancement.Builder builder = new Advancement.Builder();
            builder.display(display == null ? null : display.build());
            if (parent != null) builder.parent(parent.get());
            builder.requirements(requirementsStrategy);
            if (criteria.isEmpty()) addCriterion("auto_added_by_builder", AdvancementCriterion.IMPOSSIBLE);
            criteria.forEach(builder::addCriterion);

            itself = builder.build(ResourceLocation.parse(key.asString()));
        }

        return itself;
    }


    public void register(){
        Preconditions.checkArgument(!registered, "Advancement already registered!");
        CustomAdvancementUtils.register(this);
        registered = true;
    }

    public enum RequirementsStrategy implements Supplier<AdvancementRequirements.Strategy> {
        AND(AdvancementRequirements.Strategy.AND),
        OR(AdvancementRequirements.Strategy.OR);

        private final AdvancementRequirements.Strategy strategy;
        RequirementsStrategy(AdvancementRequirements.Strategy strategy){
            this.strategy = strategy;
        }
        @Override
        public AdvancementRequirements.Strategy get() {
            return strategy;
        }
    }
}