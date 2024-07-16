package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customentity.CustomEntityListener;
import me.udnek.itemscoreu.customentity.CustomEntityTicker;
import me.udnek.itemscoreu.customevent.AllBukkitEventListener;
import me.udnek.itemscoreu.customhud.CustomHudTicker;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.CraftListener;
import me.udnek.itemscoreu.customitem.CustomItemCommand;
import me.udnek.itemscoreu.customitem.CustomItemListener;
import me.udnek.itemscoreu.customitem.CustomItemManager;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemsCoreU extends JavaPlugin{
    private static JavaPlugin instance;
    private static CustomEntityTicker customEntityTicker;
    private static CustomHudTicker customHudTicker;

    public static JavaPlugin getInstance(){
        return instance;
    }

    public static CustomEntityTicker getCustomEntityTicker() {return customEntityTicker;}
    public void onEnable() {
        instance = this;

        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomEntityListener(this);
        new AllBukkitEventListener(this);
        new CustomBlockListener(this);

        this.getCommand("giveu").setExecutor(new CustomItemCommand());

        customEntityTicker = new CustomEntityTicker();
        customEntityTicker.start(this);
        customHudTicker = new CustomHudTicker();
        customHudTicker.start(this);


        NMSTest.registerAttribute("test", 0, 0, 8);
        MobEffect mobEffect = NMSTest.registerEffect();

        SerializableDataManager.loadConfig();

        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){
                LogUtils.pluginLog("Recipe registration started");
                CustomItemManager.registerRecipes();
            }
        });
    }


    @Override
    public void onDisable() {
        customEntityTicker.stop();
        customHudTicker.stop();
        SerializableDataManager.saveConfig();
    }
}
