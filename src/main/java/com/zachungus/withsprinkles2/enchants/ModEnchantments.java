package com.zachungus.withsprinkles2.enchants;

import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Supplier;

public class ModEnchantments
{
    public static final DeferredRegister<Enchantment> ENCHANTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LibMisc.ModID);

    public static final RegistryObject<Enchantment> FIERY = regEnchantment(EnchantFiery::new);

    public static final RegistryObject<Enchantment> EXCHANGE = regEnchantment(EnchantExchange::new);

    public static final RegistryObject<Enchantment> MULTIPLE = regEnchantment(EnchantMultiple::new);

    public static final RegistryObject<Enchantment> FELLING = regEnchantment(EnchantFelling::new);

    public static final RegistryObject<Enchantment> FRACKING = regEnchantment(EnchantFracking::new);

    public static RegistryObject<Enchantment> regEnchantment(final Supplier<? extends EnchantmentMod> sup)
    {
        return ENCHANTS.register(sup.get().getEnchantRegistryName(), sup);
    }




}
