package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantExchange extends EnchantmentMod
{
	protected EnchantExchange()
	{
		super(Rarity.UNCOMMON, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 5 + (enchantmentLevel - 1) * 10;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 30;
	}

	@Override
	public String getEnchantRegistryName()
	{
		return "exchange";
	}
}
