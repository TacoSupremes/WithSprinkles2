package com.zachungus.withsprinkles2.util;

import com.zachungus.withsprinkles2.WithSprinkles2;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OfflinePlayerUtils
{
	private static Map<UUID, CompoundNBT> map = new HashMap<UUID, CompoundNBT>();

	private static Map<UUID, EnderChestInventory> mapEnder = new HashMap<UUID, EnderChestInventory>();

	private static Map<UUID, String> UUIDtoName = new HashMap<UUID, String>();

	private static void writeOfflinePlayerNBT(UUID uuid, World w)
	{
		SaveHandler saveHandler = w.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler();

		try
		{
			File playersDirectory = new File(saveHandler.getWorldDirectory(), "playerdata");

			File temp = new File(playersDirectory, uuid.toString() + ".dat.tmp");
			File playerFile = new File(playersDirectory, uuid.toString() + ".dat");
			CompressedStreamTools.writeCompressed(map.get(uuid), new FileOutputStream(temp));

			if (playerFile.exists())
				playerFile.delete();

			temp.renameTo(playerFile);
		}
		catch (Exception e)
		{
			WithSprinkles2.LOGGER.log(Level.ERROR, "Player NOT found with UUID" + uuid.toString());
		}
	}

	/** Gets an Offline Player's NBT **/
	private static CompoundNBT getOfflinePlayerNBT(UUID uuid, World w)
	{
		if (map.containsKey(uuid))
			return map.get(uuid);

		SaveHandler saveHandler = w.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler();
		File playersDirectory = new File(saveHandler.getWorldDirectory(), "playerdata");

		try
		{
			File file1 = new File(playersDirectory, uuid.toString() + ".dat");

			if (file1.exists() && file1.isFile())
			{
				map.put(uuid, CompressedStreamTools.readCompressed(new FileInputStream(file1)));

				return map.get(uuid);
			}
		}
		catch (Exception e)
		{
			WithSprinkles2.LOGGER.log(Level.ERROR, "Player NOT found with UUID" + uuid.toString(), e);
		}

		return null;
	}

	public static EnderChestInventory getOfflineEnderChest(UUID uuid, World w)
	{
		if (mapEnder.containsKey(uuid))
			return mapEnder.get(uuid);

		CompoundNBT nbt = getOfflinePlayerNBT(uuid, w);

		EnderChestInventory ii = new EnderChestInventory();

		ii.read(nbt.getList("EnderItems",10));

		mapEnder.put(uuid, ii);

		return ii;
	}

	private static void saveOfflineEnderChest(UUID uuid, World w)
	{
		CompoundNBT nbt = OfflinePlayerUtils.getOfflinePlayerNBT(uuid, w);

		nbt.put("EnderItems", OfflinePlayerUtils.getOfflineEnderChest(uuid, w).write());

		map.put(uuid, nbt);
	}

	private static void saveOfflineNBT(UUID uuid, World w)
	{

		CompoundNBT nbt = OfflinePlayerUtils.getOfflinePlayerNBT(uuid, w);

		map.put(uuid, nbt);

		writeOfflinePlayerNBT(uuid, w);
	}

	@SubscribeEvent
	public static void onPlayerJoinWorld(LoadFromFile event)
	{
		UUID uuid = event.getPlayer().getUniqueID();

		if (map.containsKey(uuid))
		{
			if (mapEnder.containsKey(uuid))
			{
				EnderChestInventory ee = mapEnder.get(uuid);
				for(int i = 0; i< ee.getSizeInventory(); i++)
					event.getPlayer().getInventoryEnderChest().setInventorySlotContents(i, ee.getStackInSlot(i));

				mapEnder.remove(uuid);
			}
			map.remove(uuid);
		}
	}

	@SubscribeEvent
	public static void onWorldClose(Unload event)
	{
		if (event.getWorld().isRemote())
			return;

		for (UUID uuid : map.keySet())
		{
			if (map.containsKey(uuid))
			{
				if (mapEnder.containsKey(uuid))
					OfflinePlayerUtils.saveOfflineEnderChest(uuid, (World) event.getWorld());

				OfflinePlayerUtils.saveOfflineNBT(uuid, (World) event.getWorld());

			}
		}
	}
}
