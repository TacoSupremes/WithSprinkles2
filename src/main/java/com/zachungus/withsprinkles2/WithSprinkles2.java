package com.zachungus.withsprinkles2;

import com.google.common.collect.ImmutableList;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChestRenderer;
import com.zachungus.withsprinkles2.enchants.ModEnchantments;
import com.zachungus.withsprinkles2.events.WS2Events;
import com.zachungus.withsprinkles2.items.ModItems;
import com.zachungus.withsprinkles2.lib.LibMisc;
import com.zachungus.withsprinkles2.recipes.EnchantedBookRecipe;
import com.zachungus.withsprinkles2.recipes.ModRecipes;
import com.zachungus.withsprinkles2.util.LootHandler;
import com.zachungus.withsprinkles2.util.WSSavedData;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(LibMisc.ModID)
public class WithSprinkles2
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public WithSprinkles2()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModBlocks.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModEnchantments.ENCHANTS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModRecipes.RECIPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(OfflinePlayerUtils.class);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(WS2Events.class);
        MinecraftForge.EVENT_BUS.register(new LootHandler());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code

        //LOGGER.info("HELLO FROM PREINIT");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event)
    {
        // do something that can only be done on the client
        RenderTypeLookup.setRenderLayer(ModBlocks.RAIN_DETECTOR.get(), RenderType.getCutout());

        ClientRegistry.bindTileEntityRenderer(ModBlocks.TILE_BOUND_ENDER_CHEST.get(), TileBoundEnderChestRenderer::new);
       // LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    }







    // number of times world loaded  the event is fired multiple times
    public static int LOADED_WORLD = 0;

    // handles Saving data to world (EnchantedBookRecipe)
    @SubscribeEvent
    public void onWorldLoaded(WorldEvent.Load event)
    {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
           // LOGGER.debug("lOADED WORLD");

            WSSavedData saver = WSSavedData.forWorld((ServerWorld) event.getWorld());

            LOADED_WORLD++;

            if(saver.data.contains("ENCHANTS"))
            {
                CompoundNBT c = saver.data.getCompound("ENCHANTS");

              //  LOGGER.debug("ENCHANTS FOUND " + LOADED_WORLD);

                for(int i = 0; i < 3; i++)
                {
                    String s = c.getString("ENCHANT" + i);

                    Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(s));

                  //  if(e != null)
                   //     LOGGER.debug("LOADED: " + s);

                    EnchantedBookRecipe.enchant.set(i, e);
                }
            }
            else
            {
              //  LOGGER.debug("ENCHANTS NOT FOUND" + LOADED_WORLD);

                if (LOADED_WORLD == 2)
                {
                    EnchantedBookRecipe.enchant = Arrays.asList(null, null, null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
            WSSavedData saver = WSSavedData.forWorld((ServerWorld) event.getWorld());
            CompoundNBT myData = new CompoundNBT();

            CompoundNBT c = new CompoundNBT();

            for(int i = 0; i < 3; i++)
            {
                Enchantment e = EnchantedBookRecipe.enchant.get(i);

                if(e == null)
                {
                    c.putString("ENCHANT" + i, "null");
                   // LOGGER.debug("SAVED: " + "NULL");
                }
                else
                {
                    //LOGGER.debug("SAVED: " + e.getDisplayName(e.getMaxLevel()).getString());
                    c.putString("ENCHANT" + i, e.getRegistryName().toString());
                }
            }

            myData.put("ENCHANTS", c);

            saver.data = myData;
            saver.markDirty();
            LOADED_WORLD++;
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // register a new block here
           // LOGGER.info("HELLO from Register Block");
       // blockRegistryEvent.getRegistry().register(ModBlocks.test);
        }
    }

    public static final ItemGroup TAB = new ItemGroup("withsprinkles2.tab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Blocks.CAKE);
        }
    };
}
