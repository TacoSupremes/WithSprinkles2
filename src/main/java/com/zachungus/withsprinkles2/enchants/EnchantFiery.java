package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantFiery extends EnchantmentMod
{
	protected EnchantFiery()
	{
		super(Rarity.RARE, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND });
	}

	@Override
	public boolean canApplyTogether(Enchantment ench)
	{
		return super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH && ench != Enchantments.FORTUNE;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 15;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public String getEnchantRegistryName()
	{
		return "fiery";
	}
}
