package com.zachungus.withsprinkles2.blocks.tiles;

import com.zachungus.withsprinkles2.blocks.BlockAutoDispenser;
import com.zachungus.withsprinkles2.blocks.BlockAutoDropper;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class TileAutoDispenser extends TileSimpleInventory implements ITickableTileEntity
{
    public TileAutoDispenser()
    {
        super(ModBlocks.TILE_AUTO_DISPENSER.get());
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    protected void dispense(World worldIn, BlockPos pos) {
        ProxyBlockSource proxyblocksource = new ProxyBlockSource(worldIn, pos);

       // int i = getStackInSlot(0).isEmpty() ? -1 : 0;

        if (!getStackInSlot(0).isEmpty())
        {
            ItemStack itemstack = getStackInSlot(0);
            IDispenseItemBehavior idispenseitembehavior = ((BlockAutoDispenser)this.getBlockState().getBlock()).getBehavior2(itemstack);
            if (idispenseitembehavior != IDispenseItemBehavior.NOOP) {
                setInventorySlotContents(0, idispenseitembehavior.dispense(proxyblocksource, itemstack));
            }

        }
    }


    @Override
    public void tick()
    {
        if (this.getStackInSlot(0) == null || this.getStackInSlot(0).isEmpty() || this.getWorld().isBlockPowered(this.getPos()))
            return;


        dispense(this.getWorld(), this.getPos());


        // check if leftover item is a bucket and drop item in world
        // prevents fluids from being placed and picked up repeatedly
        if(isBucket(this.getStackInSlot(0)))
        {
            ItemEntity e = new ItemEntity(EntityType.ITEM, this.getWorld());

            Direction enumfacing = BlockAutoDropper.getFacing(this.getBlockState());

            double d0 = this.getPos().getX() + 0.5D + 0.7D * enumfacing.getXOffset();
            double d1 = this.getPos().getY() + 0.2D + 0.7D * enumfacing.getYOffset();
            double d2 = this.getPos().getZ() + 0.5D + 0.7D * enumfacing.getZOffset();

            if (enumfacing.getAxis() == Direction.Axis.Y)
            {
                d1 = d1 - 0.125D;
            }
            else
            {
                d1 = d1 - 0.15625D;
            }

            if (enumfacing == Direction.DOWN)
                d1 = this.getPos().getY() - 0.4D;

            e.setPosition(d0, d1, d2);

            double d3 = this.getWorld().rand.nextDouble() * 0.1D + 0.2D;
            int speed = 6;

            double motionX = enumfacing.getXOffset() * d3;
            double motionY = 0.20000000298023224D;
            double motionZ = enumfacing.getZOffset() * d3;


            motionX += 0.007499999832361937D * speed * enumfacing.getXOffset();
            motionY += 0.007499999832361937D * speed + (enumfacing.getAxis() == Direction.Axis.Y ? 0.3D * (enumfacing == Direction.UP ? 1 : -1) : 0);
            motionZ += 0.007499999832361937D * speed * enumfacing.getZOffset();

            e.setMotion(motionX, motionY, motionZ);

            e.setItem(this.getStackInSlot(0));

            this.setInventorySlotContents(0, ItemStack.EMPTY);

            if (enumfacing.getAxis() == Direction.Axis.Y)
            {
                e.setMotion(0, e.getMotion().y,0);
            }

            if (!this.getWorld().isRemote)
                this.getWorld().addEntity(e);
        }



/*
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);

        IDispenseItemBehavior ibehaviordispenseitem = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(this.getStackInSlot(0).getItem());

        if(ibehaviordispenseitem != IBehaviorDispenseItem.DEFAULT_BEHAVIOR)
            this.setInventorySlotContents(0, ibehaviordispenseitem.dispense(blocksourceimpl, this.getStackInSlot(0)));

        if(isBucket(this.getStackInSlot(0)))
        {
            EntityItem e = new EntityItem(this.getWorld());

            EnumFacing enumfacing = BlockAutoDropper.getFacing(getBlockMetadata());

            double d0 = this.getPos().getX() + 0.5D + 0.7D * enumfacing.getFrontOffsetX();
            double d1 = this.getPos().getY() + 0.2D + 0.7D * enumfacing.getFrontOffsetY();
            double d2 = this.getPos().getZ() + 0.5D + 0.7D * enumfacing.getFrontOffsetZ();

            if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            {
                d1 = d1 - 0.125D;
            }
            else
            {
                d1 = d1 - 0.15625D;
            }

            if (enumfacing == EnumFacing.DOWN)
                d1 = this.getPos().getY() - 0.4D;

            e.setPosition(d0, d1, d2);

            double d3 = this.getWorld().rand.nextDouble() * 0.1D + 0.2D;
            int speed = 6;
            e.motionX = enumfacing.getFrontOffsetX() * d3;
            e.motionY = 0.20000000298023224D;
            e.motionZ = enumfacing.getFrontOffsetZ() * d3;
            e.motionX += 0.007499999832361937D * speed * enumfacing.getFrontOffsetX();
            e.motionY += 0.007499999832361937D * speed + (enumfacing.getAxis() == EnumFacing.Axis.Y ? 0.3D * (enumfacing == EnumFacing.UP ? 1 : -1) : 0);
            e.motionZ += 0.007499999832361937D * speed * enumfacing.getFrontOffsetZ();
            e.setItem(this.getStackInSlot(0));

            this.setInventorySlotContents(0, null);

            if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            {
                e.motionX = 0;
                e.motionZ = 0;
            }

            if (!this.getWorld().isRemote)
                this.getWorld().spawnEntity(e);
        }
    }*/

    }


    private boolean isBucket(ItemStack is)
    {
        if(is.isEmpty())
            return false;

        return is.getItem() == Items.BUCKET || is.getItem() == Items.LAVA_BUCKET || is.getItem() == Items.WATER_BUCKET || FluidUtil.getFluidHandler(is).isPresent();
    }
}
