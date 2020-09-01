package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.WithSprinkles2;
import com.zachungus.withsprinkles2.blocks.BlockMod;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.blocks.tiles.ItemBoundEnderChestRender;
import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChest;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LibMisc.ModID);

    public static final RegistryObject<Item> LAVA_TOTEM = regItem(ItemLavaTotem::new);

    public static final RegistryObject<Item> LOST_PAGE = ITEMS.register("lost_page", () -> new Item(new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> PORTABLE_ENDER_CHEST = regItem(ItemEnderChest::new);

    public static final RegistryObject<Item> PORTABLE_BOUND_ENDER_CHEST = regItem(ItemBoundEnderChest::new);

    public static final RegistryObject<Item> RAIN_DETECT_ITEM = makeBlockItem(ModBlocks.RAIN_DETECTOR);

    public static final RegistryObject<Item> AUTO_DROPPER_ITEM = makeBlockItem(ModBlocks.AUTO_DROPPER);

    public static final RegistryObject<Item> AUTO_DISPENSER_ITEM = ITEMS.register("auto_dispenser", () -> new BlockItem(ModBlocks.AUTO_DISPENSER.get(), new Item.Properties().group(WithSprinkles2.TAB)));

    public static final RegistryObject<Item> ENDER_HOPPER_ITEM = makeBlockItem(ModBlocks.ENDER_HOPPER);

    public static final RegistryObject<Item> BOUND_ENDER_CHEST_ITEM = ITEMS.register("bound_ender_chest", () -> new BlockItem(ModBlocks.BOUND_ENDER_CHEST.get(), new Item.Properties().group(WithSprinkles2.TAB).setISTER(() -> boundChestRenderer())));

    public static RegistryObject<Item> regItem(final Supplier<? extends Item> sup)
    {
        return ITEMS.register(((IModItem)sup.get()).getItemRegistryName(), sup);
    }

    public static RegistryObject<Item> makeBlockItem(RegistryObject<BlockMod> b)
    {
        return ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), new Item.Properties().group(WithSprinkles2.TAB)));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> boundChestRenderer()
    {
        return () -> new ItemBoundEnderChestRender(TileBoundEnderChest::new);
    }
}
