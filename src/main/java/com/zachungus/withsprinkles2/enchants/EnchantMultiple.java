package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantMultiple extends EnchantmentMod
{
	protected EnchantMultiple()
	{
		super(Rarity.RARE, EnchantmentType.ALL, EquipmentSlotType.values());
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
		return stack.hasTag() ? stack.getTag().contains("MULTIPLEMODE") ? stack.getTag().getInt("MULTIPLEMODE") == 0 : true : true;
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
