package com.zachungus.withsprinkles2.util;

import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class WSSavedData extends WorldSavedData
{
    private static final String DATA_NAME = LibMisc.ModID;

    private final String NBT_NAME = "NBT_SAVE";

    public WSSavedData()
    {
        super(DATA_NAME);
    }

    public CompoundNBT data = new CompoundNBT();

    @Override
    public void read(CompoundNBT nbt)
    {
        data = nbt.getCompound(NBT_NAME);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.put(NBT_NAME, data);
        return nbt;
    }

    public static WSSavedData forWorld(ServerWorld world)
    {
        DimensionSavedDataManager storage = world.getSavedData();

        WSSavedData saver = (WSSavedData) storage.getOrCreate(WSSavedData::new, LibMisc.ModID);

        if (saver == null)
        {
            saver = new WSSavedData();
            storage.set(saver);
        }

        return saver;
    }
}