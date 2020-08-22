package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.lib.LibMisc;
import com.zachungus.withsprinkles2.util.OfflinePlayerUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemBoundEnderChest extends ItemMod
{
    @Override
    public String getItemRegistryName()
    {
        return "portable_bound_ender_chest";
    }

    public static final TranslationTextComponent ENDER_CHEST_NAME = new TranslationTextComponent("container.enderchest");

    public final String PLAYER_UUID = "PLAYER_UUID";

    public final String PLAYER_NAME = "PLAYER_NAME";

    @Override
    public ActionResult<ItemStack> onItemRightClick(World w, PlayerEntity player, Hand hand)
    {
        ItemStack is = player.getHeldItem(hand);

        if(!is.hasTag())
        {
            is.setTag(new CompoundNBT());
            is.getTag().putString(PLAYER_UUID, player.getUniqueID().toString());
            is.getTag().putString(PLAYER_NAME, player.getDisplayName().getString());
        }

        UUID u = UUID.fromString(is.getTag().getString(PLAYER_UUID));

        PlayerEntity target = w.getPlayerByUuid(u);

        // if the player changes display name update their name on the NBT for display purposes
        if(target != null && target.getDisplayName().toString() != is.getTag().getString(PLAYER_NAME))
        {
            is.getTag().putString(PLAYER_NAME, target.getDisplayName().getString());
        }

        if(!w.isRemote)
        {
            StringTextComponent s = new StringTextComponent(is.getTag().getString(PLAYER_NAME) + "'s ");

            player.openContainer(new SimpleNamedContainerProvider((id, playerInventory, player2) -> {

                return ChestContainer.createGeneric9X3(id, playerInventory, target != null ? target.getInventoryEnderChest() :  OfflinePlayerUtils.getOfflineEnderChest(u, w));
                }, s.appendSibling(ENDER_CHEST_NAME)));
        }
        else
        {
            w.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT,1, 1);
        }

        return super.onItemRightClick(w, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.hasTag())
        {
            // gray color code
            tooltip.add(new StringTextComponent(I18n.format(LibMisc.ModID + '.' + "bound") + ": " +  stack.getTag().getString(PLAYER_NAME)).applyTextStyle(TextFormatting.GRAY));
        }
        else
        {
            //red color code
            tooltip.add(new StringTextComponent(I18n.format(LibMisc.ModID + '.' + "unbound")).applyTextStyle(TextFormatting.RED));
        }
    }
}
