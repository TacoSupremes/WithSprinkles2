package com.zachungus.withsprinkles2.blocks.tiles;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.zachungus.withsprinkles2.blocks.ModBlocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ItemBoundEnderChestRender extends ItemStackTileEntityRenderer {

    private final Supplier<TileBoundEnderChest> te;

    public ItemBoundEnderChestRender(Supplier<TileBoundEnderChest> te) {
        this.te = te;
    }


    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        TileEntityRendererDispatcher.instance.renderItem(this.te.get(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}