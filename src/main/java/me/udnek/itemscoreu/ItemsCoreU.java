package me.udnek.itemscoreu;

import io.papermc.paper.command.brigadier.argument.predicate.ItemStackPredicate;
import me.udnek.itemscoreu.customblock.CustomBlockListener;
import me.udnek.itemscoreu.customentity.CustomEntityCommand;
import me.udnek.itemscoreu.customentity.CustomEntityManager;
import me.udnek.itemscoreu.customhud.CustomHudTicker;
import me.udnek.itemscoreu.custominventory.CustomInventoryListener;
import me.udnek.itemscoreu.customitem.*;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.nms.CustomNmsLootPoolBuilder;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.nms.entry.SimpleNmsEntry;
import me.udnek.itemscoreu.resourcepack.ResourcePackCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.NMS.NMSTest;
import me.udnek.itemscoreu.utils.NMS.ProtocolTest;
import me.udnek.itemscoreu.utils.VanillaItemManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.storage.loot.LootPool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Predicate;

public final class ItemsCoreU extends JavaPlugin implements ResourcePackablePlugin {
    private static JavaPlugin instance;
    private static CustomHudTicker customHudTicker;
    public static JavaPlugin getInstance(){
        return instance;
    }
    public void onEnable() {
        instance = this;

        NMSTest.editItem();
        NMSTest.testEnchantment();

        // EVENTS
        new CustomItemListener(this);
        new CraftListener(this);
        new CustomInventoryListener(this);
        new CustomBlockListener(this);
        VanillaItemManager.getInstance();
        Bukkit.getPluginManager().registerEvents(new LootTableUtils(), this);
        RecipeManager.getInstance();

        // COMMANDS
        this.getCommand("giveu").setExecutor(new CustomItemCommand());
        this.getCommand("summonu").setExecutor(new CustomEntityCommand());
        this.getCommand("resourcepacku").setExecutor(new ResourcePackCommand());

        // TICKERS
        CustomEntityManager.getInstance().start(this);
        customHudTicker = new CustomHudTicker();
        customHudTicker.start(this);



        // TODO: 8/19/2024 REMOVE
        //NMSTest.registerAttribute("test", 0, 0, 8);
        MobEffect mobEffect = NMSTest.registerEffect();
        VanillaItemManager.getInstance().replaceItem(Material.LEATHER_BOOTS);
        ProtocolTest.kek();

        SerializableDataManager.loadConfig();
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
            public void run(){
                LogUtils.pluginLog("Recipe registration started");
                CustomItemRegistry.registerRecipes();

                LogUtils.pluginLog("Vanilla disabler started");
                VanillaItemManager.getInstance().start();
                Predicate<ItemStack> predicate = new Predicate<>() {
                    @Override
                    public boolean test(ItemStack itemStack) {
                        return !CustomItem.isCustom(itemStack) && itemStack.getType() == Material.ROTTEN_FLESH;
                    }
                };
                Nms.get().addLootPool(
                        LootTables.SHULKER.getLootTable(),
                        new CustomNmsLootPoolBuilder(
                                SimpleNmsEntry.fromVanilla(LootTables.ZOMBIE.getLootTable(), predicate, new ItemStack(Material.COMMAND_BLOCK)))
                                .rolls(1)
                );

                ItemStackPredicate predicate1 = itemStack -> !CustomItem.isCustom(itemStack) && itemStack.getType() == Material.BLAZE_ROD;
                Nms.get().replaceAllEntriesContains(
                        LootTables.BLAZE.getLootTable(),
                        predicate1,
                        SimpleNmsEntry.fromVanilla(LootTables.BLAZE.getLootTable(), predicate1, new ItemStack(Material.CHAIN_COMMAND_BLOCK))
                );
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
