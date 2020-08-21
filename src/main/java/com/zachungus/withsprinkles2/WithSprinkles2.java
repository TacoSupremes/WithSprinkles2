package com.zachungus.withsprinkles2;

import com.zachungus.withsprinkles2.blocks.ModBlocks;
import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChest;
import com.zachungus.withsprinkles2.blocks.tiles.TileBoundEnderChestRenderer;
import com.zachungus.withsprinkles2.items.ModItems;
import com.zachungus.withsprinkles2.lib.LibMisc;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.*;
import net.minecraftforge.client.model.pipeline.TRSRTransformer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.stream.Collectors;

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

        //ModItems.makeBlockItems();

        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(OfflinePlayerUtils.class);
        MinecraftForge.EVENT_BUS.register(this);
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

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event)
    {
        // do something when the server starts
       // LOGGER.info("HELLO from server starting");
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
