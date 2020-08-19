package com.zachungus.withsprinkles2.items;

import com.sun.corba.se.spi.ior.IORTemplate;
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
import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.ModID);

    public static final RegistryObject<Item> rock = ITEMS.register("rock2", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    // heavy boots sink instantly to bottom of water

    //xp tome to hold xp

    // lava saver item extinguish player from fire and build obsidian pillar to above lava when falling into lava
    // 4 blaze rods 4 obsidian 1 ender pearl play ender noise when teleport to top of pillar

    public static final RegistryObject<Item> LAVA_TOTEM = regItem(ItemLavaTotem::new);

    public static final RegistryObject<Item> LOST_PAGE = ITEMS.register("lost_page", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> PORTABLE_ENDER_CHEST = regItem(ItemEnderChest::new);

    public static final RegistryObject<Item> ROCK_BLOCK_ITEM = makeBlockItem(ModBlocks.ROCK);

    public static final RegistryObject<Item> RAIN_DETECT_ITEM = makeBlockItem(ModBlocks.RAIN_DETECTOR);

    public static RegistryObject<Item> regItem(final Supplier<? extends ItemMod> sup)
    {
        return ITEMS.register(sup.get().getItemRegistryName(), sup);
    }

    public static RegistryObject<Item> makeBlockItem(RegistryObject<BlockMod> b)
    {
        return ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), new Item.Properties().group(WithSprinkles2.TAB)));
    }
}
