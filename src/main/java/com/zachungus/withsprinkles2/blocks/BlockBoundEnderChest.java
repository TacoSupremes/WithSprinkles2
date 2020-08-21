package com.zachungus.withsprinkles2.blocks;

import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChest;
import com.zachungus.withsprinkles2.blocks.tiles.TileEnderHopper;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class BlockBoundEnderChest extends BlockMod
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    public static final TranslationTextComponent field_220115_d = new TranslationTextComponent("container.enderchest");

    public BlockBoundEnderChest()
    {
        super(Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).hardnessAndResistance(2.0F, 2F).sound(SoundType.STONE));
    }

    @Override
    public void onBlockPlacedBy(World w, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(w.getTileEntity(pos) instanceof TileBoundEnderChest)
        {
            if(placer instanceof PlayerEntity)
            {
                ((TileBoundEnderChest)w.getTileEntity(pos)).uuid =  ((PlayerEntity)placer).getUniqueID();
                ((TileBoundEnderChest)w.getTileEntity(pos)).name =  ((PlayerEntity)placer).getDisplayName().getString();

            }
        }
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileBoundEnderChest)
        {
            StringTextComponent s = new StringTextComponent(((TileBoundEnderChest)tileentity).name + "'s ");

            BlockPos blockpos = pos.up();
            if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos)) {
                return ActionResultType.SUCCESS;
            } else if (worldIn.isRemote) {

               // ((TileBoundEnderChest)tileentity).numPlayersUsing++;

                return ActionResultType.SUCCESS;
            } else {
                player.openContainer(new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
                    return ChestContainer.createGeneric9X3(p_226928_1_, p_226928_2_, ((TileBoundEnderChest)tileentity));
                }, s.appendSibling(field_220115_d)));

                player.addStat(Stats.OPEN_ENDERCHEST);
                return ActionResultType.SUCCESS;
            }
        } else {
            return ActionResultType.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public TileEntityMerger.ICallbackWrapper<? extends ChestTileEntity> func_225536_a_(BlockState p_225536_1_, World p_225536_2_, BlockPos p_225536_3_, boolean p_225536_4_) {
        return TileEntityMerger.ICallback::func_225537_b_;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        for(int i = 0; i < 3; ++i) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
            double d1 = (double)((float)pos.getY() + rand.nextFloat());
            double d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
            double d3 = (double)(rand.nextFloat() * (float)j);
            double d4 = ((double)rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = (double)(rand.nextFloat() * (float)k);
            worldIn.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }

    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return ModBlocks.TILE_BOUND_ENDER_CHEST.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean eventReceived(BlockState state, World w, BlockPos pos, int id, int param)
    {
            ((TileBoundEnderChest) w.getTileEntity(pos)).numPlayersUsing = param;
        return true;
    }

    @Override
    public String getName()
    {
        return "bound_ender_chest";
    }
}
