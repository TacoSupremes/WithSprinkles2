package com.zachungus.withsprinkles2.blocks.tiles;

import com.zachungus.withsprinkles2.blocks.BlockEnderHopper;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.util.InventoryUtils;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

import java.util.UUID;

public class TileEnderHopper extends TileSimpleInventory implements ITickableTileEntity
{
    public TileEnderHopper()
    {
        super(ModBlocks.TILE_ENDER_HOPPER.get());
    }

    @Override
    public int getSizeInventory()
    {
        return 5;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return true;
    }

    public UUID uuid = null;

    int ticks = 0;

    @Override
    public void tick()
    {

        if (this.getWorld().isBlockPowered(pos))
            return;

        ticks++;

        if (ticks % 16 != 0)
            return;

        if (this.getWorld().isRemote)
            return;

       Direction enumf = this.getBlockState().get(BlockEnderHopper.FACING);

        if (InventoryUtils.getInventory(getWorld(), getPos().up()) != null && (this.getWorld().getBlockState(getPos().up()).getBlock() != Blocks.ENDER_CHEST  && this.getWorld().getBlockState(getPos().up()).getBlock() != ModBlocks.BOUND_ENDER_CHEST.get()))
        {
            IInventory ii = InventoryUtils.getInventory(getWorld(), getPos().up());

            for (int i = 0; i < ii.getSizeInventory(); i++)
            {
                if (ii.getStackInSlot(i).isEmpty())
                    continue;

                ItemStack is = ii.getStackInSlot(i);

                ItemStack result = HopperTileEntity.putStackInInventoryAllSlots(ii,this, is, Direction.UP);

                ii.setInventorySlotContents(i, result);

                ii.markDirty();
                this.markDirty();

                if(result.isEmpty())
                    break;
            }

        }
        else if (this.getWorld().getBlockState(getPos().up()).getBlock() == Blocks.ENDER_CHEST)
        {
            PlayerEntity player = this.getWorld().getPlayerByUuid(uuid);

            EnderChestInventory ii = player == null ? OfflinePlayerUtils.getOfflineEnderChest(uuid, this.getWorld()) : player.getInventoryEnderChest();

            for (int i = 18; i < 27; i++)
            {
                if (ii.getStackInSlot(i).isEmpty())
                    continue;


                ItemStack is = ii.getStackInSlot(i);

                ItemStack result = HopperTileEntity.putStackInInventoryAllSlots(ii,this, is, Direction.UP);

                ii.setInventorySlotContents(i, result);

                if(result.isEmpty())
                {
                    ii.markDirty();
                    break;
                }

            }

        }
        else if (this.getWorld().getBlockState(getPos().up()).getBlock() == ModBlocks.BOUND_ENDER_CHEST.get())
        {

            UUID shared = ((TileBoundEnderChest) this.getWorld().getTileEntity(pos.up())).uuid;

            PlayerEntity player = this.getWorld().getPlayerByUuid(shared);

            EnderChestInventory ii = player == null ? OfflinePlayerUtils.getOfflineEnderChest(shared, this.getWorld()) : player.getInventoryEnderChest();

            for (int i = 18; i < 27; i++)
            {
                if (ii.getStackInSlot(i).isEmpty())
                    continue;


                ItemStack is = ii.getStackInSlot(i);

                ItemStack result = HopperTileEntity.putStackInInventoryAllSlots(ii,this, is, Direction.UP);

                ii.setInventorySlotContents(i, result);

                if(result.isEmpty())
                {
                    ii.markDirty();
                    break;
                }

            }

        }

        for(int inv = 0; inv < this.getSizeInventory(); inv++)
        {

            ItemStack is = this.getStackInSlot(inv);

            if (is.isEmpty())
                continue;

            if (InventoryUtils.getInventory(getWorld(), getPos().add(enumf.getDirectionVec())) != null && this.getWorld().getBlockState(getPos().add(enumf.getDirectionVec())).getBlock() != ModBlocks.BOUND_ENDER_CHEST.get())
            {

                ItemStack result = HopperTileEntity.putStackInInventoryAllSlots(this, InventoryUtils.getInventory(getWorld(), getPos().add(enumf.getDirectionVec())), is, enumf.getOpposite());

               this.setInventorySlotContents(inv, result);

                if(result.isEmpty())
                {
                    InventoryUtils.getInventory(getWorld(), getPos().add(enumf.getDirectionVec())).markDirty();
                    return;
                }


            } else if (this.getWorld().getBlockState(getPos().add(enumf.getDirectionVec())).getBlock() == Blocks.ENDER_CHEST) {

                PlayerEntity player = this.getWorld().getPlayerByUuid(uuid);

                 EnderChestInventory ii = player == null ? OfflinePlayerUtils.getOfflineEnderChest(uuid, this.getWorld()) : player.getInventoryEnderChest();

                int slotChosen = -1;

                for (int i = 18; i < 27; i++) {

                    if (!ii.getStackInSlot(i).isEmpty()) {

                        if (ii.getStackInSlot(i).isItemEqual(is))
                        {

                            if (ii.getStackInSlot(i).getCount() == ii.getStackInSlot(i).getMaxStackSize())
                                continue;

                            if (is.getCount() + ii.getStackInSlot(i).getCount() <= is.getMaxStackSize())
                            {

                                ii.setInventorySlotContents(i, new ItemStack(is.getItem(), is.getCount() + ii.getStackInSlot(i).getCount()));
                                this.setInventorySlotContents(inv, ItemStack.EMPTY);
                                ii.markDirty();
                                //this.markDirty();
                                return;
                            } else {

                                ii.setInventorySlotContents(i, new ItemStack(is.getItem(), is.getMaxStackSize()));
                                this.setInventorySlotContents(inv, new ItemStack(is.getItem(), is.getCount() + ii.getStackInSlot(i).getCount() - is.getMaxStackSize()));
                                ii.markDirty();
                               // this.markDirty();
                                return;

                            }

                        }

                    } else {
                        slotChosen = i;
                        break;
                    }

                }

                if (slotChosen != -1)
                {
                    ii.setInventorySlotContents(slotChosen, is);
                    this.setInventorySlotContents(inv, ItemStack.EMPTY);
                    ii.markDirty();
                }

            }
            else if (this.getWorld().getBlockState(getPos().add(enumf.getDirectionVec())).getBlock() == ModBlocks.BOUND_ENDER_CHEST.get())
            {

                UUID shared = ((TileBoundEnderChest) this.getWorld().getTileEntity(getPos().add(enumf.getDirectionVec()))).uuid;


                PlayerEntity player = this.getWorld().getPlayerByUuid(shared);

                EnderChestInventory ii = player == null ? OfflinePlayerUtils.getOfflineEnderChest(shared, this.getWorld()) : player.getInventoryEnderChest();

                int slotChosen = -1;

                for (int i = 18; i < 27; i++) {

                    if (!ii.getStackInSlot(i).isEmpty()) {

                        if (ii.getStackInSlot(i).isItemEqual(is))
                        {

                            if (ii.getStackInSlot(i).getCount() == ii.getStackInSlot(i).getMaxStackSize())
                                continue;

                            if (is.getCount() + ii.getStackInSlot(i).getCount() <= is.getMaxStackSize())
                            {

                                ii.setInventorySlotContents(i, new ItemStack(is.getItem(), is.getCount() + ii.getStackInSlot(i).getCount()));
                                this.setInventorySlotContents(inv, ItemStack.EMPTY);
                                ii.markDirty();
                               // this.markDirty();
                                return;
                            }
                            else
                            {
                                ii.setInventorySlotContents(i, new ItemStack(is.getItem(), is.getMaxStackSize()));
                                this.setInventorySlotContents(inv, new ItemStack(is.getItem(), is.getCount() + ii.getStackInSlot(i).getCount() - is.getMaxStackSize()));
                                ii.markDirty();
                               ///this.markDirty();
                                return;

                            }

                        }

                    } else {
                        slotChosen = i;
                        break;
                    }

                }

                if (slotChosen != -1)
                {
                    ii.setInventorySlotContents(slotChosen, is);
                    this.setInventorySlotContents(inv, ItemStack.EMPTY);
                    ii.markDirty();
                }
            }
        }
    }

    @Override
    public void readCustomNBT(CompoundNBT par1nbtTagCompound)
    {
        super.readCustomNBT(par1nbtTagCompound);
        if(par1nbtTagCompound.contains("PNAME"))
            this.uuid = UUID.fromString(par1nbtTagCompound.getString("PNAME"));

        this.ticks = par1nbtTagCompound.getInt("TICKS");

    }

    @Override
    public void writeCustomNBT(CompoundNBT par1nbtTagCompound)
    {

        super.writeCustomNBT(par1nbtTagCompound);
        if(uuid != null)
            par1nbtTagCompound.putString("PNAME", this.uuid.toString());

        par1nbtTagCompound.putInt("TICKS", this.ticks);
    }

}