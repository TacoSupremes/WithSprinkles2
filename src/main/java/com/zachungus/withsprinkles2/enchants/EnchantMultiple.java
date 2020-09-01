package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

import java.util.function.Predicate;

public class EnchantMultiple extends EnchantmentMod
{
	protected EnchantMultiple()
	{
		super(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND);
	}

	@Override
	public int getMaxLevel()
	{
		return 2;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 20;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 40;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return ignoreItems(stack) && (!stack.hasTag() || (!stack.getTag().contains("MULTIPLEMODE") || stack.getTag().getInt("MULTIPLEMODE") == 0));
	}

	public boolean ignoreItems(ItemStack is)
	{
		return !(is.getItem() instanceof ArmorItem || is.getItem() instanceof FishingRodItem);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return canApply(stack);
	}

	@Override
	public String getEnchantRegistryName()
	{
		return "multiple";
	}
}
