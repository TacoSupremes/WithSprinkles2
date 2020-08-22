package com.zachungus.withsprinkles2.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class EnchantmentMod extends Enchantment
{
    protected EnchantmentMod(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots)
    {
        super(rarityIn, typeIn, slots);
    }

    public abstract String getEnchantRegistryName();
}
