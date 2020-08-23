package com.zachungus.withsprinkles2.recipes;

import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes
{
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LibMisc.ModID);

    public static final RegistryObject<IRecipeSerializer<EnchantedBookRecipe>> r = RECIPES.register("lost_page_book_recipe", ()-> new SpecialRecipeSerializer<EnchantedBookRecipe>(EnchantedBookRecipe::new));

}
