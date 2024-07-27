package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customentity.CustomEntityCommand;
import me.udnek.itemscoreu.customentity.CustomEntityManager;
import me.udnek.itemscoreu.customhud.CustomHudTicker;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.CraftListener;
import me.udnek.itemscoreu.customitem.CustomItemCommand;
import me.udnek.itemscoreu.customitem.CustomItemListener;
import me.udnek.itemscoreu.customitem.CustomItemRegistry;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.merger.ResourcePackMerger;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import me.udnek.itemscoreu.utils.NMS.ProtocolTest;
import me.udnek.itemscoreu.utils.VanillaItemDisabler;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemsCoreU extends JavaPlugin implements ResourcePackablePlugin {
    private static JavaPlugin instance;
    private static CustomHudTicker customHudTicker;
    public static JavaPlugin getInstance(){
        return instance;
    }

    public void onEnable() {
        instance = this;

        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomBlockListener(this);
        VanillaItemDisabler.getInstance();

        this.getCommand("giveu").setExecutor(new CustomItemCommand());
        this.getCommand("summonu").setExecutor(new CustomEntityCommand());
        this.getCommand("resourcepacku").setExecutor(new ResourcePackCommand());

        CustomEntityManager.getInstance().start(this);

        customHudTicker = new CustomHudTicker();
        customHudTicker.start(this);

        NMSTest.registerAttribute("test", 0, 0, 8);
        MobEffect mobEffect = NMSTest.registerEffect();

        ProtocolTest.kek();

        SerializableDataManager.loadConfig();
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){
                LogUtils.pluginLog("Recipe registration started");
                CustomItemRegistry.registerRecipes();
                LogUtils.pluginLog("Vanilla disabler started");
                VanillaItemDisabler.getInstance().runDisabler();
            }
        });
    }


    @Override
    public void onDisable() {
        CustomEntityManager.getInstance().stop();
        customHudTicker.stop();
        SerializableDataManager.saveConfig();
    }
}
