package com.zachungus.withsprinkles2.blocks;

import com.zachungus.withsprinkles2.WithSprinkles2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.apache.logging.log4j.Level;

public class TestBlock extends BlockMod
{
    public TestBlock()
    {
        super(Properties.create(Material.ROCK).notSolid());

    }

    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }


    public String getName()
    {
        return "rock";
    }
}
