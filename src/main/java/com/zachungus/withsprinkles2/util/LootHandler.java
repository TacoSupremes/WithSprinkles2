package com.zachungus.withsprinkles2.util;

import com.google.common.collect.ImmutableList;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class LootHandler
{
    private static final List<String> TABLES = ImmutableList.of(
            "inject/abandoned_mineshaft", "inject/desert_pyramid",
            "inject/jungle_temple", "inject/simple_dungeon",
            "inject/nether_bridge", "inject/stronghold_corridor", "inject/stronghold_library",
             "inject/underwater_ruin_big", "inject/underwater_ruin_small",  "inject/village/village_temple"
            );

    private static List<String> COMPARE = new ArrayList<String>();

    public LootHandler()
    {
        for (String s : TABLES)
            COMPARE.add(s.replace("inject", "minecraft:chests"));
    }


    @SubscribeEvent
    public void lootLoad(LootTableLoadEvent evt)
    {
        if (COMPARE.contains(evt.getName().toString()))
            evt.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(LibMisc.ModID, TABLES.get(COMPARE.indexOf(evt.getName().toString()))))).name(LibMisc.ModID+"_inject").build());
    }
}
