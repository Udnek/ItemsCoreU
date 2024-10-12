package me.udnek.itemscoreu.customadvancement;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplayBuilder;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import me.udnek.itemscoreu.ItemsCoreU;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adv {



    public static void run(){


        UltimateAdvancementAPI api = UltimateAdvancementAPI.getInstance(ItemsCoreU.getInstance());
        AdvancementTab tab = api.createAdvancementTab("your_tab_name");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.GRASS_BLOCK, "Example", AdvancementFrameType.TASK, true, true, 0, 0, "attribute.name.generic.armor", "description.");
        RootAdvancement root = new RootAdvancement(tab, "root", rootDisplay, "textures/block/stone.png");


        List<BaseAdvancement> advancements = new ArrayList<>();

        advancements.add(new TestAdv("test1", new AdvancementDisplay(Material.BASALT, "attribute.name.generic.armor", AdvancementFrameType.TASK, true, true, 1, 0), root));
        advancements.add(new TestAdv("test2", new AdvancementDisplay(Material.ACACIA_BOAT, "TEST2", AdvancementFrameType.TASK, true, true, 2, 0), advancements.get(0)));
        advancements.add(new TestAdv("test3", new AdvancementDisplay(Material.DEAD_BRAIN_CORAL, "TEST3", AdvancementFrameType.TASK, true, true, 3, 0), advancements.get(1)));


        tab.registerAdvancements(root, new HashSet<>(advancements));

    }

    public static class TestAdv extends BaseAdvancement{
        public TestAdv(@NotNull String key, @NotNull AdvancementDisplay display, @NotNull Advancement parent) {
            super(key, display, parent);
        }
    }

}
