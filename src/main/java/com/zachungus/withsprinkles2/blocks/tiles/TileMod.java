/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the WithSprinkles Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 21, 2014, 9:18:28 PM (GMT)]
 */
package com.zachungus.withsprinkles2.blocks.tiles;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class TileMod extends TileEntity
{

	public TileMod(TileEntityType<?> tileEntityTypeIn)

	{
		super(tileEntityTypeIn);
	}

	/*
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}*/

	@Nonnull
	@Override
	public CompoundNBT write(CompoundNBT par1nbtTagCompound)
	{
		CompoundNBT ret = super.write(par1nbtTagCompound);
		writeCustomNBT(ret);
		return ret;
	}

	@Nonnull
	@Override
	public final CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public void read(CompoundNBT par1nbtTagCompound)
	{
		super.read(par1nbtTagCompound);
		readCustomNBT(par1nbtTagCompound);
	}

	public void writeCustomNBT(CompoundNBT cmp)
	{
	}

	public void readCustomNBT(CompoundNBT cmp)
	{
	}

	@Override
	public final SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT tag = new CompoundNBT();
		writeCustomNBT(tag);
		return new SUpdateTileEntityPacket(pos, -999, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		super.onDataPacket(net, packet);
		readCustomNBT(packet.getNbtCompound());
	}


}