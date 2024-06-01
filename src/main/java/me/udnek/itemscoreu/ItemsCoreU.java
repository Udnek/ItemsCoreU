package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customentity.CustomEntityListener;
import me.udnek.itemscoreu.customentity.CustomEntityTicker;
import me.udnek.itemscoreu.customitem.CustomItemManager;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.utils.CraftListener;
import me.udnek.itemscoreu.customitem.utils.CustomItemGive;
import me.udnek.itemscoreu.customitem.utils.CustomItemListener;
import me.udnek.itemscoreu.nms.NMSHelper;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class ItemsCoreU extends JavaPlugin{
    private static JavaPlugin instance;
    private static CustomEntityTicker customEntityTicker;

    public static JavaPlugin getInstance(){
        return instance;
    }

    public static CustomEntityTicker getCustomEntityTicker() {return customEntityTicker;}

    public static MobEffect testEffect;

    @Override
    public void onEnable() {
        instance = this;

        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomEntityListener(this);

        this.getCommand("giveu").setExecutor(new CustomItemGive());

        customEntityTicker = new CustomEntityTicker();
        customEntityTicker.start(this);

        testEffect = NMSTest.registerEffect();

        // attribute = NMSTest.TEST_ATTRIBUTES();
/*        try {
            NMSTest.registerEntity();
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }*/

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){
                ItemsCoreU.this.getLogger().info("Recipe registration started");
                CustomItemManager.registerRecipes();
            }
        });
    }

    @Override
    public void onDisable() {
        customEntityTicker.stop();
    }
}
