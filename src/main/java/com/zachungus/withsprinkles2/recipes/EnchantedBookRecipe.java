package com.zachungus.withsprinkles2.recipes;

import com.zachungus.withsprinkles2.items.ModItems;
import com.zachungus.withsprinkles2.util.EnchantUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnchantedBookRecipe extends SpecialRecipe
{
	public EnchantedBookRecipe(ResourceLocation resourceLocation) {
		super(resourceLocation);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn)
	{
		boolean hasLeather = false;

		int paper = 0;
		int oldPaper = 0;

		for (int x = 0; x < inv.getWidth(); x++)
		{
			for (int y = 0; y < inv.getHeight(); y++)
			{
				int slot = x * inv.getWidth() + y;

				ItemStack is = inv.getStackInSlot(slot);

				if (is.isEmpty())
					continue;

				if (is.getItem() == Items.LEATHER)
				{
					if (hasLeather)
						return false;

					hasLeather = true;
					continue;
				}

				if (is.getItem() == ModItems.LOST_PAGE.get())
				{
					paper++;
					oldPaper++;
				}

				if (is.getItem() == Items.PAPER)
					paper++;

			}
		}

		return oldPaper > 0 && paper == 3 && hasLeather;
	}

	Random rand = new Random();

	public static List<Enchantment> enchant = Arrays.asList(null, null, null);

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv)
	{
		ItemStack is = new ItemStack(Items.ENCHANTED_BOOK);

		int oldPaper = 0;

		for (int x = 0; x < inv.getWidth(); x++)
		{
			for (int y = 0; y < inv.getHeight(); y++)
			{
				int slot = x * inv.getWidth() + y;

				ItemStack invs = inv.getStackInSlot(slot);

				if (!invs.isEmpty() && invs.getItem() == ModItems.LOST_PAGE.get())
					oldPaper++;
			}

		}

		// 1 oldPaper gives Common to Rare, 2 gives Uncommon to Rare, 3 gives
		// treasure enchant and Very Rare
		Rarity r = Rarity.values()[Math.min(oldPaper - 1 + rand.nextInt(2), Rarity.values().length - 1)];

		Enchantment e = EnchantUtils.randEnchantmentTier(rand,oldPaper == 3, oldPaper == 3 ? Rarity.VERY_RARE : r);

		oldPaper--;

		if(enchant.get(oldPaper) == null)
		{
			enchant.set(oldPaper, e);
		}
		else
		{
			e = enchant.get(oldPaper);
		}

		EnchantedBookItem.addEnchantment(is, new EnchantmentData(e, e.getMaxLevel()));

		return is;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width >= 2 && height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return ModRecipes.r.get();
	}

	public boolean isDynamic() {
		return true;
	}

}
