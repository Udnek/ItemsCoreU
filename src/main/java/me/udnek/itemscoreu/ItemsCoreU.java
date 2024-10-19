package me.udnek.itemscoreu;

import me.udnek.itemscoreu.customblock.CustomBlock;
import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customentity.CustomEntityCommand;
import me.udnek.itemscoreu.customentity.CustomEntityManager;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customevent.BeforeVanillaManagerActivationEvent;
import me.udnek.itemscoreu.customevent.GlobalInitializationEndEvent;
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
import me.udnek.itemscoreu.customregistry.Registrable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.nms.loot.entry.*;
import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolBuilder;
import me.udnek.itemscoreu.nms.loot.table.NmsLootTableContainer;
import me.udnek.itemscoreu.nms.loot.util.ItemStackCreator;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import me.udnek.itemscoreu.utils.VanillaItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.loot.LootTable;
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

        // TICKERS
        CustomEntityManager.getInstance().start(this);
        customHudTicker = new CustomHudTicker();
        customHudTicker.start(this);




        NMSTest.editItem();
        NMSTest.testEnchantment();
        //VanillaItemManager.getInstance().replaceVanillaMaterial(Material.LEATHER_BOOTS);
        
        // TODO: 8/19/2024 REMOVE
        //NMSTest.registerAttribute("test", 0, 0, 8);
        //MobEffect mobEffect = NMSTest.registerEffect();
        //ProtocolTest.kek();

/*        Optional<net.minecraft.world.level.storage.loot.LootTable> result = LootDataType.TABLE.codec().parse().result();


        RegistryAccess.Frozen var3 = var0.getAccessForLoading(RegistryLayer.RELOADABLE);
        RegistryOps<JsonElement> var4 = (new ReloadableServerRegistries.EmptyTagLookupWrapper(var3)).createSerializationContext(JsonOps.INSTANCE)*/


        SerializableDataManager.loadConfig();
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){



                LogUtils.pluginLog("After-initialization started");
                for (CustomRegistry<?> registry : CustomRegistries.getRegistries()) {
                    for (Registrable registrable : registry.getAll()) {
                        registrable.afterInitialization();
                    }
                }

                new BeforeVanillaManagerActivationEvent().callEvent();

                LogUtils.pluginLog("VanillaManager started");
                VanillaItemManager.getInstance().start();

/*                LootTable bukkitLootTable = Nms.get().getLootTable("minecraft:blocks/iron_ore");
                NmsLootTableContainer oreLootTable = Nms.get().getLootTableContainer(bukkitLootTable);

                NmsCompositeEntryContainer entry = (NmsCompositeEntryContainer) oreLootTable.getPool(0).getEntry(0);

                // GETTING ORE BLOCK
                //NmsLootEntryContainer oreBlock = entry.getChild(0);
                // GETTING IRON
                NmsSingletonEntryContainer rawIron = (NmsSingletonEntryContainer) entry.getChild(1);
                NmsUtils.getPossibleLoot(((NmsSingletonEntryContainer) entry.getChild(0)).get(), System.out::println);
                NmsUtils.getPossibleLoot(((NmsSingletonEntryContainer) entry.getChild(1)).get(), System.out::println);
                // CREATING GOLD
                NmsCustomLootEntryBuilder rawGold = new NmsCustomLootEntryBuilder(new ItemStackCreator.Material(Material.RAW_GOLD));
                rawGold.setConditions(rawIron.getConditions());
                rawGold.setFunctions(rawIron.getFunctions());
                // MERGING GOLD AND IRON IN LOOTTABLE
                NmsLootTableContainer newSubLootTable = oreLootTable.copy();
                newSubLootTable.removePool(0);
                newSubLootTable.addPool(new NmsLootPoolBuilder(rawIron));
                newSubLootTable.addPool(new NmsLootPoolBuilder(rawGold));

                // REMOVING OLD RAW IRON
                entry.removeChild(1);
                entry.addChild(NmsNestedEntryContainer.newFrom(newSubLootTable));*/


/*                NmsDefaultLootPoolContainer ironPool = NmsDefaultLootPoolContainer.fromVanilla(lootTable, 0);
                NmsCompositeLootEntryContainer ironEntry = (NmsCompositeLootEntryContainer) ironPool.getEntry(0);
                NmsCompositeLootEntryContainer goldEntry = ironEntry.copy();

                NmsCustomLootEntryBuilder goldItemEntry = NmsCustomLootEntryBuilder.fromVanilla(
                        lootTable,
                        (ItemStackPredicate) itemStack -> itemStack.getType() == Material.RAW_IRON,
                        new ItemStackCreator.Material(Material.GOLD_INGOT));

                NmsCustomLootEntryBuilder ironOre = NmsCustomLootEntryBuilder.fromVanilla(
                        lootTable,
                        (ItemStackPredicate) itemStack -> itemStack.getType() == Material.RAW_IRON,
                        new ItemStackCreator.Material(Material.GOLD_INGOT));



                goldEntry.replaceChild(itemStack -> itemStack.getType() == Material.RAW_IRON, goldItemEntry);
                goldEntry.removeChild(itemStack -> itemStack.getType() == Material.IRON_ORE);
                Nms.get().addLootPool(lootTable, new NmsLootPoolBuilder(goldEntry));*/


                new GlobalInitializationEndEvent().callEvent();
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
