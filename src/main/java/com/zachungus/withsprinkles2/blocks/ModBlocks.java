package com.zachungus.withsprinkles2.blocks;

import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LibMisc.ModID);

    //public static final RegistryObject<Block> TEST_BLOCK = regBlock(TestBlock::new);

   // public static final RegistryObject<Block> ROCK = BLOCKS.register("rock", () -> new Block(Block.Properties.create(Material.ROCK)));

    public static final RegistryObject<BlockMod> ROCK = regBlock(TestBlock::new);

    public static final RegistryObject<BlockMod> RAIN_DETECTOR = regBlock(BlockRainDetector::new);

    //public static final RegistryObject<Block> TEST_BLOCK = regBlock(TestBlock::new);

   // public static List<RegistryObject<BlockMod>> blocks = new ArrayList<RegistryObject<BlockMod>>();

    //public static List<String> blockNames = new ArrayList<String>();

    public static RegistryObject<BlockMod> regBlock(final Supplier<? extends BlockMod> sup)
    {
        /*
        if(blocks == null)
        {
            blocks = new ArrayList<RegistryObject<BlockMod>>();
            blockNames = new ArrayList<String>();
        }
*/
       return  BLOCKS.register(sup.get().getName(), sup);

      //  blockNames.add(sup.get().getName());

       // System.out.println();

     //   return blocks.get(blocks.size() - 1);
    }


}
