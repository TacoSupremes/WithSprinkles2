package com.zachungus.withsprinkles2.items;

import com.sun.corba.se.spi.ior.IORTemplate;
import com.zachungus.withsprinkles2.WithSprinkles2;
import com.zachungus.withsprinkles2.blocks.BlockMod;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.blocks.tiles.ItemBoundEnderChestRender;
import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChest;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.ModID);

   // public static final RegistryObject<Item> rock = ITEMS.register("rock2", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    // heavy boots sink instantly to bottom of water

    //xp tome to hold xp

    // 4 blaze rods 4 obsidian 1 netherwart

    public static final RegistryObject<Item> LAVA_TOTEM = regItem(ItemLavaTotem::new);

    public static final RegistryObject<Item> LOST_PAGE = ITEMS.register("lost_page", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> PORTABLE_ENDER_CHEST = regItem(ItemEnderChest::new);

    public static final RegistryObject<Item> PORTABLE_BOUND_ENDER_CHEST = regItem(ItemBoundEnderChest::new);

    public static final RegistryObject<Item> RAIN_DETECT_ITEM = makeBlockItem(ModBlocks.RAIN_DETECTOR);

    public static final RegistryObject<Item> AUTO_DROPPER_ITEM = makeBlockItem(ModBlocks.AUTO_DROPPER);

    public static final RegistryObject<Item> AUTO_DISPENSER_ITEM = ITEMS.register("auto_dispenser", () -> new BlockItem(ModBlocks.AUTO_DISPENSER.get(), new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> ENDER_HOPPER_ITEM = makeBlockItem(ModBlocks.ENDER_HOPPER);

    public static final RegistryObject<Item> BOUND_ENDER_CHEST_ITEM = ITEMS.register("bound_ender_chest", () -> new BlockItem(ModBlocks.BOUND_ENDER_CHEST.get(), new Item.Properties().group(WithSprinkles2.TAB).setISTER(()-> ironChestRenderer())));


   // public static final RegistryObject<Item> BOUND_ENDER_CHEST_ITEM = makeBlockItem(ModBlocks.BOUND_ENDER_CHEST);

    public static RegistryObject<Item> regItem(final Supplier<? extends ItemMod> sup)
    {
        return ITEMS.register(sup.get().getItemRegistryName(), sup);
    }

    public static RegistryObject<Item> makeBlockItem(RegistryObject<BlockMod> b)
    {
        return ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), new Item.Properties().group(WithSprinkles2.TAB)));
    }

    public static RegistryObject<Item> makeBlockItem(RegistryObject<BlockMod> b, Item.Properties props)
    {
        return ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), props.group(WithSprinkles2.TAB)));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> ironChestRenderer() {
        return () -> new ItemBoundEnderChestRender(TileBoundEnderChest::new);
    }
}
