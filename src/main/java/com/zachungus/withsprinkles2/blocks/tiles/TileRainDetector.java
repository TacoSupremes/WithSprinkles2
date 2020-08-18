package com.zachungus.withsprinkles2.blocks.tiles;

import com.zachungus.withsprinkles2.blocks.BlockRainDetector;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileRainDetector extends TileEntity implements ITickableTileEntity
{

    public TileRainDetector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    int ticks = 0;

    public TileRainDetector()
    {
        this(ModBlocks.TILE_RAIN_DETECT.get());
    }

    @Override
    public void tick()
    {
        ticks++;

        if (ticks % 20 == 0 && !this.getWorld().isRemote)
        {
            if(this.getBlockState().getBlock() instanceof BlockRainDetector)
            {
                ((BlockRainDetector)this.getBlockState().getBlock()).updatePower(this.getWorld(), this.getPos());
            }
        }
    }
}
