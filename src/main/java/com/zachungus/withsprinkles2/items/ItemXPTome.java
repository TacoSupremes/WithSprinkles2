package com.zachungus.withsprinkles2.items;

import com.zachungus.withsprinkles2.WithSprinkles2;
import com.zachungus.withsprinkles2.lib.LibMisc;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemXPTome extends ItemMod
{
    @Override
    public String getItemRegistryName()
    {
        return "xp_tome";
    }

    private final String LVL = "LVL_STORED";

    private final String EXP = "EXP_STORED";

    @Override
    public ActionResult<ItemStack> onItemRightClick(World w, PlayerEntity player, Hand hand)
    {
        ItemStack is = player.getHeldItem(hand);

        if(!is.hasTag())
            is.setTag(new CompoundNBT());

        int lvl = is.getTag().getInt(LVL);

        int xpNeeded = getXPForNextLevel(lvl);

        WithSprinkles2.LOGGER.debug(lvl + "," + xpNeeded);
        WithSprinkles2.LOGGER.debug("PLAYER:" + player.experienceLevel + "," + getXPForLevel(player.experienceLevel) + ":" + player.experienceTotal);

        if(player.isSneaking())
        {
            if(player.experienceTotal >= xpNeeded)
            {
                is.getTag().putInt(LVL, lvl + 1) ;
                player.addExperienceLevel(-xpNeeded);
                is.getTag().putInt(EXP, is.getTag().getInt(EXP) + xpNeeded);
            }
            else
            {
                if((getXPForLevel(player.experienceLevel) + player.experienceTotal) >= xpNeeded)
                {
                   player.giveExperiencePoints(-xpNeeded);
                   is.getTag().putInt(LVL, lvl + 1) ;
                }

            }
        }
        else
        {
            if(lvl > 0)
            {
                if(getXPForNextLevel(player.experienceLevel) <= getXPForLevel(lvl))
                {
                    is.getTag().putInt(LVL, lvl - 1);
                    player.addExperienceLevel(1);
                }
            }
        }

        return super.onItemRightClick(w, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent(I18n.format(LibMisc.ModID + ".currentlvl") + ": " + String.valueOf(stack.hasTag() ? stack.getTag().getInt(LVL) : 0)).applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent(I18n.format(LibMisc.ModID + ".nextlvl") + ": " + getXPForNextLevel(stack.hasTag() ? stack.getTag().getInt(LVL) : 0)).applyTextStyle(TextFormatting.GRAY));

    }

    public static int getXPForNextLevel(int level)
    {
        /*
        2 × current_level + 7 (for levels 0–15)
        5 × current_level – 38 (for levels 16–30)
        9 × current_level – 158 (for levels 31+)
        */

        if(level <= 15)
            return 2 * level + 7;
        else if(level <= 30)
            return 5 * level - 38;

        return 9 * level - 158;
    }


    public static int getXPForLevel(int level)
    {
        /*
        level^2 + 6 × level (at levels 0–16)
        2.5 × level^2 – 40.5 × level + 360 (at levels 17–31)
        4.5 × level^2 – 162.5 × level + 2220 (at levels 32+)
        */

        if(level <= 16)
            return (level * level + 6 * level);
        else if (level <= 31)
            return (int)(2.5D * (level * level)  - 40.5D * level + 360);

        return (int)(4.5D * (level * level)  - 162.5D * level + 2220);
    }

}
