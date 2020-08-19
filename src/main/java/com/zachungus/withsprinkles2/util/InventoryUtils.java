package com.zachungus.withsprinkles2.util;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryUtils
{


/*
	public static int itemsLeft(ItemStack ism, IInventory iio)
	{

		ItemStack is = ism.copy();

		NewInventory ii = new NewInventory(iio);

		for (int i = 0; i < ii.getSizeInventory(); i++)
		{

			ItemStack slot = ii.getStackInSlot(i);

			if (is == null || is.isEmpty() || is.getCount() == 0)
				return 0;

			if (slot == null)
			{
				ii.setInventorySlotContents(i, is);
				return 0;
			}

			if (ItemStack.areItemsEqual(is, slot))
			{

				if (slot.getCount() + is.getCount() <= slot.getMaxStackSize())
				{
					ii.setInventorySlotContents(i, new ItemStack(slot.getItem(), slot.getCount() + is.getCount(), slot.getTag()));
					return 0;
				}
				else
				{

					is.split(slot.getMaxStackSize() - slot.getCount());
					ii.setInventorySlotContents(i, new ItemStack(slot.getItem(), slot.getMaxStackSize(), slot.getTag()));

				}

			}
		}

		if (is == null || is.isEmpty() || is.getCount() == 0)
			return 0;

		return is.getCount();
	}


 */

	public static IInventory getInventory(World w, BlockPos bp)
	{

		if (w.getTileEntity(bp) == null)
			return null;

		if (!(w.getTileEntity(bp) instanceof IInventory))
			return null;

		if (w.getTileEntity(bp) instanceof ChestTileEntity)
		{
			Block chestBlock = w.getBlockState(bp).getBlock();

			return ChestBlock.func_226916_a_((ChestBlock)chestBlock, w.getBlockState(bp), w, bp, true);


			//LockableContainer tc = ((ChestBlock) chestBlock).getLockableContainer(w, bp);




			/*
			if (w.getBlockState(bp.add(-1, 0, 0)).getBlock() == chestBlock)
				return new LargeChest("Large chest", ((ChestBlock) w.getBlockState(bp.add(-1, 0, 0)).getBlock()).getLockableContainer(w, bp.add(-1, 0, 0)), tc);
			if (w.getBlockState(bp.add(-1, 0, 0)).getBlock() == chestBlock)
				return new InventoryLargeChest("Large chest", ((ChestBlock) w.getBlockState(bp.add(1, 0, 0)).getBlock()).getLockableContainer(w, bp.add(1, 0, 0)), tc);
			if (w.getBlockState(bp.add(0, 0, -1)).getBlock() == chestBlock)
				return new InventoryLargeChest("Large chest", ((ChestBlock) w.getBlockState(bp.add(0, 0, -1)).getBlock()).getLockableContainer(w, bp.add(0, 0, -1)), tc);
			if (w.getBlockState(bp.add(0, 0, 1)).getBlock() == chestBlock)
				return new InventoryLargeChest("Large chest", ((ChestBlock) w.getBlockState(bp.add(0, 0, 1)).getBlock()).getLockableContainer(w, bp.add(0, 0, 1)), tc);

		*/
		}


		return (IInventory) w.getTileEntity(bp);
	}

	public static int countofItemStack(IInventory ii, ItemStack is)
	{

		int amount = 0;

		for (int i = 0; i < ii.getSizeInventory(); i++)
		{

			if (ItemStack.areItemsEqual(is, ii.getStackInSlot(i)))
				amount += ii.getStackInSlot(i).getCount();

		}

		return amount;
	}

}
