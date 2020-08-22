package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class EnchantFelling extends EnchantmentMod
{
	protected EnchantFelling()
	{
		super(Rarity.UNCOMMON, EnchantmentType.DIGGER, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack != ItemStack.EMPTY  && stack.getItem().getToolTypes(stack).contains(ToolType.AXE);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return canApply(stack);
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
		return "felling";
	}
}
