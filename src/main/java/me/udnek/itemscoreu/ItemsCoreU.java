package me.udnek.itemscoreu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import me.udnek.itemscoreu.customentity.CustomEntityListener;
import me.udnek.itemscoreu.customentity.CustomEntityTicker;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.CustomItemManager;
import me.udnek.itemscoreu.customitem.CraftListener;
import me.udnek.itemscoreu.customitem.CustomItemCommand;
import me.udnek.itemscoreu.customitem.CustomItemListener;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import me.udnek.itemscoreu.utils.NMS.ProtocolTest;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemsCoreU extends JavaPlugin{
    private static JavaPlugin instance;
    private static CustomEntityTicker customEntityTicker;

    public static JavaPlugin getInstance(){
        return instance;
    }

    public static CustomEntityTicker getCustomEntityTicker() {return customEntityTicker;}

    @Override
    public void onEnable() {
        instance = this;

        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomEntityListener(this);

        this.getCommand("giveu").setExecutor(new CustomItemCommand());

        customEntityTicker = new CustomEntityTicker();
        customEntityTicker.start(this);

        NMSTest.registerAttribute("test", 0, 0, 8);
        MobEffect mobEffect = NMSTest.registerEffect();



        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){
                LogUtils.pluginLog("Recipe registration started");
                CustomItemManager.registerRecipes();
            }
        });
    }

/*    @Override
    public void onLoad() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new ProtocolTest(
                        this,
                        ListenerPriority.NORMAL,
                        PacketType.Play.Server.UPDATE_ATTRIBUTES,
                        PacketType.Play.Server.ENTITY_METADATA
                        )
        );
    }*/

    @Override
    public void onDisable() {
        customEntityTicker.stop();
    }
}
