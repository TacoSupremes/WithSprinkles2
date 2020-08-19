package com.zachungus.withsprinkles2.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchantUtils
{

	public static void enchantItem(ItemStack is, Enchantment e, int lvl)
	{

		ListNBT nbtl = new ListNBT ();

		CompoundNBT nbt = new CompoundNBT();

		nbt.putString("id", String.valueOf((Object)ForgeRegistries.ENCHANTMENTS.getKey(e)));
		nbt.putShort("lvl", (short) lvl);
		nbtl.add(nbt);

		if (is.getItem() == Items.ENCHANTED_BOOK)
		{
			EnchantedBookItem.addEnchantment(is, new EnchantmentData(e, lvl));
			return;
		}

		if (!is.hasTag())
			is.setTag(new CompoundNBT());

		is.setTagInfo("ench", nbtl);

	}

	public static Enchantment randEnchantmentBook(Random rand, ItemStack is, boolean treasureOnly, Rarity rarity)
	{

		List<Enchantment> treasure = new ArrayList<Enchantment>();
		List<Enchantment> canApply = new ArrayList<Enchantment>();

		if (treasureOnly)
			rarity = Rarity.RARE;

		for (ResourceLocation r : ForgeRegistries.ENCHANTMENTS.getKeys())
		{
			Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(r);
			if (e.isCurse())
				continue;

			if (e.isTreasureEnchantment())
				treasure.add(e);

			if (e.getRarity() == rarity)
				canApply.add(e);

			if (treasureOnly && e.getRarity() == Rarity.VERY_RARE)
				canApply.add(e);
		}

		if (treasureOnly)
			canApply.addAll(treasure);

		return canApply.get(rand.nextInt(canApply.size()));

	}

	public static Enchantment randEnchantmentMinTier(Random rand, ItemStack is, boolean treasure, Rarity rarity)
	{

		List<Enchantment> treasureL = new ArrayList<Enchantment>();
		List<Enchantment> canApply = new ArrayList<Enchantment>();

		for (ResourceLocation r : ForgeRegistries.ENCHANTMENTS.getKeys())
		{
			Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(r);
			if (e.isCurse())
				continue;

			if (e.isTreasureEnchantment())
				treasureL.add(e);
			else if (e.getRarity().getWeight() <= rarity.getWeight())
				canApply.add(e);
		}

		if (treasure)
			canApply.addAll(treasureL);

		return canApply.get(rand.nextInt(canApply.size()));

	}

	public static Enchantment randEnchantmentMaxTier(Random rand, ItemStack is, boolean treasure, Rarity rarity)
	{

		List<Enchantment> treasureL = new ArrayList<Enchantment>();
		List<Enchantment> canApply = new ArrayList<Enchantment>();

		for (ResourceLocation r : ForgeRegistries.ENCHANTMENTS.getKeys())
		{
			Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(r);
			if (e.isCurse())
				continue;

			if (e.isTreasureEnchantment())
				treasureL.add(e);
			else if (e.getRarity().getWeight() >= rarity.getWeight())
				canApply.add(e);
		}

		if (treasure)
			canApply.addAll(treasureL);

		return canApply.get(rand.nextInt(canApply.size()));

	}

	public static Enchantment randEnchantmentTier(Random rand, ItemStack is, boolean treasure, Rarity rarity)
	{

		List<Enchantment> treasureL = new ArrayList<Enchantment>();
		List<Enchantment> canApply = new ArrayList<Enchantment>();

		for (ResourceLocation r : ForgeRegistries.ENCHANTMENTS.getKeys())
		{
			Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(r);

			if (e.isCurse())
				continue;

			if (e.isTreasureEnchantment())
				treasureL.add(e);
			else if (e.getRarity() == rarity)
				canApply.add(e);
		}

		if (treasure)
			canApply.addAll(treasureL);

		return canApply.get(rand.nextInt(canApply.size()));

	}
}
