package com.zachungus.withsprinkles2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockAutoDispenser extends DispenserBlock
{
    protected BlockAutoDispenser()
    {
        super(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).hardnessAndResistance(2.0F, 2F).sound(SoundType.STONE));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModBlocks.TILE_AUTO_DISPENSER.get().create();
    }

    public IDispenseItemBehavior getBehavior2(ItemStack is)
    {
        return getBehavior(is);
    }


}
