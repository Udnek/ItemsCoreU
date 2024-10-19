package me.udnek.itemscoreu;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
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
import me.udnek.itemscoreu.nms.loot.ItemStackCreator;
import me.udnek.itemscoreu.nms.loot.entry.NmsCompositeLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.entry.NmsCustomLootEntryBuilder;
import me.udnek.itemscoreu.nms.loot.entry.NmsLootEntryContainer;
import me.udnek.itemscoreu.nms.loot.pool.NmsDefaultLootPoolContainer;
import me.udnek.itemscoreu.nms.loot.pool.NmsLootPoolBuilder;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.customadvancement.CustomAdvancementUtils;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import me.udnek.itemscoreu.utils.NMS.ProtocolTest;
import me.udnek.itemscoreu.utils.VanillaItemManager;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

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

                new BeforeVanillaManagerActivationEvent().callEvent();

                LogUtils.pluginLog("VanillaManager started");
                VanillaItemManager.getInstance().start();

                LogUtils.pluginLog("After-initialization started");
                for (CustomRegistry<?> registry : CustomRegistries.getRegistries()) {
                    for (Registrable registrable : registry.getAll()) {
                        registrable.afterInitialization();
                    }
                }

/*
                LootTable lootTable = Nms.get().getLootTable("minecraft:blocks/iron_ore");
                NmsDefaultLootPoolContainer ironPool = NmsDefaultLootPoolContainer.fromVanilla(lootTable, 0);
                NmsCustomLootEntryBuilder goldSubEntry = NmsCustomLootEntryBuilder.fromVanilla(
                        lootTable,
                        (ItemStackPredicate) itemStack -> itemStack.getType() == Material.RAW_IRON,
                        new ItemStackCreator.Material(Material.GOLD_INGOT));

                NmsCompositeLootEntryContainer ironEntry = (NmsCompositeLootEntryContainer) ironPool.getEntry(0);
                NmsCompositeLootEntryContainer goldEntry = ironEntry.copy();

                NmsCustomLootEntryBuilder ironOre = NmsCustomLootEntryBuilder.fromVanilla(
                        lootTable,
                        (ItemStackPredicate) itemStack -> itemStack.getType() == Material.RAW_IRON,
                        new ItemStackCreator.Material(Material.GOLD_INGOT));



                goldEntry.replaceChild(itemStack -> itemStack.getType() == Material.RAW_IRON, goldSubEntry);
                goldEntry.removeChild(itemStack -> itemStack.getType() == Material.IRON_ORE);
                Nms.get().addLootPool(lootTable, new NmsLootPoolBuilder(goldEntry));
*/


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
