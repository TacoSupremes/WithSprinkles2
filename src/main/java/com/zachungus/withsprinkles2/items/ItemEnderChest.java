package com.zachungus.withsprinkles2.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemEnderChest extends ItemMod
{
    public ItemEnderChest()
    {
        super(getDefaultProps().maxStackSize(1));
    }

    @Override
    public String getItemRegistryName()
    {
        return "portable_ender_chest";
    }

    public static final TranslationTextComponent ENDERCHESTNAME = new TranslationTextComponent("container.enderchest");

    @Override
    public ActionResult<ItemStack> onItemRightClick(World w, PlayerEntity player, Hand hand)
    {
        if(!w.isRemote)
        {
            player.openContainer(new SimpleNamedContainerProvider((id, playerInventory, player2) -> {

                return ChestContainer.createGeneric9X3(id, playerInventory, player.getInventoryEnderChest());
            }, ENDERCHESTNAME));
        }
        else
        {
            w.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT,1, 1);
        }

        return super.onItemRightClick(w, player, hand);
    }
}
