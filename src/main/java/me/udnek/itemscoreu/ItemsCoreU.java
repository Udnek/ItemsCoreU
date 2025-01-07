package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customattribute.ClearAttributeCommand;
import me.udnek.itemscoreu.customattribute.CustomAttributeCommand;
import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customblock.type.CustomBlockType;
import me.udnek.itemscoreu.customentity.CustomEntityCommand;
import me.udnek.itemscoreu.customentity.CustomEntityManager;
import me.udnek.itemscoreu.customentity.CustomLoadedEntitiesCommand;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customhelp.CustomHelpCommand;
import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.CraftListener;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemCommand;
import me.udnek.itemscoreu.customitem.CustomItemListener;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.CustomRegistry;
import me.udnek.itemscoreu.nms.PacketHandler;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.customregistry.InitializationProcess;
import me.udnek.itemscoreu.customitem.VanillaItemManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemsCoreU extends JavaPlugin implements ResourcePackablePlugin {
    private static Plugin instance;


    public static Plugin getInstance(){
        return instance;
    }

    public void onEnable() {
        instance = this;

        CustomRegistry<CustomRegistry<?>> registry = CustomRegistries.REGISTRY;
        CustomRegistry<CustomItem> item = CustomRegistries.ITEM;
        CustomRegistry<CustomBlockType> block = CustomRegistries.BLOCK_TYPE;
        CustomRegistry<CustomEquipmentSlot> equipmentSlot = CustomRegistries.EQUIPMENT_SLOT;

        // EVENTS
        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomBlockListener(this);
        VanillaItemManager.getInstance();
        Bukkit.getPluginManager().registerEvents(new LootTableUtils(), this);
        RecipeManager.getInstance();

        // COMMANDS
        getCommand("giveu").setExecutor(new CustomItemCommand());
        getCommand("summonu").setExecutor(new CustomEntityCommand());
        getCommand("resourcepacku").setExecutor(new ResourcePackCommand());
        getCommand("helpu").setExecutor(CustomHelpCommand.getInstance());
        getCommand("attributeu").setExecutor(new CustomAttributeCommand());
        getCommand("clear_attribute_modifiers").setExecutor(new ClearAttributeCommand());
        getCommand("custom_entities").setExecutor(new CustomLoadedEntitiesCommand());

        // TICKERS
        CustomEntityManager.getInstance().start(this);
        CustomHudManager.getInstance().start(this);

        PacketHandler.initialize();

        SerializableDataManager.loadConfig();
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run() {
                InitializationProcess.start();
            }
        });
    }


    @Override
    public void onDisable() {
        CustomEntityManager.getInstance().stop();
        CustomHudManager.getInstance().stop();
        SerializableDataManager.saveConfig();
    }
}
