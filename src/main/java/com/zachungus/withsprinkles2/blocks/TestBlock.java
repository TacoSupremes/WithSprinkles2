package com.zachungus.withsprinkles2.blocks;

import com.zachungus.withsprinkles2.WithSprinkles2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.apache.logging.log4j.Level;

public class TestBlock extends BlockMod
{
    public TestBlock()
    {
        super(Properties.create(Material.ROCK));

    }

    public String getName()
    {
        return "rock";
    }
}
