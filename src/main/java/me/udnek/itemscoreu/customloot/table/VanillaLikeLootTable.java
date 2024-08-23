package me.udnek.itemscoreu.customloot.table;

import me.udnek.itemscoreu.customevent.LootTableGenerateEvent;

public interface VanillaLikeLootTable extends CustomLootTable{

    void onLootTableGenerateEvent(LootTableGenerateEvent event);

}
