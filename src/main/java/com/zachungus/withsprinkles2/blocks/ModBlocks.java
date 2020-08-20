package com.zachungus.withsprinkles2.blocks;

import com.zachungus.withsprinkles2.blocks.tiles.TileAutoDispenser;
import com.zachungus.withsprinkles2.blocks.tiles.TileAutoDropper;
import com.zachungus.withsprinkles2.blocks.tiles.TileEnderHopper;
import com.zachungus.withsprinkles2.blocks.tiles.TileRainDetector;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LibMisc.ModID);

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LibMisc.ModID);

    // double hopper splits stacks round robin between 2 inventories

    // blocks
    public static final RegistryObject<BlockMod> RAIN_DETECTOR = regBlock(BlockRainDetector::new);

    public static final RegistryObject<BlockMod> AUTO_DROPPER = regBlock(BlockAutoDropper::new);

    public static final RegistryObject<Block> AUTO_DISPENSER = BLOCKS.register("auto_dispenser", BlockAutoDispenser::new);

    public static final RegistryObject<BlockMod> ENDER_HOPPER = regBlock(BlockEnderHopper::new);


    //tiles
    public static final RegistryObject<TileEntityType<?>> TILE_RAIN_DETECT = regTile(TileRainDetector::new, ModBlocks.RAIN_DETECTOR);

    public static final RegistryObject<TileEntityType<?>> TILE_AUTO_DROPPER = regTile(TileAutoDropper::new, ModBlocks.AUTO_DROPPER);

    public static final RegistryObject<TileEntityType<?>> TILE_AUTO_DISPENSER = TILES.register("auto_dispenser", () -> TileEntityType.Builder.create(TileAutoDispenser::new, ModBlocks.AUTO_DISPENSER.get()).build(null));

    public static final RegistryObject<TileEntityType<?>> TILE_ENDER_HOPPER = regTile(TileEnderHopper::new, ModBlocks.ENDER_HOPPER);

    public static RegistryObject<TileEntityType<?>> regTile(final Supplier<? extends TileEntity> sup, RegistryObject<BlockMod> bm)
    {
        return TILES.register(bm.getId().getPath(), () -> TileEntityType.Builder.create(sup, bm.get()).build(null));
    }

    public static RegistryObject<BlockMod> regBlock(final Supplier<? extends BlockMod> sup)
    {
       return  BLOCKS.register(sup.get().getName(), sup);
    }
}
