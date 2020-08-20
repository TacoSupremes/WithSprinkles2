package com.zachungus.withsprinkles2.blocks.tiles;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public abstract class TileSimpleInventory extends TileMod implements IInventory
{

	// this was taken from Botania mod 1.12

	NonNullList<ItemStack> inventorySlots = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);

	public TileSimpleInventory(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
	}

	@Override
	public void readCustomNBT(CompoundNBT par1NBTTagCompound)
	{
		ItemStackHelper.loadAllItems(par1NBTTagCompound, inventorySlots);
	}

	@Override
	public void writeCustomNBT(CompoundNBT par1NBTTagCompound)
	{
		ItemStackHelper.saveAllItems(par1NBTTagCompound, inventorySlots);
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inventorySlots.get(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (inventorySlots.get(i) != ItemStack.EMPTY)
		{
			ItemStack stackAt;

			if (inventorySlots.get(i).getCount() <= j)
			{
				stackAt = inventorySlots.get(i);
				inventorySlots.set(i, ItemStack.EMPTY);
				return stackAt;
			}
			else
			{
				stackAt = inventorySlots.get(i).split(j);

				if (inventorySlots.get(i).getCount() == 0)
					inventorySlots.set(i, ItemStack.EMPTY);

				return stackAt;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{

		inventorySlots.set(i, itemstack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public void openInventory(PlayerEntity player)
	{

	}

	@Override
	public void closeInventory(PlayerEntity player)
	{

	}

	@Override
	public void clear()
	{
		for (int i = 0; i < inventorySlots.size(); i++)
		{
			inventorySlots.set(i, ItemStack.EMPTY);
		}

	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{

		if (inventorySlots.get(index).isEmpty())
			return ItemStack.EMPTY;

		ItemStack is = inventorySlots.get(index).copy();

		inventorySlots.set(index, ItemStack.EMPTY);

		return is;
	}


	@Override
	public boolean isEmpty()
	{

		for (int i = 0; i < getSizeInventory(); i++)
		{
			if (!inventorySlots.get(i).isEmpty())
				return false;
		}

		return true;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{

		return false;
	}

	/*
	public static void breakBlock(World w, BlockPos pos, BlockState state)
	{
		TileSimpleInventory te = (TileSimpleInventory) w.getTileEntity(pos);

		for (ItemStack is : te.inventorySlots)
		{

			if (is.isEmpty())
				continue;

			Random RANDOM = w.rand;

			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			float f = RANDOM.nextFloat() * 0.8F + 0.1F;
			float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
			float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

			ItemEntity entityitem = new ItemEntity(w, x + (double) f, y + (double) f1, z + (double) f2, is);
			float f3 = 0.05F;


			entityitem.setMotion(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);



			w.spawnEntity(entityitem);

		}

	}
	*/


}