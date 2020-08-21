package com.zachungus.withsprinkles2.events;

import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibMisc.ModID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents
{
    public static final ResourceLocation BOUND_CHEST_LOCATION = new ResourceLocation(LibMisc.ModID, "model/bound_ender_chest");

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS))
        {
            return;
        }

        event.addSprite(BOUND_CHEST_LOCATION);
    }
}
