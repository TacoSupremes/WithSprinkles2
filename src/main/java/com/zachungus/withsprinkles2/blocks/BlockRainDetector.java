package com.zachungus.withsprinkles2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BlockRainDetector extends BlockMod
{
    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 8);
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    public BlockRainDetector()
    {
        super(Properties.create(Material.WOOD).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(POWER, 0));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModBlocks.TILE_RAIN_DETECT.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return blockState.get(POWER);
    }

    public static boolean canRain(World w, BlockPos pos)
    {
        return w.getBiome(pos).getPrecipitation() == Biome.RainType.RAIN;
    }

    public void updatePower(World w, BlockPos pos)
    {

        BlockState state = w.getBlockState(pos);

        if (w.isRaining() && canRain(w, pos) && w.canBlockSeeSky(pos))
        {
            if (w.isThundering())
            {
                if (state.get(POWER).intValue() != 8)
                    w.setBlockState(pos, state.with(POWER, 8), 3);
                return;
            }

            if (state.get(POWER).intValue() != 4)
            {

                w.setBlockState(pos, state.with(POWER, 4), 3);
                return;
            }

        }
        else if (state.get(POWER).intValue() != 0)
            w.setBlockState(pos, state.with(POWER, 0));
    }
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWER);
}

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos,  Direction side)
    {
        return true;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }


    @Override
    public String getName()
    {
        return "rain_detector";
    }

}
