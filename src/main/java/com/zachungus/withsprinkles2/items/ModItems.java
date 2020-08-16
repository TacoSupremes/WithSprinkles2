package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.WithSprinkles2;
import com.zachungus.withsprinkles2.blocks.BlockMod;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.ModID);

    public static final RegistryObject<Item> rock = ITEMS.register("rock2", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> ROCK_BLOCK_ITEM = makeBlockItem(ModBlocks.ROCK);



    public static RegistryObject<Item> makeBlockItem(RegistryObject<BlockMod> b)
    {
        return ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), new Item.Properties().group(WithSprinkles2.TAB)));
    }
}
