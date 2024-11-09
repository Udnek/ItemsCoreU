package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customattribute.ClearAttributeCommand;
import me.udnek.itemscoreu.customattribute.CustomAttributeCommand;
import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customentity.CustomEntityCommand;
import me.udnek.itemscoreu.customentity.CustomEntityManager;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customhelp.CustomHelpCommand;
import me.udnek.itemscoreu.customhud.CustomHudTicker;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.CraftListener;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemCommand;
import me.udnek.itemscoreu.customitem.CustomItemListener;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.customregistry.CustomRegistry;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.loot.entry.NmsCustomLootEntryBuilder;
import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolBuilder;
import me.udnek.itemscoreu.nms.loot.util.ItemStackCreator;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.util.InitializationProcess;
import me.udnek.itemscoreu.util.NMS.NMSTest;
import me.udnek.itemscoreu.util.VanillaItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemsCoreU extends JavaPlugin implements ResourcePackablePlugin {
    private static JavaPlugin instance;
    private static CustomHudTicker customHudTicker;


    public static JavaPlugin getInstance(){
        return instance;
    }
    public void onEnable() {
        instance = this;

        CustomRegistry<CustomItem> item = CustomRegistries.ITEM;
        CustomRegistry<CustomBlock> block = CustomRegistries.BLOCK;
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

        // TICKERS
        CustomEntityManager.getInstance().start(this);
        customHudTicker = new CustomHudTicker();
        customHudTicker.start(this);


        //NMSTest.editItem();
        //NMSTest.testEnchantment();
        //NMSTest.editEnchantment();
        
        // TODO: 8/19/2024 REMOVE
        //NMSTest.registerAttribute("test", 0, 0, 8);
        //MobEffect mobEffect = NMSTest.registerEffect();
        //ProtocolTest.kek();

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
        customHudTicker.stop();
        SerializableDataManager.saveConfig();
    }
}
