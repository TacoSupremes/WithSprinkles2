package com.zachungus.withsprinkles2.blocks.tiles;

import com.zachungus.withsprinkles2.blocks.BlockAutoDropper;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.util.InventoryUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TileAutoDropper extends TileSimpleInventory implements ITickableTileEntity
{
    public TileAutoDropper(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public TileAutoDropper()
    {
        this(ModBlocks.TILE_AUTO_DROPPER.get());
    }

    @Override
    public void tick()
    {
        if (this.getStackInSlot(0) == null || this.getStackInSlot(0).isEmpty() || this.getWorld().isBlockPowered(pos))
            return;

        BlockPos bp = getPos().add(BlockAutoDropper.getFacing(this.getBlockState()).getDirectionVec());

        if (InventoryUtils.getInventory(this.getWorld(), bp) == null)
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
        else
        {
            Direction enumfacing = BlockAutoDropper.getFacing(this.getBlockState());

            IInventory t = InventoryUtils.getInventory(this.getWorld(), bp);

            ItemStack is = HopperTileEntity.putStackInInventoryAllSlots(this, t, this.getStackInSlot(0), enumfacing.getOpposite());

            this.setInventorySlotContents(0, is);
            this.markDirty();
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }
}
